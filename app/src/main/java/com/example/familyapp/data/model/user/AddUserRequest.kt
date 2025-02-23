package com.example.familyapp.data.model.user
data class AddUserRequest(
    val nom: String,
    val prenom: String,
    val email: String,
    val motDePasse: String,
    val role: String
)
