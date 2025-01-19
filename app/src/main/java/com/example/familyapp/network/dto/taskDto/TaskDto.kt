package com.example.familyapp.network.dto.taskDto

import com.example.familyapp.data.model.task.Priorite
import com.example.familyapp.data.model.task.StatusTache
import com.example.familyapp.data.model.task.TypeTache
import java.time.LocalDate

data class TaskDto(
    val idTache: Int,
    val nom: String,
    val dateDebut: String,
    val dateFin: String,
    val status: StatusTache,
    val type: TypeTache,
    val description: String,
    val priorite: Priorite,
    val idUser: Int,
    val idFamille: Int
)