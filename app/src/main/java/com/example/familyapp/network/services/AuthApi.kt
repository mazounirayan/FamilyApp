package com.example.familyapp.network

import com.example.familyapp.network.dto.autentDto.LoginRequest
import com.example.familyapp.network.dto.autentDto.LoginResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("auth/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>
}
