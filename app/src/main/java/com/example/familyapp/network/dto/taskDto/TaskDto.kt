package com.example.familyapp.network.dto.taskDto


import com.example.familyapp.data.model.task.Priorite
import com.example.familyapp.data.model.user.User
import com.example.familyapp.network.dto.userDto.UserDTO
import com.google.gson.annotations.SerializedName

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
    val priorite:  Priorite?,
    val user: UserDTO,
    val idFamille: Int
)