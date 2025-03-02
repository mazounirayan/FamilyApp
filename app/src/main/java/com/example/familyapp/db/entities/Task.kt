package com.example.familyapp.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val idTache: Int = 0,
    val nom: String,
    val dateDebut: String,
    val dateFin: String,
    var status: String,
    val type: String,
    val description: String,
    val priorite: String,
    val idUser: Int,
    val idFamille: Int
)