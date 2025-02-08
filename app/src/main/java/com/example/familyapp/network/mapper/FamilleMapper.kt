package com.example.familyapp.network.mapper

import com.example.familyapp.data.model.famille.Famille
import com.example.familyapp.data.model.recompense.Recompense
import com.example.familyapp.network.dto.familleDto.FamilleDto
import java.util.Date


    fun mapFamilleDtoToFamille(familleDto: FamilleDto): Famille {
        return Famille(
            idFamille =familleDto.idFamille,
            nom=familleDto.nom,
            date_de_creation =familleDto.date_de_creation,
            code_invitation =familleDto.code_invitation,


            )
    }