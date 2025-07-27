package com.mandiri.appta.Petugas.Pemeriksaan

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.mandiri.appta.Petugas.Balita.Balita
import com.mandiri.appta.R
import com.mandiri.appta.databinding.FragmentPemeriksanBalitaBinding


class PemeriksanBalitaFragment: Fragment() {

    private var _binding: FragmentPemeriksanBalitaBinding? = null
    private val binding get() = _binding!!
    private val db = Firebase.firestore
    private lateinit var adapter: ArrayAdapter<String>
    private val displayToBalitaMap = mutableMapOf<String, Balita>()
    private lateinit var viewModel: PemeriksaanViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPemeriksanBalitaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(requireActivity())[PemeriksaanViewModel::class.java]

        adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, mutableListOf())
        binding.selectBalita.setAdapter(adapter)

        db.collection("balita")
            .get()
            .addOnSuccessListener { result ->
                val displayList = mutableListOf<String>()
                for (doc in result) {
                    val balita = doc.toObject(Balita::class.java)
                    val displayText = "${balita.nama} - ${balita.nik}"
                    displayList.add(displayText)
                    displayToBalitaMap[displayText] = balita
                }
                adapter.clear()
                adapter.addAll(displayList)
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Gagal memuat data balita", Toast.LENGTH_SHORT).show()
            }

        binding.btnNext.setOnClickListener {
            val BalitaDisplay = binding.selectBalita.text.toString()
            val selectedBalita = displayToBalitaMap[BalitaDisplay]
            if (selectedBalita != null) {
                viewModel.setSelectedBalita(selectedBalita)

                parentFragmentManager.beginTransaction()
                    .replace(R.id.container, InputBeratTinggiFragment())
                    .addToBackStack(null)
                    .commit()
            } else {
                Toast.makeText(requireContext(), "Balita tidak ditemukan", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
