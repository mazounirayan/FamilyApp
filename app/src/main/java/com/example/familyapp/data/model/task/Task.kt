package com.example.familyapp.data.model.task

import com.example.familyapp.data.model.user.User
import com.google.gson.annotations.SerializedName

enum class Priorite {
    Haute,
    Moyenne,
    Basse
}

enum class StatusTache {
    A_FAIRE,
    EN_COURS,
    FINI,
    SUSPENDU
}

enum class TypeTache {
    TACHE_MENAGERE,
    TACHES_FAMILIALES,
    TACHES_ADMINISTRATIVES
}


data class Task(
    val idTache: Int,
    var nom: String,
    val dateDebut: String,
    val dateFin: String,
    var status: String,
    val type: String,
    var description: String,
    val priorite: Priorite?,
    var user: User,
    val idFamille: Int
)

