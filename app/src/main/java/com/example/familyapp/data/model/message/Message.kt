package com.example.familyapp.data.model.message

data class Message(
    val id: String,
    val contenu: String,
    val date_envoie: String,
    val isVue: Boolean,
    val idUser: Int,
    val idChat: Int,

)