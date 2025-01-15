package com.example.familyapp.network

import com.example.familyapp.network.dto.autentDto.LoginRequest
import com.example.familyapp.network.dto.autentDto.LoginResponse
import com.example.familyapp.network.dto.userDto.UserDTO
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


interface UserService {
    @GET
    fun getAllUsers(): Call<List<UserDTO>>

    @POST("auth/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>
}
