package com.mandiri.appta.Petugas.OrangTua

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.mandiri.appta.R
import com.mandiri.appta.databinding.FragmentDetailOrangtuaBinding
import java.util.Calendar

class DetailOrangtuaFragment(private val orangTua: OrangTua) : BottomSheetDialogFragment() {
    private var _binding: FragmentDetailOrangtuaBinding? = null
    private val binding get() = _binding!!
    private val db = Firebase.firestore

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDetailOrangtuaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.txtNikAyah.setText(orangTua.nik_ayah)
        binding.txtNamaAyah.setText(orangTua.nama_ayah)
        binding.txtNikAyah.isEnabled = false


        binding.txtNikIbu.setText(orangTua.nik_ibu)
        binding.txtNikIbu.isEnabled = false

        binding.txtNamaIbu.setText(orangTua.nama_ibu)


        binding.txtAlamat.setText(orangTua.alamat)

        binding.txtNoHp.setText(orangTua.no_hp)

        binding.txtIbuKb.setText(orangTua.ibu_kb)

        binding.txtTglLahirAyah.setOnClickListener {
            showDatePicker(binding.txtTglLahirAyah)
        }

        binding.txtTglLahirIbu.setOnClickListener {
            showDatePicker(binding.txtTglLahirIbu)
        }


        binding.btnUpdate.setOnClickListener {

            val updatedData = mapOf(
                "nama_ayah" to binding.txtNamaAyah.text.toString(),
                "nama_ibu" to binding.txtNamaIbu.text.toString(),
                "tgl_lahir_ayah" to binding.txtTglLahirAyah.text.toString(),
                "tgl_lahir_ibu" to binding.txtTglLahirIbu.text.toString(),
                "alamat" to binding.txtAlamat.text.toString(),
                "ibu_kb" to binding.txtIbuKb.text.toString(),
                "no_hp" to binding.txtNoHp.text.toString()
            )


            db.collection("orangtua").whereEqualTo("nik_ayah", orangTua.nik_ayah)
                .get()
                .addOnSuccessListener { documents ->
                    for (doc in documents) {
                        db.collection("orangtua").document(doc.id).update(updatedData)
                            .addOnSuccessListener {
                                Toast.makeText(requireContext(), "Data berhasil diperbarui", Toast.LENGTH_SHORT).show()
                                dismiss()
                            }
                            .addOnFailureListener {
                                Toast.makeText(requireContext(), "Gagal memperbarui data", Toast.LENGTH_SHORT).show()
                            }
                        break
                    }
                }
        }

        binding.btnDelete.setOnClickListener {
            android.app.AlertDialog.Builder(requireContext())
                .setTitle("Konfirmasi Hapus")
                .setMessage("Yakin ingin menghapus data orang tua ini?")
                .setPositiveButton("Hapus") { _, _ ->
                    db.collection("orangtua").whereEqualTo("nik_ayah", orangTua.nik_ayah)
                        .get()
                        .addOnSuccessListener { documents ->
                            for (doc in documents) {
                                db.collection("orangtua").document(doc.id).delete()
                                    .addOnSuccessListener {
                                        Toast.makeText(requireContext(), "Data dihapus", Toast.LENGTH_SHORT).show()
                                        dismiss()
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(requireContext(), "Gagal menghapus data", Toast.LENGTH_SHORT).show()
                                    }
                                break
                            }
                        }
                }
                .setNegativeButton("Batal", null)
                .show()
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