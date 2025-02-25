package com.example.familyapp

import android.app.Application
import androidx.room.Room
import com.example.familyapp.db.AppDatabase

class MainApplication : Application() {

    companion object {
        lateinit var database: AppDatabase
            private set
    }

    override fun onCreate() {
        super.onCreate()

        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "family_app_db"
        ).build()
    }
}
