package com.mandiri.appta.Petugas

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.card.MaterialCardView
import com.mandiri.appta.Custom_btn.BtnLogin
import com.mandiri.appta.Petugas.Balita.PetugasBalitaActivity
import com.mandiri.appta.Petugas.OrangTua.PetugasOrangtuaActivity
import com.mandiri.appta.Petugas.Pemeriksaan.PemeriksaanActivity
import com.mandiri.appta.R


class PetugasActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_petugas)
        val btnOrangtua=findViewById<MaterialCardView>(R.id.btn_tambah_orangtua)
        val btnTambahbalita=findViewById<MaterialCardView>(R.id.btn_tambah_balita)
        val btn_pemeriksaan=findViewById<MaterialCardView>(R.id.btn_pemeriksaan)
        val btn_laporan=findViewById<MaterialCardView>(R.id.btn_laporan)

        btnOrangtua.setOnClickListener {
            val intent = Intent(this, PetugasOrangtuaActivity::class.java)
            startActivity(intent)
        }

        btnTambahbalita.setOnClickListener {
            val intent = Intent(this, PetugasBalitaActivity::class.java)
            startActivity(intent)
        }
        btn_pemeriksaan.setOnClickListener {
            val intent = Intent(this, PemeriksaanActivity::class.java)
            startActivity(intent)
        }
        btn_laporan.setOnClickListener {
            val intent = Intent(this, com.mandiri.appta.Petugas.Laporan.LaporanActivity::class.java)
            startActivity(intent)
        }

    }
}