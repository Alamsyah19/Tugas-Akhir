package com.mandiri.appta.Petugas.Laporan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.mandiri.appta.Petugas.Balita.Balita
import com.mandiri.appta.Petugas.Pemeriksaan.Asi.PemberianAsi
import com.mandiri.appta.Petugas.Pemeriksaan.Imunisasi.Imunisasi
import com.mandiri.appta.Petugas.Pemeriksaan.Pelayanan.Pelayanan
import com.mandiri.appta.Petugas.Pemeriksaan.lihat.PemeriksaanFull
import java.text.SimpleDateFormat

import java.util.*

class LaporanViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()


    private val _laporanList = MutableLiveData<List<PemeriksaanFull>>()
    val laporanList: LiveData<List<PemeriksaanFull>> get() = _laporanList

    private val _totalBalita = MutableLiveData<Int>()
    val totalBalita: LiveData<Int> get() = _totalBalita

    private val _totalSetahun = MutableLiveData<Int>()
    val totalSetahun: LiveData<Int> get() = _totalSetahun

    private val _isDataReady = MutableLiveData<Boolean>()
    val isDataReady: LiveData<Boolean> get() = _isDataReady


    fun fetchLaporanData() {
        if (_laporanList.value?.isNotEmpty() == true) {
            _isDataReady.value = true
            return
        }

        _isDataReady.value = false

        // Ambil total balita
        db.collection("balita").get().addOnSuccessListener { balitaDocs ->
            _totalBalita.value = balitaDocs.size()
            fetchPemeriksaanData()
        }.addOnFailureListener {
            _isDataReady.value = true
            _totalBalita.value = 0
            _totalSetahun.value=0
        }
    }

    private fun fetchPemeriksaanData() {
        db.collection("pemeriksaan").get().addOnSuccessListener { pemeriksaanDocs ->
            val pending = mutableListOf<com.google.android.gms.tasks.Task<List<com.google.firebase.firestore.DocumentSnapshot>>>()
            val  currentList= mutableListOf<PemeriksaanFull>()
            for (doc in pemeriksaanDocs) {
                val pemeriksaan = doc.toObject(com.mandiri.appta.Petugas.Pemeriksaan.Pemeriksaan::class.java)

                val balitaTask = db.collection("balita").document(pemeriksaan.nik_balita).get()
                val pelayananTask = db.collection("pelayanan").document(pemeriksaan.id_pelayanan).get()
                val imunisasiTask = db.collection("imunisasi").document(pemeriksaan.id_imunisasi).get()
                val asiTask = db.collection("pemberian_asi").document(pemeriksaan.id_pemberian_asi).get()

                val allTask = com.google.android.gms.tasks.Tasks.whenAllSuccess<com.google.firebase.firestore.DocumentSnapshot>(
                    listOf(balitaTask, pelayananTask, imunisasiTask, asiTask)
                ).addOnSuccessListener { results ->
                    val balita = results[0].toObject(Balita::class.java)
                    val pelayanan = results[1].toObject(Pelayanan::class.java)
                    val imunisasi = results[2].toObject(Imunisasi::class.java)
                    val asi = results[3].toObject(PemberianAsi::class.java)

                    val full = PemeriksaanFull(pemeriksaan, balita, pelayanan, imunisasi, asi)
                    currentList.add(full)
                    _laporanList.value = currentList.sortedByDescending { it.pemeriksaan.tanggal_pemeriksaan }
                }

                pending.add(allTask)
            }

            com.google.android.gms.tasks.Tasks.whenAllComplete(pending).addOnSuccessListener {
                _totalSetahun.value=groupByMonth().values.sumOf { it.size }
                _isDataReady.value = true
            }
        }.addOnFailureListener {
            _isDataReady.value = true
            _laporanList.value = emptyList()
            _totalSetahun.value=0
        }
    }


    fun groupByMonth(): Map<String, List<PemeriksaanFull>> {
        val formatter = SimpleDateFormat("yyyy-MM", Locale.getDefault())
        return _laporanList.value.orEmpty().groupBy {
            formatter.format(SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .parse(it.pemeriksaan.tanggal_pemeriksaan) ?: Date())
        }.filter { entry ->
            val cal = Calendar.getInstance().apply { time = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(entry.key + "-01") ?: Date() }
            cal.get(Calendar.YEAR) >= Calendar.getInstance().get(Calendar.YEAR) - 1
        }
    }
}