package com.mandiri.appta.Balita


import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView

import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.mandiri.appta.R

class BalitaActivity : AppCompatActivity() {
    companion object {
        var nikBalita: String? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_balita)
        nikBalita = intent.getStringExtra("NIK_BALITA")

        val nikTextView = findViewById<TextView>(R.id.nikTextView)
        val namaTextView = findViewById<TextView>(R.id.namaTextView)

        nikTextView.text = "NIK: $nikBalita"

        // Ambil nama balita dari Firestore
        nikBalita?.let { nik ->
            val db = Firebase.firestore
            db.collection("balita")
                .whereEqualTo("nik", nik)
                .limit(1)
                .get()
                .addOnSuccessListener { docs ->
                    if (!docs.isEmpty) {
                        val nama = docs.documents[0].getString("nama") ?: "-"
                        namaTextView.text = "Nama: $nama"
                    } else {
                        namaTextView.text = "Nama: (tidak ditemukan)"
                    }
                }
                .addOnFailureListener {
                    namaTextView.text = "Nama: (gagal mengambil)"
                }
        }


        nikBalita = intent.getStringExtra("NIK_BALITA")

        supportFragmentManager.beginTransaction()
            .replace(R.id.container_Grafik, GrafikPertumbuhanFragment())
            .commit()

        supportFragmentManager.beginTransaction()
            .replace(R.id.container_pemeriksaan, PemeriksaanPerBulanFragment())
            .commit()

        supportFragmentManager.beginTransaction()
            .replace(R.id.container_imunisasi, ImunisasiPelayananFragment())
            .commit()
    }
}
