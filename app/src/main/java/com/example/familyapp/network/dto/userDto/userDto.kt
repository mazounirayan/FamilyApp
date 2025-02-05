package com.example.familyapp.network.dto.userDto

import com.example.familyapp.network.dto.familleDto.FamilleDTO

data class UserDTO(
    val id: Int,
    val nom: String,
    val prenom: String,
    val email: String,
    val motDePasse: String,
    val role: String,
    val dateInscription: String,
    val avatar: String?,
    val coins: Int,
    val totalPoints: Int,
    val famille: FamilleDTO?,
    val numTel: String
)
