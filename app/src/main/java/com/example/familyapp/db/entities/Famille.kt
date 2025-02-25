package com.example.familyapp.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "familles")
data class FamilleEntity(
    @PrimaryKey(autoGenerate = true)
    val idFamille: Int = 0,
    val nom: String,
    val dateCreation: String,
    val codeInvitation: String
)