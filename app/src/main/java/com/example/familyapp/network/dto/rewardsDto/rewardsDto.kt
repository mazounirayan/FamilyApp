package com.example.familyapp.network.dto.rewardsDto

import com.example.familyapp.data.model.task.Priorite
import java.time.LocalDate

data class rewardsDto(
    val idRecompense: Int,
    val nom: String,
    val description: String,
    val cout: Int,
    val stock: Int,
    val estDisponible: Boolean
)


