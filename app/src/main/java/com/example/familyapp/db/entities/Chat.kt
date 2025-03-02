package com.example.familyapp.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chats")
data class ChatEntity(
    @PrimaryKey(autoGenerate = true) val localId: Long = 0,
    val idUser: Int,
    val idChat: Int? = null
)