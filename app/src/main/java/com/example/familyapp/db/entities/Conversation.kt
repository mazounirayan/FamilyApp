package com.example.familyapp.db.entities

data class Conversation(
    val id: String,
    val name: String,
    val lastMessage: String,
    val messageTime: String,
    val profileImage: String
)
