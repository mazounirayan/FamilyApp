package com.example.familyapp.network.dto.userDto

import com.example.familyapp.data.model.chat.Chat
import com.example.familyapp.network.dto.familleDto.FamilleDto

data class UserDTO(
    val id: Int,
    val nom: String,
    val prenom: String,
    val email: String,
    val motDePasse: String,
    val role: String,
    val dateInscription: String,
    val avatar:String? ,
    val coins: Int,
    val totalPoints: Int,
    val famille: FamilleDto?,
    val numTel: String,
    val chats: List<Chat>
)
