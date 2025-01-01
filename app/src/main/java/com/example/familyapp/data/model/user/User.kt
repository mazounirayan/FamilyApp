package com.example.familyapp.data.model.user

data class User(
    val id: Int,
    val nom: String,
    val prenom: String,
    val email: String,
    val motDePasse: String,
    val profession: String,
    val numTel: String,
    val role: String,
    val idFamille: Int
)
