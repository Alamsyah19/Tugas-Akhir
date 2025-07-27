package com.mandiri.appta.Balita

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.mandiri.appta.R


class PemeriksaanPerBulanFragment : Fragment() {

    private val viewModel: BalitaViewModel by activityViewModels()
    private lateinit var layoutContainer: LinearLayout



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_pemeriksaan_per_bulan, container, false)
        layoutContainer = view.findViewById(R.id.pemeriksaanPerBulanList)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val nikBalita = BalitaActivity.nikBalita ?: return
        viewModel.fetchPemeriksaanBulanan(nikBalita)
        viewModel.pemeriksaanBulanan.observe(viewLifecycleOwner) { data ->
            layoutContainer.removeAllViews()
            val tvTotal = TextView(view.context)
            val total = data.values.sum()
            tvTotal.text = "Total Pemeriksaan Tahun Ini: $total"
            layoutContainer.addView(tvTotal)
            for ((bulan, jumlah) in data) {
                val tv = TextView(requireContext())
                tv.text = "$bulan: $jumlah pemeriksaan"
                layoutContainer.addView(tv)
            }
        }
    }
}