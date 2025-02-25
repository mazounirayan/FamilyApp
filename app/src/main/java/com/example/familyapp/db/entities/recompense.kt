package com.example.familyapp.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recompenses")
data class RecompenseEntity(
    @PrimaryKey(autoGenerate = true)
    val idRecompense: Int = 0,
    val nom: String,
    val description: String,
    val cout: Int,
    val stock: Int,
    val estDisponible: Boolean,
    val idFamille: Int
)
