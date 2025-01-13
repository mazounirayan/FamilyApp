package com.example.familyapp.network.mapper



import com.example.familyapp.data.model.user.User

import com.example.familyapp.network.dto.autentDto.LoginResponse
import com.example.familyapp.network.dto.userDto.UserDTO

fun mapUserDtoToUser(userDto: UserDTO): User {
    return User(
        id = userDto.id,
        nom = userDto.nom,
        prenom = userDto.prenom,
        email = userDto.email,
        role = userDto.role,
        numTel = userDto.numTel,
        idFamille = userDto.idFamille,
        dateInscription = userDto.dateInscription,
        motDePasse = userDto.motDePasse,
        profession = userDto.profession,
        score = 3
    )
}
