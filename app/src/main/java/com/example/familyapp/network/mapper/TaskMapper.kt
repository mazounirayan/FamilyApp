package com.example.familyapp.network.mapper

import com.example.familyapp.data.model.task.Task
import com.example.familyapp.network.dto.taskDto.TaskDto

fun mapTaskDtoToTask(taskDto: TaskDto): Task {
    return Task(
        idTache = taskDto.idTache,
        nom = taskDto.nom,
        dateDebut = taskDto.dateDebut,
        dateFin = taskDto.dateFin,
        status = taskDto.status,
        type = taskDto.type,
        description = taskDto.description,
        priorite = taskDto.priorite,
        user = mapUserDtoToUser(taskDto.user),
        idFamille = taskDto.idFamille,
    )
}