package com.example.familyapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.familyapp.utils.LocalStorage

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SplashActivity : AppCompatActivity() {
    private lateinit var localStorage: LocalStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        localStorage = LocalStorage(this)

        lifecycleScope.launch {
            delay(2000)
            val destinationActivity = if (localStorage.isLoggedIn()) {
                MainActivity::class.java
            } else {
                AuthenticationActivity::class.java
            }
            startActivity(Intent(this@SplashActivity, destinationActivity))
            finish()
        }

    }
}