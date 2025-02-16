package com.example.familyapp.network.dto.userDto

data class AddUserRequestDTO(
    val nom: String,
    val prenom: String,
    val email: String,
    val motDePasse: String,
    val role: String
)
