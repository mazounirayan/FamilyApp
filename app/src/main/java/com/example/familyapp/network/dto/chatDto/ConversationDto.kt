package com.example.familyapp.network.dto.chatDto



data class ConversationDto(
    val id: Int,
    val name: String,
    val lastMessage: String,
    val messageTime: String,
    val profileImage: String?
)