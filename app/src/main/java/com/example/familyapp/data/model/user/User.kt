package com.example.familyapp.data.model.user

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
    val totalPoints : Int  ,
)
