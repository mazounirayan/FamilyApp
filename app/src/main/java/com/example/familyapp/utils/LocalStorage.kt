package com.example.familyapp.utils

import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi

class LocalStorage(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)

    @OptIn(UnstableApi::class)
    fun saveLoginState(token: String) {
        sharedPreferences.edit().apply {
            putString("token", token)
            putBoolean("isLoggedIn", true)
            apply()
        }
        Log.d("LocalStorage", "Token sauvegardé : $token")
    }

    @OptIn(UnstableApi::class)
    fun isLoggedIn(): Boolean {
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
        Log.d("LocalStorage", "isLoggedIn : $isLoggedIn")
        return isLoggedIn
    }

    fun getToken(): String? {
        return sharedPreferences.getString("token", null)
    }

    fun logout() {
        sharedPreferences.edit().clear().apply()
    }
}