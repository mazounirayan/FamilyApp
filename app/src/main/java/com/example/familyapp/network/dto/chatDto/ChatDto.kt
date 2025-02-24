package com.example.familyapp.network.dto.chatDto

import com.example.familyapp.data.model.user.User

data class ChatDto(
    val idChat: Int,
    val libelle: String,
    val participants: List<User>


)
