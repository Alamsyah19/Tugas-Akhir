package com.mandiri.appta.Petugas.Pemeriksaan

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.type.Date
import com.mandiri.appta.R
import com.mandiri.appta.databinding.FragmentKonfirmasiBinding
import java.util.Locale


class KonfirmasiFragment : Fragment() {
    private lateinit var binding: FragmentKonfirmasiBinding
    private val viewModel: PemeriksaanViewModel by activityViewModels()
    private val db = Firebase.firestore

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentKonfirmasiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val balita = viewModel.selectedBalita.value
        val pelayanan = viewModel.pelayananData.value
        val imunisasi = viewModel.imunisasiData.value
        val asi = viewModel.asiData.value
        val berat = viewModel.beratBadan.value?:0.0
        val tinggi = viewModel.tinggiBadan.value ?: 0.0
        val lingkarKepala = viewModel.lingkarKepala.value ?: 0.0
        val lingkarLengan = viewModel.lingkarLengan.value ?: 0.0
        val KeteranganTimbangan = viewModel.keteranganTimbangan.value ?: ""

        val konfirmasiText = """
            NIK Balita: ${balita?.nik}
            Nama: ${balita?.nama}
            
            
            Pelayanan:
            - Vitamin A: ${pelayanan?.vitamin_a}
            - Oralit: ${pelayanan?.oralit}
            
            Imunisasi:
            - Polio: ${imunisasi?.polio}
            - DPT-HB: ${imunisasi?.dpt_hb}
            - Campak: ${imunisasi?.campak}
            
            Pemberian ASI:
            - Eksklusif 1: ${asi?.e1}
            - Eksklusif 2: ${asi?.e2}
            - Eksklusif 3: ${asi?.e3}
            - Eksklusif 4: ${asi?.e4}
            - Eksklusif 5: ${asi?.e5}
            - Eksklusif 6: ${asi?.e6}
            
            Data Perkembangan Balita:
            - Berat Badan: ${berat} kg
            - Tinggi Badan: ${tinggi} cm
            - Lingkar Kepala: ${lingkarKepala} cm
            - Lingkar Lengan: ${lingkarLengan} cm
            - Keterangan Timbangan: ${KeteranganTimbangan}
        """.trimIndent()

        binding.tvKonfirmasi.text = konfirmasiText

        binding.btnSimpan.setOnClickListener {
            val id = db.collection("pemeriksaan").document().id
            val now = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(java.util.Date())
            val nikBalita = viewModel.selectedBalita.value?.nik ?: ""
//            val namaBalita = viewModel.selectedBalita.value?.nama ?: ""
            db.collection("balita").document(nikBalita).get().addOnSuccessListener { document ->
                if (document.exists()){
                    val tglLahir=document.getString("tgl_lahir")?:""
                    val umurBulan=Umur(tglLahir)

                    val pemeriksaan = Pemeriksaan(
                        id_pemeriksaan = id,
                        nik_balita = balita?.nik ?: "",
                        nama_balita = balita?.nama ?: "",
                        berat_badan = berat,
                        tinggi_badan = tinggi,
                        lingkar_kepala = lingkarKepala,
                        lingkar_lengan = lingkarLengan,
                        keteranganTimbangan = KeteranganTimbangan,
                        umur_balita = umurBulan,
                        id_pelayanan = pelayanan?.id_pelayanan ?: "",
                        id_imunisasi = imunisasi?.id_imunisasi ?: "",
                        id_pemberian_asi = asi?.id_pemberian_asi ?: "",
                        tanggal_pemeriksaan = now
                    )
                    db.collection("pemeriksaan").document(id)
                        .set(pemeriksaan)
                        .addOnSuccessListener {
                            Toast.makeText(requireContext(), "Data pemeriksaan disimpan", Toast.LENGTH_SHORT).show()
                            parentFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                            requireActivity().finish()
                        }
                        .addOnFailureListener {
                            Toast.makeText(requireContext(), "Gagal menyimpan data", Toast.LENGTH_SHORT).show()
                        }

                }
                else{
                    Toast.makeText(requireContext(), "Data balita tidak ditemukan", Toast.LENGTH_SHORT).show()
                }

            }.addOnFailureListener {
                    Toast.makeText(requireContext(), "Gagal mengambil data balita", Toast.LENGTH_SHORT).show()
            }

        }
    }
    private fun Umur(tglLahirStr: String): Int {
        return try {
            val format = java.text.SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            val tanggalLahir = format.parse(tglLahirStr) ?: return 0

            val lahir = java.util.Calendar.getInstance().apply { time = tanggalLahir }
            val sekarang = java.util.Calendar.getInstance()

            var tahun = sekarang.get(java.util.Calendar.YEAR) - lahir.get(java.util.Calendar.YEAR)
            var bulan = sekarang.get(java.util.Calendar.MONTH) - lahir.get(java.util.Calendar.MONTH)
            println("Tahun: $tahun, Bulan: $bulan")
            // Koreksi jika hari belum mencapai tanggal lahir di bulan ini
            if (sekarang.get(java.util.Calendar.DAY_OF_MONTH) < lahir.get(java.util.Calendar.DAY_OF_MONTH)) {
                bulan--
            }
            if (bulan < 0) {
                tahun--
                bulan += 12
            }

            (tahun * 12) + bulan

        } catch (e: Exception) {
            0
        }
    }
}