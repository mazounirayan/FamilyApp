package com.example.familyapp.network.dto.taskDto

import com.example.familyapp.data.model.task.Priorite
import java.time.LocalDate

data class TaskDto(
    val idTache: Int,
    val nom: String,
    val dateDebut: LocalDate?,
    val dateFin: LocalDate?,
    val status: String?,
    val type: String?,
    val description: String?,
    val priorite: Priorite?,
    val idCategorie: Int?,
    val idUser: Int?,
    val idFamille: Int?
)