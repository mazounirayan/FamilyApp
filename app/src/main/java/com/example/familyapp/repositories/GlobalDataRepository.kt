package com.example.app.repositories

import com.example.familyapp.network.services.ApiService

import com.example.familyapp.network.dto.userDto.UserDTO

class UserRepository(private val apiService: ApiService) {

    suspend fun getUsers(): List<UserDTO> {
        return apiService.getUsers()
    }

    suspend fun registerUser(user: UserDTO): UserDTO {
        return apiService.registerUser(user)
    }

    suspend fun loginUser(user: UserDTO): UserDTO {
        return apiService.loginUser(user)
    }
}
