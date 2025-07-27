package com.mandiri.appta.Petugas.Pemeriksaan.Pelayanan

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.mandiri.appta.Petugas.Pemeriksaan.Asi.AsiFragment
import com.mandiri.appta.Petugas.Pemeriksaan.PemeriksaanViewModel
import com.mandiri.appta.R
import com.mandiri.appta.databinding.FragmentPelayananBinding

class PelayananFragment : Fragment() {
    private lateinit var binding: FragmentPelayananBinding
    private val viewModel: PemeriksaanViewModel by activityViewModels()
    private val db = Firebase.firestore

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentPelayananBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnLanjut.setOnClickListener {
            val idPelayanan = db.collection("pelayanan").document().id
            val pelayanan = Pelayanan(
                id_pelayanan = idPelayanan,
                vitamin_a = binding.cbVitaminA.isChecked,
                oralit = binding.cbOralit.isChecked
            )

            db.collection("pelayanan").document(idPelayanan)
                .set(pelayanan)
                .addOnSuccessListener {
                    viewModel.setPelayanan(pelayanan)

                    parentFragmentManager.beginTransaction()
                        .replace(R.id.container, AsiFragment())
                        .addToBackStack(null)
                        .commit()
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Gagal menyimpan data pelayanan", Toast.LENGTH_SHORT).show()
                }
        }
    }
}