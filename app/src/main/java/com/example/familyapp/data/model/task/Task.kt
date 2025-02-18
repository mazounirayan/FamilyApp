package com.example.familyapp.data.model.task

import com.google.gson.annotations.SerializedName

enum class Priorite {
    HAUTE,
    MOYENNE,
    BASSE
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
    @Transient
    val idTache: Int,
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

