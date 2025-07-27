package com.mandiri.appta.Balita

import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.ValueFormatter
import com.mandiri.appta.databinding.FragmentGrafikPertumbuhanBinding
import java.text.SimpleDateFormat
import java.util.*

class GrafikPertumbuhanFragment : Fragment() {

    private val viewModel: BalitaViewModel by viewModels()
    private var _binding: FragmentGrafikPertumbuhanBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentGrafikPertumbuhanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nikBalita = BalitaActivity.nikBalita ?: return
        viewModel.fetchDataPertumbuhan(nikBalita)

        viewModel.dataPertumbuhan.observe(viewLifecycleOwner) { data ->
            val labels = data.map {
                SimpleDateFormat("dd MMM", Locale("id")).format(it.first)
            }

            //BB
            setChartData(
                binding.chartBB,
                data.mapIndexed { index, triple -> Entry(index.toFloat(), triple.second) },
                "Berat Badan (kg)"
            )
            //TB
            setChartData(
                binding.chartTB,
                data.mapIndexed { index, triple -> Entry(index.toFloat(), triple.third) },
                "Tinggi Badan (cm)"
            )
            //LK
            setChartData(
                binding.chartLK,
                data.mapIndexed { index, triple -> Entry(index.toFloat(), triple.fourth) },
                "Lingkar Kepala (cm)"
            )
            //LL
            setChartData(
                binding.chartLL,
                data.mapIndexed { index, triple -> Entry(index.toFloat(), triple.fifth) },
                "Lingkar Lengan (cm)"
            )
            setXAxis(binding.chartBB, labels)
            setXAxis(binding.chartTB, labels)
        }
    }

    private fun setChartData(chart: LineChart, entries: List<Entry>, label: String) {
        val dataSet = LineDataSet(entries, label).apply {
            color = Color.BLUE
            setCircleColor(Color.RED)
            lineWidth = 2f
            circleRadius = 4f
            valueTextSize = 10f
        }

        chart.data = LineData(dataSet)
        chart.invalidate()
    }

    private fun setXAxis(chart: LineChart, labels: List<String>) {
        chart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            granularity = 1f
            valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    val index = value.toInt()
                    return if (index >= 0 && index < labels.size) labels[index] else ""
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
