package com.example.familyapp.network.dto.messageDto

import com.google.gson.annotations.SerializedName

data class MessageDto (
    val contenu: String,
    @SerializedName("date_envoie")
    val dateEnvoie: String,
    val isVue: Boolean,
    val idUser: Int,
    val idChat: Int,
)
