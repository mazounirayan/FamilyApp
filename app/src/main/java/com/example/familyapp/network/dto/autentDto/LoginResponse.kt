package com.example.familyapp.network.dto.autentDto

import com.example.familyapp.network.dto.userDto.UserDTO

data class LoginResponse(
    val token:String,
    val user: UserDTO
)
