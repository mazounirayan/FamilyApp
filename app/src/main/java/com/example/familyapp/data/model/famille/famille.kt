package com.example.familyapp.data.model.famille

import java.util.Date

data class famille(
    val idFamille :Int,
    val nom:String  ,
    val  date_de_creation: Date,
    val code_invitation:String
)
