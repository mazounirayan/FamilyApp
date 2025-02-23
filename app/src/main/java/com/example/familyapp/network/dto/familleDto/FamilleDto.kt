package com.example.familyapp.network.dto.familleDto

import java.util.Date

data class FamilleDto(

    val idFamille :Int,
    val nom:String,
    val  date_de_creation: Date,
    val code_invitation:String
)
