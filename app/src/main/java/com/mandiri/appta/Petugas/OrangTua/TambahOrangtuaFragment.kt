package com.mandiri.appta.Petugas.OrangTua

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.mandiri.appta.R
import com.mandiri.appta.databinding.FragmentTambahOrangtuaBinding
import java.util.Calendar


class TambahOrangtuaFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentTambahOrangtuaBinding? = null
    private val binding get() = _binding!!
    private val db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTambahOrangtuaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.edtTglLahirAyah.setOnClickListener {
            showDatePicker(binding.edtTglLahirAyah)
        }
        binding.edtTglLahirIbu.setOnClickListener {
            showDatePicker(binding.edtTglLahirIbu)
        }

        binding.btnSimpan.setOnClickListener {
            val nikAyah = binding.edtNikAyah.text.toString().trim()
            val nikIbu = binding.edtNikIbu.text.toString().trim()
            val namaAyah = binding.edtNamaAyah.text.toString().trim()
            val namaIbu = binding.edtNamaIbu.text.toString().trim()
            val tglAyah = binding.edtTglLahirAyah.text.toString().trim()
            val tglIbu = binding.edtTglLahirIbu.text.toString().trim()
            val alamat = binding.edtAlamat.text.toString().trim()
            val noHp = binding.edtNoHp.text.toString().trim()
            val ibuKb = binding.edtIbuKb.text.toString().trim()

            if (nikAyah.isEmpty() || nikIbu.isEmpty() || namaAyah.isEmpty() || namaIbu.isEmpty() ||
                tglAyah.isEmpty() || tglIbu.isEmpty() || alamat.isEmpty() || noHp.isEmpty()
            ) {
                Toast.makeText(requireContext(), "Semua field wajib diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            db.collection("orangtua")
                .whereEqualTo("nik_ayah", nikAyah)
                .get()
                .addOnSuccessListener { resultAyah ->
                    db.collection("orangtua")
                        .whereEqualTo("nik_ibu", nikIbu)
                        .get()
                        .addOnSuccessListener { resultIbu ->

                            // Jika keduanya kosong, lanjutkan tambah data
                            if (resultAyah.isEmpty && resultIbu.isEmpty) {
                                val newDoc = db.collection("orangtua").document()
                                val data = OrangTua(
                                    id = newDoc.id,
                                    nik_ayah = nikAyah,
                                    nik_ibu = nikIbu,
                                    nama_ayah = namaAyah,
                                    nama_ibu = namaIbu,
                                    tgl_lahir_ayah = tglAyah,
                                    tgl_lahir_ibu = tglIbu,
                                    alamat = alamat,
                                    no_hp = noHp,
                                    ibu_kb = ibuKb,
                                )

                                db.collection("orangtua").document(newDoc.id).set(data)
                                    .addOnSuccessListener {
                                        Toast.makeText(requireContext(), "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                                        dismiss()
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(requireContext(), "Gagal menyimpan data", Toast.LENGTH_SHORT).show()
                                    }
                            } else {
                                Toast.makeText(requireContext(), "NIK Ayah atau Ibu sudah terdaftar", Toast.LENGTH_SHORT).show()
                            }

                        }
                        .addOnFailureListener {
                            Toast.makeText(requireContext(), "Gagal cek NIK Ibu", Toast.LENGTH_SHORT).show()
                        }
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Gagal cek NIK Ayah", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun showDatePicker(target: EditText) {
        val calendar = Calendar.getInstance()
        val datePicker = DatePickerDialog(requireContext(),
            { _, year, month, day ->
                target.setText(String.format("%02d-%02d-%04d", day, month + 1, year))
            },
            calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}