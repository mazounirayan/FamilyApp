package com.example.familyapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.familyapp.utils.SessionManager

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SplashActivity : AppCompatActivity() {
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        sessionManager = SessionManager(this)

        lifecycleScope.launch {
            delay(2000)
            val destinationActivity = if (sessionManager.isLoggedIn()) {
                MainActivity::class.java
            } else {
                AuthenticationActivity::class.java
            }
            startActivity(Intent(this@SplashActivity, destinationActivity))
            finish()
        }

    }
}