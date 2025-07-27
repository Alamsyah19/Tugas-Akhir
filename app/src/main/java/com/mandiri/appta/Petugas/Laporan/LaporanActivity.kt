package com.mandiri.appta.Petugas.Laporan
import com.mandiri.appta.R
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.mandiri.appta.Petugas.Pemeriksaan.lihat.PemeriksaanFull
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*


class LaporanActivity : AppCompatActivity() {
    private val viewModel: LaporanViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout. activity_laporan)

        if (savedInstanceState == null) {
            viewModel.fetchLaporanData()
        }

        val tvTotalBalita = findViewById<TextView>(R.id.tvTotalBalita)
        val layoutPreviewData = findViewById<LinearLayout>(R.id.layoutPreviewData)
        val tvTotalSetahun = findViewById<TextView>(R.id.tvTotalSetahun)

        val laporanId = FirebaseFirestore.getInstance().collection("laporan").document().id

        viewModel.isDataReady.observe(this) { isReady ->
            if (isReady) {
                val totalBalita = viewModel.totalBalita.value ?: 0
                tvTotalBalita.text = "Total Balita Terdaftar: $totalBalita"
                val totalSetahun = viewModel.totalSetahun.value ?: 0
                tvTotalSetahun.text = "Total Pemeriksaan Setahun: $totalSetahun"


                layoutPreviewData.removeAllViews()

                val groupedByMonth = viewModel.groupByMonth()
                var totalPemeriksaan = 0

                for ((bulan, listPerBulan) in groupedByMonth) {
                    val jmlPemeriksaan = listPerBulan.size
                    totalPemeriksaan += jmlPemeriksaan

                    val rowLayout = LinearLayout(this).apply {
                        orientation = LinearLayout.HORIZONTAL
                        layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                    }

                    val tvBulan = TextView(this).apply {
                        layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                        text = bulan
                        gravity = Gravity.CENTER
                        setPadding(8, 8, 8, 8)
                    }

                    val tvJmlPemeriksaan = TextView(this).apply {
                        layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                        text = jmlPemeriksaan.toString()
                        gravity = Gravity.CENTER
                        setPadding(8, 8, 8, 8)
                    }


                    rowLayout.addView(tvBulan)
                    rowLayout.addView(tvJmlPemeriksaan)
                    layoutPreviewData.addView(rowLayout)
                }

                findViewById<Button>(R.id.btnCetakPdf).setOnClickListener {
                    if (viewModel.laporanList.value?.isNotEmpty() == true && totalBalita > 0) {
                        createPdfReport(this, viewModel.laporanList.value ?: emptyList(), totalBalita,totalSetahun,laporanId)
                    } else {
                        Toast.makeText(this, "Data belum siap atau kosong", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun createPdfReport(context: Context, laporanList: List<PemeriksaanFull>, totalBalita: Int,totalSetahun: Int,laporanId: String) {
        val document = PdfDocument()
        val paint = Paint()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = document.startPage(pageInfo)
        var canvas: Canvas = page.canvas
        val date = SimpleDateFormat("yyyy", Locale.getDefault()).format(Date())

        var y = 40
        paint.textSize = 12f

        val tanggalSekarang = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())



        canvas.drawText("Laporan Pemeriksaan Balita Tahunan", 180f, y.toFloat(), paint)
        y += 20
        canvas.drawText("ID Laporan: $laporanId", 40f, y.toFloat(), paint)
        y += 20
        canvas.drawText("Tanggal: $tanggalSekarang", 40f, y.toFloat(), paint)
        y += 30
        canvas.drawText("Total Balita Terdaftar: $totalBalita", 40f, y.toFloat(), paint)
        y += 30
        canvas.drawText("Total Pemeriksaan Setahun: $totalSetahun", 40f, y.toFloat(), paint) // Tambah totalSetahun
        y += 20

        // Header Tabel
        canvas.drawText("Bulan", 40f, y.toFloat(), paint)
        canvas.drawText("Jumlah Pemeriksaan", 180f, y.toFloat(), paint)
        y += 10

        // Garis horizontal untuk header
        canvas.drawLine(40f, y.toFloat(), 550f, y.toFloat(), paint.apply { strokeWidth = 1f })
        y += 20

        // Kelompokkan data per bulan
        val groupedByMonth = viewModel.groupByMonth()
        var totalPemeriksaan = 0

        for ((bulan, listPerBulan) in groupedByMonth) {
            val jmlPemeriksaan = listPerBulan.size
            totalPemeriksaan += jmlPemeriksaan


            canvas.drawText(bulan, 40f, y.toFloat(), paint)
            canvas.drawText(jmlPemeriksaan.toString(), 200f, y.toFloat(), paint)
            y += 20

            if (y > 800) {
                document.finishPage(page)
                val newPage = document.startPage(pageInfo)
                canvas = newPage.canvas
                canvas.drawText("Laporan Pemeriksaan Balita Tahunan (lanjutan)", 180f, 40f, paint)
                y = 70
            }
        }

        // Total Keseluruhan
        y += 20
        canvas.drawText("Total Pemeriksaan $date", 40f, y.toFloat(), paint)
        canvas.drawText(totalPemeriksaan.toString(), 200f, y.toFloat(), paint)

        document.finishPage(page)

        val file = File(context.getExternalFilesDir(null), "LaporanPemeriksaanTahunan.pdf")
        try {
            document.writeTo(FileOutputStream(file))
            Toast.makeText(context, "PDF disimpan di: ${file.absolutePath}", Toast.LENGTH_LONG).show()

            val firestore = FirebaseFirestore.getInstance()
            val dataLaporan = Laporan(
                id_laporan = laporanId,
                total_balita = totalBalita,
                total_pemeriksaan = totalPemeriksaan,
                tgl_laporan = tanggalSekarang,
                totalSetahun =totalSetahun ,
            )
            firestore.collection("laporan").document(laporanId).set(dataLaporan)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Gagal menyimpan PDF", Toast.LENGTH_SHORT).show()
        } finally {
            document.close()
        }
    }
}