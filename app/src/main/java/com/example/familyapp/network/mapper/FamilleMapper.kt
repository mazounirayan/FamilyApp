package com.example.familyapp.network.mapper

import com.example.familyapp.data.model.Famille.Famille
import com.example.familyapp.network.dto.familleDto.FamilleDto


fun mapFamilleDtoToFamille(familleDto: FamilleDto): Famille {
        return Famille(
            idFamille =familleDto.idFamille,
            nom =familleDto.nom,
            dateCreation = familleDto.date_de_creation.toString(),
            codeInvitation =familleDto.code_invitation,

            )
    }