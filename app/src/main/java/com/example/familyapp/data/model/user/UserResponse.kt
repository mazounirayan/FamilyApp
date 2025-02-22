package com.example.familyapp.data.model.user

import com.example.familyapp.network.dto.userDto.UserDTO

data class UserResponse(
    val users: List<UserDTO>,
)