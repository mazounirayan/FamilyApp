package com.example.familyapp.data.model.user

import com.example.familyapp.data.model.chat.Chat

data class User(
    val id: Int,
    val nom: String,
    val prenom: String,
    val email: String,
    val motDePasse: String,
    val numTel: String,
    val role: String,
    val idFamille: Int,
    val dateInscription: String,
    val coins: Int,
    val avatar:String? ,
    val totalPoints : Int,
    val chats: List<Chat>?
) {
    override fun toString(): String {
        return prenom
    }
}
