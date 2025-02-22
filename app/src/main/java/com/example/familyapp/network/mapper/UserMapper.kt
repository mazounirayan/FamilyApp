package com.example.familyapp.network.mapper

import com.example.familyapp.data.model.user.AddUserRequest
import com.example.familyapp.data.model.user.User
import com.example.familyapp.network.dto.userDto.UserDTO
import com.example.familyapp.network.dto.userDto.AddUserRequestDTO





fun mapAddUserRequestToUserDto(request: AddUserRequest): AddUserRequestDTO {
    return AddUserRequestDTO(
        nom = request.nom,
        prenom = request.prenom,
        email = request.email,
        motDePasse = request.motDePasse,
        role = request.role
    )
}


  /*  fun mapUserToUserDto(user: User): UserDTO {
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
            totalPoints = user.totalPoints,
        )
    }*/
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
                coins = userDto.coins,
                totalPoints = userDto.totalPoints,
                chats = userDto.chats,
             //   idFamille = TODO()
            )
    }



