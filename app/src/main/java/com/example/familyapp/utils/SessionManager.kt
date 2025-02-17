package com.example.familyapp.utils

import android.content.Context

class SessionManager(context: Context) {


    companion object {
        private const val PREFS_NAME = "LoginPrefs"
        private const val KEY_IS_LOGGED_IN = "isLoggedIn"
        private const val KEY_EMAIL = "email"
    }

    private val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)


    fun saveLoginState(email: String) {
        if (email.isBlank()) {
            throw IllegalArgumentException("L'e-mail ne peut pas être vide.")
        }

        sharedPreferences.edit().apply {
            putBoolean(KEY_IS_LOGGED_IN, true)
            putString(KEY_EMAIL, email)
            apply()
        }
    }


    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
    }


    fun getUserEmail(): String? {
        return sharedPreferences.getString(KEY_EMAIL, null)
    }


    fun logout() {
        sharedPreferences.edit().clear().apply()
    }
}