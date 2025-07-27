package com.mandiri.appta.Petugas.Pemeriksaan.Asi

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.mandiri.appta.Petugas.Pemeriksaan.KonfirmasiFragment
import com.mandiri.appta.Petugas.Pemeriksaan.PemeriksaanViewModel
import com.mandiri.appta.R
import com.mandiri.appta.databinding.FragmentAsiBinding

class AsiFragment : Fragment() {
    private lateinit var binding: FragmentAsiBinding
    private val viewModel: PemeriksaanViewModel by activityViewModels()
    private val db = Firebase.firestore

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentAsiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnLanjut.setOnClickListener {
            val idAsi = db.collection("pemberian_asi").document().id
            val asi = PemberianAsi(
                id_pemberian_asi = idAsi,
                e1= binding.cbEksklusif1.isChecked,
                e2= binding.cbEksklusif2.isChecked,
                e3= binding.cbEksklusif3.isChecked,
                e4= binding.cbEksklusif4.isChecked,
                e5= binding.cbEksklusif5.isChecked,
                e6= binding.cbEksklusif6.isChecked,
            )

            db.collection("pemberian_asi").document(idAsi)
                .set(asi)
                .addOnSuccessListener {
                    viewModel.setAsi(asi)

                    parentFragmentManager.beginTransaction()
                        .replace(R.id.container, KonfirmasiFragment())
                        .addToBackStack(null)
                        .commit()
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Gagal menyimpan data ASI", Toast.LENGTH_SHORT).show()
                }
        }
    }
}