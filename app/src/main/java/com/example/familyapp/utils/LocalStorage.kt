package com.example.familyapp.utils

import android.content.Context

class LocalStorage(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)

    fun saveLoginState(token: String) {
        sharedPreferences.edit().apply {
            putString("token", token)
            apply()
        }
    }

    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean("isLoggedIn", false)
    }

    fun logout() {
        sharedPreferences.edit().clear().apply()
    }
}