package com.example.familyapp.data.model.user

data class UpdateUserRequest(
    val nom: String,
    val prenom: String,
    val email: String,

    val role: String
)
