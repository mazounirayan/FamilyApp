package com.example.familyapp.network.dto.autentDto

import com.example.familyapp.network.dto.userDto.UserDTO


data class SignUpRequest(
    val nom: String,
    val prenom: String,
    val email: String,
    val motDePasse: String,
    val role: String,
    val codeFamille: String? = null,
    val nomFamille: String? = null
)

data class SignUpResponse(
    val user: UserDTO
)