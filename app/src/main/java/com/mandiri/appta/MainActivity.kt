package com.mandiri.appta

import android.content.Intent
import android.os.Bundle

import androidx.activity.ComponentActivity

import androidx.activity.enableEdgeToEdge

import com.google.android.material.card.MaterialCardView

import com.mandiri.appta.Balita.InputNikActivity
import com.mandiri.appta.Petugas.LoginPetugasActivity
import com.mandiri.appta.Petugas.PetugasActivity

import com.mandiri.appta.util.SessionManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val sessionManager = SessionManager(this)
        val btnPetugas = findViewById<MaterialCardView>(R.id.btn_petugas)
        val btnOrangTua = findViewById<MaterialCardView>(R.id.btn_orang_tua)


        btnOrangTua.setOnClickListener {
            val intent = Intent(this, InputNikActivity::class.java)
            startActivity(intent)
        }
        btnPetugas.setOnClickListener {
            if (sessionManager.isPetugasLoggedIn()) {
                startActivity(Intent(this, PetugasActivity::class.java))
            } else {
                startActivity(Intent(this, LoginPetugasActivity::class.java))
            }
        }

    }
}
