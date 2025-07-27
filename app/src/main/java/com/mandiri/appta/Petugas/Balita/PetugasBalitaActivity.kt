package com.mandiri.appta.Petugas.Balita

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

import com.mandiri.appta.R

class PetugasBalitaActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: BalitaAdapter
    private val viewModel: BalitaViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_petugas_balita)

        recyclerView = findViewById(R.id.recycler_balita)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = BalitaAdapter(mutableListOf()) { balita ->
            val dialog = DetailBalitaFragment(balita)
            dialog.show(supportFragmentManager, "DetailBalita")
        }
        recyclerView.adapter = adapter

        val fabAdd = findViewById<FloatingActionButton>(R.id.fab_add)
        fabAdd.setOnClickListener {
            val dialog = TambahBalitaFragment()
            dialog.show(supportFragmentManager, "TambahBalita")
        }

        // Observe LiveData dari ViewModel
        viewModel.balitaList.observe(this) { list ->
            adapter.updateData(list)
        }

        // Mulai mendengarkan data dari Firestore
        viewModel.startListening()
    }
}