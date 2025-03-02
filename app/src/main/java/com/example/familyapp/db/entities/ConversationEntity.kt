package com.example.familyapp.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "conversations")
data class ConversationEntity(
@PrimaryKey val id: String,
val name: String,
val lastMessage: String,
val messageTime: String,
val profileImage: String
)
