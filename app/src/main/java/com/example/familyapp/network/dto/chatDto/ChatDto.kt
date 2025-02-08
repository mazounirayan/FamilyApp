package com.example.familyapp.network.dto.chatDto

data class ChatDto(
    @Transient
    val idChat: Int,
    val libelle: String
)
