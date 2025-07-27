package com.mandiri.appta.Petugas.Pemeriksaan.lihat

import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.firestore
import com.mandiri.appta.Petugas.Balita.Balita
import com.mandiri.appta.Petugas.Pemeriksaan.Asi.PemberianAsi
import com.mandiri.appta.Petugas.Pemeriksaan.Imunisasi.Imunisasi
import com.mandiri.appta.Petugas.Pemeriksaan.Pelayanan.Pelayanan
import com.mandiri.appta.Petugas.Pemeriksaan.Pemeriksaan
import com.mandiri.appta.R

class LihatPemeriksaanActivity : AppCompatActivity() {

    private lateinit var adapter: GroupedPemeriksaanAdapter
    private val db = Firebase.firestore
    private var fullGroupedList = listOf<PemeriksaanGroup>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lihat_pemeriksaan)

        adapter =  GroupedPemeriksaanAdapter()
        findViewById<RecyclerView>(R.id.rvPemeriksaan).apply {
            layoutManager = LinearLayoutManager(this@LihatPemeriksaanActivity)
            adapter = this@LihatPemeriksaanActivity.adapter
        }

        fetchPemeriksaanGrouped()

        val searchView = findViewById<SearchView>(R.id.etSearch)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                filterPemeriksaan(newText)
                return true
            }
        })
    }
    private fun filterPemeriksaan(query: String?) {
        if (query.isNullOrEmpty()) {
            adapter.submitList(fullGroupedList)
            return
        }

        val filtered = fullGroupedList.filter { group ->
            group.balita.contains(query, ignoreCase = true) ||
                    group.listPemeriksaan.any { it.balita?.nik?.contains(query, ignoreCase = true) == true }
        }

        adapter.submitList(filtered)
    }
    private fun fetchPemeriksaanGrouped() {
        db.collection("pemeriksaan").get().addOnSuccessListener { pemeriksaanDocs ->
            val detailTasks = pemeriksaanDocs.map { pemeriksaanDoc ->
                val pemeriksaan = pemeriksaanDoc.toObject(Pemeriksaan::class.java)

                val balitaTask = db.collection("balita").document(pemeriksaan.nik_balita).get()
                val pelayananTask = db.collection("pelayanan").document(pemeriksaan.id_pelayanan).get()
                val imunisasiTask = db.collection("imunisasi").document(pemeriksaan.id_imunisasi).get()
                val asiTask = db.collection("pemberian_asi").document(pemeriksaan.id_pemberian_asi).get()

                Tasks.whenAllSuccess<DocumentSnapshot>(
                    listOf(balitaTask, pelayananTask, imunisasiTask, asiTask)
                ).continueWith { task ->
                    val results = task.result
                    val balita = results[0].toObject(Balita::class.java)
                    val pelayanan = results[1].toObject(Pelayanan::class.java)
                    val imunisasi = results[2].toObject(Imunisasi::class.java)
                    val asi = results[3].toObject(PemberianAsi::class.java)

                    PemeriksaanFull(pemeriksaan, balita, pelayanan, imunisasi, asi)
                }
            }


            Tasks.whenAll(detailTasks).addOnSuccessListener {
                val fullList = detailTasks.mapNotNull { it.result }

                val grouped = fullList.groupBy { it.balita?.nama ?: "Tidak diketahui" }
                    .map { (nama, list) -> PemeriksaanGroup(nama, list) }

                fullGroupedList = grouped
                adapter.submitList(grouped)
            }.addOnFailureListener {
                Toast.makeText(this, "Gagal mengambil data detail", Toast.LENGTH_SHORT).show()
            }

        }.addOnFailureListener {
            Toast.makeText(this, "Gagal mengambil data pemeriksaan", Toast.LENGTH_SHORT).show()
        }
    }

}