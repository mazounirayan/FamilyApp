package com.example.familyapp.data.model.conversation

data class Conversation(
    val id: Int,
    val name: String,
    val lastMessage: String?,
    val messageTime: String?,
    val profileImage: String?
)
