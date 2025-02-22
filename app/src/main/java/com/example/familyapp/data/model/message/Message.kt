package com.example.familyapp.data.model.message

import com.example.familyapp.data.model.user.UserMessage

data class Message(
    val idMessage: String,
    val contenu: String,
    val dateEnvoie: String,
    val isVue: Boolean,
    val user: UserMessage,
    val idChat: Int,
)