package com.example.familyapp.network.mapper

import com.example.familyapp.data.model.famille.Famille
import com.example.familyapp.data.model.recompense.Recompense
import com.example.familyapp.network.dto.familleDto.FamilleDTO
import java.util.Date


    fun mapFamilleDtoToFamille(familleDto: FamilleDTO): Famille {
        return Famille(
            idFamille =familleDto.idFamille,
            nom=familleDto.nom,
            dateCreation =familleDto.date_de_creation,
            codeInvitation =familleDto.code_invitation,

            )
    }