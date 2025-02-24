package com.example.familyapp.data.model.chat

import com.example.familyapp.data.model.user.User

data class Chat(
    val idChat: Int,
    val libelle: String,
    val participants: List<User>
)
