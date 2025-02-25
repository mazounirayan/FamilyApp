package com.example.familyapp.db.entities


import androidx.room.Entity


@Entity(primaryKeys = ["userId", "chatId"])
data class UserChatCrossRef(
    val userId: Int,
    val chatId: Int
)