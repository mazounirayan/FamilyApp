package com.example.familyapp.utils


import android.content.Context

class SessionManager(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)

    fun saveLoginState(email: String) {
        sharedPreferences.edit().apply {
            putBoolean("isLoggedIn", true)
            putString("email", email)
            apply()
        }
    }

    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean("isLoggedIn", false)
    }

    fun logout() {
        sharedPreferences.edit().clear().apply()
    }
