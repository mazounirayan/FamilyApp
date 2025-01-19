package com.example.familyapp.data.model.task

import com.google.firebase.encoders.annotations.Encodable.Ignore
import com.google.gson.annotations.SerializedName
import java.time.LocalDate

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

    @SerializedName("date_debut")
    val dateDebut: String,

    @SerializedName("date_fin")
    val dateFin: String,

    val status: StatusTache,
    val type: TypeTache,
    val description: String,
    @Transient
    val priorite: Priorite,
    val idUser: Int,
    val idFamille: Int
)

