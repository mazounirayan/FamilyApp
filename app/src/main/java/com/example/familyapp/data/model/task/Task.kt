package com.example.familyapp.data.model.task

import java.time.LocalDate

enum class Priorite {
    Haute,
    Moyenne,
    Basse
}

data class Task(
    val idTache: Int,
    val nom: String,
    val dateDebut: LocalDate?,
    val dateFin: LocalDate?,
    val status: String?,
    val type: String?,
    val description: String?,
    val priorite: Priorite?, //  "Haute", "Moyenne", ou "Basse"
    val idCategorie: Int?,
    val idUser: Int?,
    val idFamille: Int?
)

