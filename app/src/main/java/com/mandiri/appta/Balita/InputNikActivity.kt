package com.mandiri.appta.Balita

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mandiri.appta.R

class InputNikActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_nik)

        val nikEditText = findViewById<EditText>(R.id.et_nik_balita)
        val lihatButton = findViewById<Button>(R.id.btn_lihat_perkembangan)

        lihatButton.setOnClickListener {
            val nik = nikEditText.text.toString().trim()
            if (nik.isEmpty()) {
                Toast.makeText(this, "Masukkan NIK Balita terlebih dahulu", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Cek ke Firestore apakah NIK ada
            val db = com.google.firebase.firestore.FirebaseFirestore.getInstance()
            db.collection("balita")
                .whereEqualTo("nik", nik)
                .limit(1)
                .get()
                .addOnSuccessListener { documents ->
                    if (!documents.isEmpty) {
                        // NIK ditemukan, lanjut ke BalitaActivity
                        val intent = Intent(this, BalitaActivity::class.java)
                        intent.putExtra("NIK_BALITA", nik)
                        startActivity(intent)
                    } else {
                        // NIK tidak ditemukan
                        Toast.makeText(this, "NIK tidak ditemukan di database", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Terjadi kesalahan saat mengecek NIK", Toast.LENGTH_SHORT).show()
                }
        }
    }
}