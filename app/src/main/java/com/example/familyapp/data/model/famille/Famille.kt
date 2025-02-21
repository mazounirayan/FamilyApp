package com.example.familyapp.data.model.Famille

import java.util.Date

data class Famille(
    val idFamille: Int,
    val nom: String,
    val dateCreation: Date,
    val codeInvitation: String,
)