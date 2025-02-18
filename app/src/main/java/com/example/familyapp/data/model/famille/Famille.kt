package com.example.familyapp.data.model.Famille

import java.time.LocalDate

data class Famille(
    val idFamille: Int,
    val nom: String,
    val dateCreation: LocalDate,
    val codeInvitation: String,
)