package com.example.familyapp.data.model.message

data class Message(
    val idMessage: String,
    val contenu: String,
    val dateEnvoie: String,
    val isVue: Boolean,
    val idUser: Int,
    val idChat: Int,
)