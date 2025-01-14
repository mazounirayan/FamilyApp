package com.example.familyapp.data.model.recompense

data class Recompense(
    val idRecompense: Int,
    val nom: String,
    val description: String,
    val cout: Int,
    val stock: Int,
    val estDisponible: Boolean
)

