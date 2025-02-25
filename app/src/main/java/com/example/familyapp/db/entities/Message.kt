package com.example.familyapp.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey
    val idMessage: String,
    val contenu: String,
    val dateEnvoie: String,
    val isVue: Boolean,
    val userId: Int, // Clé étrangère
    val idChat: Int  // Clé étrangère
)