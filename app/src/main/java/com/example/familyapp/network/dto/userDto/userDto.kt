package com.example.familyapp.network.dto.userDto

data class UserDTO(
    val id: Int,
    val nom: String,
    val prenom: String,
    val email: String,
    val motDePasse: String,
    val profession: String,
    val numTel: String,
    val role: String,
    val idFamille: Int,
    val dateInscription: String,
    val tokens: List<TokenDTO>

)

data class TokenDTO(
    val id: Int,
    val token: String
)