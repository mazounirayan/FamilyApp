package com.example.familyapp.network.services




import com.example.familyapp.data.model.user.User
import retrofit2.http.*

interface ApiService {
    @GET("users")
    suspend fun getUsers(): List<User>

    @POST("users")
    suspend fun createUser(@Body user: User): User

    @POST("login")
    suspend fun login(@Body credentials: LoginRequest): LoginResponse
}