package com.example.familyapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.familyapp.db.dao.UserDao
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var userDao: UserDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        val db = MainApplication.database
        userDao = db.userDao()

        // Utilisation exemple
        lifecycleScope.launch {
            val users = userDao.getUserById(1)
              Log.d("RoomDB", "Utilisateurs: $users")
        }

        NavigationBar.setupNavigationClicks(this)

    }
}