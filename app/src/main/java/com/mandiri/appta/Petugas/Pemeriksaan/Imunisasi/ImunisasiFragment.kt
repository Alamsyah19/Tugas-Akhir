package com.mandiri.appta.Petugas.Pemeriksaan.Imunisasi

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.mandiri.appta.Petugas.Pemeriksaan.Pelayanan.PelayananFragment
import com.mandiri.appta.Petugas.Pemeriksaan.PemeriksaanViewModel
import com.mandiri.appta.R
import com.mandiri.appta.databinding.FragmentImunisasiBinding
import java.util.UUID

class ImunisasiFragment : Fragment() {
    private lateinit var binding: FragmentImunisasiBinding
    private val viewModel: PemeriksaanViewModel by activityViewModels()
    private val db = Firebase.firestore

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentImunisasiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnLanjut.setOnClickListener {
            val id_imunisasi = UUID.randomUUID().toString()
            val imunisasi = Imunisasi(
                id_imunisasi = id_imunisasi,
                polio = binding.cbPolio.isChecked,
                dpt_hb = binding.cbDptHb.isChecked,
                campak = binding.cbCampak.isChecked
            )
            db.collection("imunisasi").document(id_imunisasi)
                .set(imunisasi)
                .addOnSuccessListener {
                    viewModel.setImunisasi(imunisasi)

                    parentFragmentManager.beginTransaction()
                        .replace(R.id.container, PelayananFragment())
                        .addToBackStack(null)
                        .commit()
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Gagal menyimpan data imunisasi", Toast.LENGTH_SHORT).show()
                }

        }
    }
}