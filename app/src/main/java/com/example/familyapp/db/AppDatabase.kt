package com.example.familyapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.familyapp.db.dao.UserDao
import com.example.familyapp.db.entities.UserEntity

@Database(entities = [UserEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}
