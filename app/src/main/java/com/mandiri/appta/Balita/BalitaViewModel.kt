package com.mandiri.appta.Balita

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import java.text.SimpleDateFormat
import java.util.*

class BalitaViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private var pemeriksaanListener: ListenerRegistration? = null

    private val _pemeriksaanBulanan = MutableLiveData<Map<String, Int>>()
    val pemeriksaanBulanan: LiveData<Map<String, Int>> = _pemeriksaanBulanan

    private val _imunisasiPelayanan = MutableLiveData<List<String>>()
    val imunisasiPelayanan: LiveData<List<String>> = _imunisasiPelayanan

    private val _dataPertumbuhan = MutableLiveData<List<Quintuple<Date, Float, Float,Float,Float>>>()
    val dataPertumbuhan: LiveData<List<Quintuple<Date, Float,Float, Float,Float>>> = _dataPertumbuhan

    fun fetchPemeriksaanBulanan(nikBalita: String) {
        pemeriksaanListener?.remove()
        pemeriksaanListener = firestore.collection("pemeriksaan")
            .whereEqualTo("nik_balita", nikBalita)
            .addSnapshotListener { snapshot, _ ->
                if (snapshot != null) {
                    val grouped = snapshot.groupBy {
                        val dateStr = it.getString("tanggal_pemeriksaan") ?: ""
                        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(dateStr)?.let { date ->
                            SimpleDateFormat("MMMM yyyy", Locale("id")).format(date)
                        } ?: "Tidak diketahui"
                    }
                    val result = grouped.mapValues { it.value.size }
                    _pemeriksaanBulanan.value = result
                }
            }
    }

    fun fetchImunisasiPelayanan(nikBalita: String) {
        firestore.collection("pemeriksaan")
            .whereEqualTo("nik_balita", nikBalita)
            .addSnapshotListener { snapshot, _ ->
                val results = mutableListOf<String>()
                if (snapshot != null) {
                    for (doc in snapshot) {
                        val idImunisasi = doc.getString("id_imunisasi") ?: continue
                        val idPelayanan = doc.getString("id_pelayanan") ?: continue
                        val tanggalPemeriksaan = doc.getString("tanggal_pemeriksaan") ?: "Tanggal tidak diketahui"

                        firestore.collection("imunisasi").document(idImunisasi).get().addOnSuccessListener { imunisasiDoc ->
                            firestore.collection("pelayanan").document(idPelayanan).get().addOnSuccessListener { pelayananDoc ->
                                // Ambil data dan konversi ke format user-friendly
                                val polio = imunisasiDoc.getBoolean("polio")?.let { if (it) "Ya" else "Tidak" } ?: "Belum Tercatat"
                                val vitaminA = pelayananDoc.getBoolean("vitamin_a")?.let { if (it) "Ya" else "Tidak" } ?: "Belum Tercatat"
                                val campak = imunisasiDoc.getBoolean("campak")?.let { if (it) "Ya" else "Tidak" } ?: "Belum Tercatat"
                                val dptHb = imunisasiDoc.getBoolean("dpt_hb")?.let { if (it) "Ya" else "Tidak" } ?: "Belum Tercatat"

                                // Format hasil dengan baris baru untuk setiap item
                                val result = """
                                Pemeriksaan pada: $tanggalPemeriksaan
                                - Status Vaksin Polio: $polio
                                - Status Vitamin A: $vitaminA
                                - Status Vaksin Campak: $campak
                                - Status Vaksin DPT-HB: $dptHb
                            """.trimIndent()

                                results.add(result)
                                _imunisasiPelayanan.value = results.toList()
                            }
                        }
                    }
                }
            }
    }

    fun fetchDataPertumbuhan(nikBalita: String) {
        firestore.collection("balita")
            .whereEqualTo("nik", nikBalita)
            .limit(1)
            .get()
            .addOnSuccessListener { balitaSnapshot ->
                val balitaDoc = balitaSnapshot.documents.firstOrNull()
                val bbAwal = balitaDoc?.getDouble("berat_badan_lahir")?.toFloat()?:0f
                val tbAwal = balitaDoc?.getDouble("tinggi_badan")?.toFloat()?:0f
                val tglLahirStr = balitaDoc?.getString("tgl_lahir")
                val tglAwal: Date? = try {
                    tglLahirStr?.let {
                        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(it)
                    }
                } catch (e: Exception) {
                    null
                }

                // Ambil data pemeriksaan dari Firestore
                firestore.collection("pemeriksaan")
                    .whereEqualTo("nik_balita", nikBalita)
                    .addSnapshotListener { snapshot, _ ->
                        if (snapshot != null) {
                            val list = mutableListOf<Quintuple<Date, Float,Float, Float,Float>>()

                            // Masukkan data awal dari balita
                            if (tglAwal != null) {
                                list.add(Quintuple(tglAwal, bbAwal, tbAwal,0F,0F))
                            }

                            // Tambahkan data dari pemeriksaan
                            snapshot.mapNotNullTo(list) { doc ->
                                val dateStr = doc.getString("tanggal_pemeriksaan") ?: return@mapNotNullTo null
                                val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(dateStr)
                                val bb = doc.getDouble("berat_badan")?.toFloat() ?: return@mapNotNullTo null
                                val tb = doc.getDouble("tinggi_badan")?.toFloat() ?: return@mapNotNullTo null
                                val lk = doc.getDouble("lingkar_kepala")?.toFloat() ?: 0f
                                val ll = doc.getDouble("lingkar_lengan")?.toFloat() ?: 0f
                                if (date != null) Quintuple(date, bb, tb,lk,ll) else null
                            }

                            _dataPertumbuhan.value = list.sortedBy { it.first }
                        }
                    }

            }
    }

    override fun onCleared() {
        super.onCleared()
        pemeriksaanListener?.remove()
    }
}

data class Quintuple<out A, out B, out C, out D,out E>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D,
    val fifth: E
)
