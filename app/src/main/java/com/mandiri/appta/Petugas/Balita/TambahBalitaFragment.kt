package com.mandiri.appta.Petugas.Balita

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.mandiri.appta.R
import com.mandiri.appta.databinding.FragmentTambahBalitaBinding


class TambahBalitaFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentTambahBalitaBinding? = null
    private val binding get() = _binding!!
    private val db = Firebase.firestore

    private var selectedOrangtuaId: String? = null
    private val orangtuaMap = mutableMapOf<String, String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        // Inflate the layout for this fragment
        _binding = FragmentTambahBalitaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.spinnerOrangtua.threshold = 1
        binding.spinnerOrangtua.setOnClickListener {
            binding.spinnerOrangtua.showDropDown()
        }
        binding.spinnerOrangtua.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) binding.spinnerOrangtua.showDropDown()
        }
        loadOrangtuaList()
        binding.edtTglLahir.setOnClickListener {
            val calendar = java.util.Calendar.getInstance()
            val year = calendar.get(java.util.Calendar.YEAR)
            val month = calendar.get(java.util.Calendar.MONTH)
            val day = calendar.get(java.util.Calendar.DAY_OF_MONTH)

            val datePickerDialog = android.app.DatePickerDialog(
                requireContext(),
                { _, selectedYear, selectedMonth, selectedDay ->
                    // Format: DD-MM-YYYY
                    val formattedDate = String.format("%02d-%02d-%04d", selectedDay, selectedMonth + 1, selectedYear)
                    binding.edtTglLahir.setText(formattedDate)
                },
                year, month, day
            )

            datePickerDialog.show()
        }

        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.jenis_kelamin_array,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerJenisKelamin.adapter = adapter


        binding.btnSimpan.setOnClickListener {
            val nik = binding.edtNik.text.toString().trim()
            val nama = binding.edtNama.text.toString().trim()
            val tglLahir = binding.edtTglLahir.text.toString().trim()
            val beratBadan = binding.edtBeratBadan.text.toString().replace(",", ".").toDoubleOrNull()
            val tinggiBadan = binding.edtPanjangBadan.text.toString().replace(",", ".").toDoubleOrNull()
            val jenisKelamin = binding.spinnerJenisKelamin.selectedItem.toString().trim()
            val dasawisma = binding.edtKelompok.text.toString().trim()

            if (nik.isEmpty()) {
                Toast.makeText(requireContext(), "NIK", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            else if (nama.isEmpty()){
                Toast.makeText(requireContext(), "Nama wajib diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            else if (tglLahir.isEmpty()){
                Toast.makeText(requireContext(), "Tanggal lahir wajib diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            else if (beratBadan==0.0 || beratBadan==null){
                Toast.makeText(requireContext(), "Berat badan wajib diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            else if (tinggiBadan==0.0 || tinggiBadan==null){
                Toast.makeText(requireContext(), "Tinggi badan wajib diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            else if (jenisKelamin=="Pilih Jenis Kelamin"){
                Toast.makeText(requireContext(), "Jenis kelamin wajib diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            db.collection("balita").document(nik).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        Toast.makeText(requireContext(), "NIK sudah digunakan!", Toast.LENGTH_SHORT).show()
                    } else {
                        val data = hashMapOf(
                            "nik" to nik,
                            "nama" to nama,
                            "tgl_lahir" to tglLahir,
                            "berat_badan_lahir" to beratBadan,
                            "tinggi_badan" to tinggiBadan,
                            "jenis_kelamin" to jenisKelamin,
                            "id_orang_tua" to selectedOrangtuaId,
                            "kelompok_dasawisma" to dasawisma,
                        )

                        db.collection("balita").document(nik).set(data)
                            .addOnSuccessListener {
                                Toast.makeText(requireContext(), "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                                dismiss()
                            }
                            .addOnFailureListener {
                                Toast.makeText(requireContext(), "Gagal menambahkan data", Toast.LENGTH_SHORT).show()
                            }
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Gagal mengecek NIK", Toast.LENGTH_SHORT).show()
                }
        }

    }
    private fun loadOrangtuaList() {
        db.collection("orangtua").get()
            .addOnSuccessListener { result ->
                val listNamaAyah = mutableListOf<String>()
                for (doc in result) {
                    val namaAyah = doc.getString("nama_ayah") ?: "Tanpa Nama"
                    listNamaAyah.add(namaAyah)
                    orangtuaMap[namaAyah] = doc.id
                }
                val adapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    listNamaAyah
                )
                binding.spinnerOrangtua.setAdapter(adapter)

                binding.spinnerOrangtua.setOnItemClickListener { _, _, position, _ ->
                    val namaAyah = listNamaAyah[position]
                    selectedOrangtuaId = orangtuaMap[namaAyah]
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}