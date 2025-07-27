package com.mandiri.appta.Petugas.Pemeriksaan

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.card.MaterialCardView
import com.mandiri.appta.Petugas.Pemeriksaan.lihat.LihatPemeriksaanActivity
import com.mandiri.appta.R

class PemeriksaanActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pemeriksaan)

        val btnTambah=findViewById<MaterialCardView>(R.id.btn_tambah_pemeriksaan)
        val btn_lihat=findViewById<MaterialCardView>(R.id.btn_lihat_pemeriksaan)
        btnTambah.setOnClickListener {
            val intent = Intent(this, MelakukanPemeriksaanActivity::class.java)
            startActivity(intent)
        }
        btn_lihat.setOnClickListener {
            val intent = Intent(this, LihatPemeriksaanActivity::class.java)
            startActivity(intent)
        }
    }

}