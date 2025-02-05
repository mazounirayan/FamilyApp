package com.example.familyapp.network.mapper

import com.example.familyapp.data.model.recompense.Recompense
import com.example.familyapp.data.model.task.Task
import com.example.familyapp.network.dto.rewardsDto.rewardsDto



fun mapRewardDtoToReward(rewardDto: rewardsDto): Recompense {
    return Recompense(
     idRecompense = rewardDto.idRecompense!! ,
     nom=rewardDto.nom,
     description =rewardDto.description,
     cout =rewardDto.cout,
     stock =rewardDto.stock,
     estDisponible =rewardDto.estDisponible,

    )
}