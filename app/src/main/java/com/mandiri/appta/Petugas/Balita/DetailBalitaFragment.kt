package com.mandiri.appta.Petugas.Balita

import android.app.DatePickerDialog
import android.os.Bundle
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
import com.mandiri.appta.databinding.FragmentDetailBalitaBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DetailBalitaFragment(private val balita: Balita) : BottomSheetDialogFragment() {
    private var _binding: FragmentDetailBalitaBinding? = null
    private val binding get() = _binding!!
    private val db = Firebase.firestore

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDetailBalitaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.jenis_kelamin_array,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerJenisKelamin.adapter = adapter

        binding.edtNama.setText(balita.nama)

        binding.edtNik.setText(balita.nik)
        binding.edtNik.isEnabled = false

        //Tgl Lahir
        binding.edtTglLahir.setText(balita.tgl_lahir)
        binding.edtTglLahir.setOnClickListener {
            showDatePicker()
        }
        binding.edtTglLahir.inputType = android.text.InputType.TYPE_NULL
        binding.edtTglLahir.keyListener = null

        binding.edtBeratBadan.setText(balita.berat_badan_lahir.toString())
        binding.edtTinggiBadan.setText(balita.tinggi_badan.toString())

        binding.edtKelompok.setText(balita.kelompok_dasawisma)

        val jenisKelaminArray = resources.getStringArray(R.array.jenis_kelamin_array)
        val selectedIndex = jenisKelaminArray.indexOf(balita.jenis_kelamin)
        if (selectedIndex >= 0) {
            binding.spinnerJenisKelamin.setSelection(selectedIndex)
        }




        binding.btnUpdate.setOnClickListener {
            val beratLahir = binding.edtBeratBadan.text.toString().replace(",", ".").toDoubleOrNull()
            val tinggi = binding.edtTinggiBadan.text.toString().replace(",", ".").toDoubleOrNull()
            val jenisKelamin = binding.spinnerJenisKelamin.selectedItem.toString().trim()
            if (jenisKelamin == "Pilih Jenis Kelamin") {
                Toast.makeText(requireContext(), "Silakan pilih jenis kelamin", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val updatedData = mapOf(
                "nama" to binding.edtNama.text.toString(),
                "tgl_lahir" to binding.edtTglLahir.text.toString(),
                "berat_badan_lahir" to beratLahir,
                "tinggi_badan" to tinggi,
                "jenis_kelamin" to jenisKelamin,
                "kelompok_dasawisma" to binding.edtKelompok.text.toString(),
            )
            db.collection("balita").document(balita.nik)
                .update(updatedData)
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "Data diperbarui", Toast.LENGTH_SHORT).show()
                    dismiss()
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Gagal memperbarui data", Toast.LENGTH_SHORT).show()
                }
        }

        binding.btnDelete.setOnClickListener {
            android.app.AlertDialog.Builder(requireContext())
                .setTitle("Konfirmasi Hapus")
                .setMessage("Apakah kamu yakin ingin menghapus data balita ini?")
                .setPositiveButton("Hapus") { _, _ ->
                    db.collection("balita").document(balita.nik)
                        .delete()
                        .addOnSuccessListener {
                            Toast.makeText(requireContext(), "Data dihapus", Toast.LENGTH_SHORT).show()
                            dismiss()
                        }
                        .addOnFailureListener {
                            Toast.makeText(requireContext(), "Gagal menghapus data", Toast.LENGTH_SHORT).show()
                        }
                }
                .setNegativeButton("Batal", null)
                .show()
        }

    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()

        // Kalau sudah ada tanggal lahir sebelumnya, gunakan itu sebagai default di DatePicker
        val existingDate = binding.edtTglLahir.text.toString()
        if (existingDate.isNotEmpty()) {
            try {
                val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                val date = sdf.parse(existingDate)
                date?.let {
                    calendar.time = it
                }
            } catch (_: Exception) {}
        }

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(selectedYear, selectedMonth, selectedDay)

                val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                binding.edtTglLahir.setText(sdf.format(selectedDate.time))
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}