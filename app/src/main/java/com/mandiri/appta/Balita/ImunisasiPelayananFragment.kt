package com.mandiri.appta.Balita

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.mandiri.appta.R

class ImunisasiPelayananFragment : Fragment() {

    private val viewModel: BalitaViewModel by activityViewModels()
    private lateinit var layoutContainer: LinearLayout

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_imunisasi_pelayanan, container, false)
        layoutContainer = view.findViewById(R.id.imunisasiPelayananList)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val nikBalita = BalitaActivity.nikBalita ?: return
        viewModel.fetchImunisasiPelayanan(nikBalita)
        viewModel.imunisasiPelayanan.observe(viewLifecycleOwner) { data ->
            layoutContainer.removeAllViews()
            data.forEach {
                val tv = TextView(requireContext())
                tv.text = it
                layoutContainer.addView(tv)
            }
        }
    }
}