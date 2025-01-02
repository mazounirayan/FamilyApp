package com.example.familyapp.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: Int,
    val nom: String,
    val prenom: String,
    val email: String,
    val profession: String,
    val numTel: String,
    val role: String,
    val idFamille: Int
)
