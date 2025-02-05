package com.example.familyapp.network.dto.taskDto

import com.example.familyapp.data.model.task.Priorite
import com.example.familyapp.data.model.task.StatusTache
import com.example.familyapp.data.model.task.TypeTache
import com.google.gson.annotations.SerializedName
import java.time.LocalDate

data class TaskDto(
    val idTache: Int,
    val nom: String,
    @SerializedName("date_debut")
    val dateDebut : String,
    @SerializedName("date_fin")
    val dateFin: String,
    val status: String,
    val type: String,
    val description: String,
    val priorite: String,
    val idUser: Int,
    val idFamille: Int
)