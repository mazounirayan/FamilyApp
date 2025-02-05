package com.example.familyapp.network.mapper



import com.example.familyapp.data.model.user.User

import com.example.familyapp.network.dto.userDto.UserDTO

fun mapUserDtoToUser(userDto: UserDTO): User {
    return User(
        id = userDto.id,
        nom = userDto.nom,
        prenom = userDto.prenom,
        email = userDto.email,
        role = userDto.role,
        numTel = userDto.numTel,
        idFamille = userDto.famille?.idFamille,
        dateInscription = userDto.dateInscription,
        motDePasse = userDto.motDePasse,
        avatar = userDto.avatar,
        coins= userDto.coins,
        totalPoints= userDto.totalPoints,
    )
}
