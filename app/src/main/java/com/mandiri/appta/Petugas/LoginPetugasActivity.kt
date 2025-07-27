package com.mandiri.appta.Petugas

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.mandiri.appta.Custom_btn.BtnLogin
import com.mandiri.appta.R
import com.mandiri.appta.util.SessionManager

class LoginPetugasActivity : AppCompatActivity() {
    private lateinit var btnLogin: BtnLogin
    private lateinit var db: FirebaseFirestore
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_petugas)

        val edtUsername = findViewById<EditText>(R.id.edt_username)
        val edtPassword = findViewById<EditText>(R.id.edt_password)
        btnLogin = findViewById(R.id.btn_login)
        sessionManager = SessionManager(this)

        db = FirebaseFirestore.getInstance()

        btnLogin.setOnClickListener {
            val username = edtUsername.text.toString().trim()
            val password = edtPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                edtUsername.error = "Username tidak boleh kosong"
                edtPassword.error = "Password tidak boleh kosong"
                return@setOnClickListener
            }

            db.collection("petugas")
                .whereEqualTo("username", username)
                .whereEqualTo("password", password)
                .get()
                .addOnSuccessListener { documents ->
                    if (!documents.isEmpty) {
                        sessionManager.setPetugasLoggedIn(true)
                        startActivity(Intent(this, PetugasActivity::class.java))
                        finish()
                    } else {
                        // Login gagal
                        edtUsername.error = "Username salah"
                        edtPassword.error = "password salah"
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Terjadi kesalahan: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

//    override fun onDestroy() {
//        super.onDestroy()
//        sessionManager.logoutPetugas()
//    }
}