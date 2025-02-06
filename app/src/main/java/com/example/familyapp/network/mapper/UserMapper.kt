package com.example.familyapp.network.mapper

import com.example.familyapp.data.model.user.AddUserRequest
import com.example.familyapp.data.model.user.User
import com.example.familyapp.network.dto.userDto.UserDTO




    fun mapAddUserRequestToUserDto(request: AddUserRequest): UserDTO {
        return UserDTO(
            id = 0,
            nom = request.nom,
            prenom = request.prenom,
            email = request.email,
            motDePasse = request.motDePasse,
            numTel = "",
            role = request.role,
            idFamille = 0,
            dateInscription = "",
            avatar = "",
            coins = 0,
            totalPoints = 0
        )
    }
    fun mapUserToUserDto(user: User): UserDTO {
        return UserDTO(
            id = user.id,
            nom = user.nom,
            prenom = user.prenom,
            email = user.email,
            motDePasse = user.motDePasse,
            numTel = user.numTel,
            role = user.role,
            idFamille = user.idFamille,
            dateInscription = user.dateInscription,
            avatar = user.avatar,
            coins = user.coins,
            totalPoints = user.totalPoints
        )
    }
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
            avatar = userDto.avatar,
            coins= userDto.coins,
            totalPoints= userDto.totalPoints,
        )
    }



