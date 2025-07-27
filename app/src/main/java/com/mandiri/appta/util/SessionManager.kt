package com.mandiri.appta.util

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    fun setPetugasLoggedIn(isLoggedIn: Boolean) {
        prefs.edit().putBoolean("IS_PETUGAS_LOGGED_IN", isLoggedIn).apply()
    }

    fun isPetugasLoggedIn(): Boolean {
        return prefs.getBoolean("IS_PETUGAS_LOGGED_IN", false)
    }

    fun logoutPetugas() {
        prefs.edit().clear().apply()
    }

}