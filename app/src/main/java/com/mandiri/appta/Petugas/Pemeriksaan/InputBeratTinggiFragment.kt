package com.mandiri.appta.Petugas.Pemeriksaan

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.mandiri.appta.Petugas.Pemeriksaan.Imunisasi.ImunisasiFragment
import com.mandiri.appta.R
import com.mandiri.appta.databinding.FragmentInputBeratTinggiBinding

class InputBeratTinggiFragment : Fragment() {
    private var _binding: FragmentInputBeratTinggiBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PemeriksaanViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentInputBeratTinggiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnNext.setOnClickListener {
            val beratStr = binding.edtBerat.text.toString().trim()
            val tinggiStr = binding.edtTinggi.text.toString().trim()
            val lingkarKepalaStr = binding.edtLk.text.toString().trim()
            val lingkarLenganStr = binding.edtLl.text.toString().trim()
            val keteranganStr = binding.edtKeterangan.text.toString().trim().uppercase()

            if (beratStr.isEmpty() || tinggiStr.isEmpty()|| lingkarKepalaStr.isEmpty()|| lingkarLenganStr.isEmpty() || keteranganStr.isEmpty()) {
                Toast.makeText(requireContext(), "Isi semua data", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (keteranganStr !in setOf("N", "T", "B", "O")) {
                Toast.makeText(requireContext(), "Keterangan harus N, T, B, atau O", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val berat = beratStr.replace(",", ".").toDoubleOrNull()
            val tinggi = tinggiStr.replace(",", ".").toDoubleOrNull()
            val lingkarKepala = lingkarKepalaStr.replace(",", ".").toDoubleOrNull()
            val lingkarLengan = lingkarLenganStr.replace(",", ".").toDoubleOrNull()

            if (berat == null || tinggi == null || lingkarKepala == null || lingkarLengan == null) {
                Toast.makeText(requireContext(), "Masukkan angka yang valid", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.beratBadan.value = berat
            viewModel.tinggiBadan.value = tinggi
            viewModel.lingkarKepala.value = lingkarKepala
            viewModel.lingkarLengan.value = lingkarLengan
            viewModel.keteranganTimbangan.value = keteranganStr


            parentFragmentManager.beginTransaction()
                .replace(R.id.container, ImunisasiFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}