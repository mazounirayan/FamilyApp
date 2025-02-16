package com.example.familyapp.network.services

 import com.example.familyapp.network.dto.autentDto.FamilyInfo
import com.example.familyapp.network.dto.autentDto.LoginRequest
import com.example.familyapp.network.dto.autentDto.LoginResponse
import com.example.familyapp.network.dto.userDto.UserDTO
 import com.example.familyapp.network.dto.userDto.AddUserRequestDTO

 import retrofit2.Call
 import retrofit2.http.Body
 import retrofit2.http.DELETE
 import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UserService {
    @GET("users")
     fun getAllUsers(): Call<List<UserDTO>>

    @GET("users/family/{id}")
    fun getMembers(@Path("id") id:Int): Call<List<UserDTO>>


    @POST("auth/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @POST("users")
    fun createUser(@Body user: AddUserRequestDTO): Call<UserDTO>

    @DELETE("users/{idUser}")
    fun deleteUser(@Path("idUser") idUser: Int): Call<Void>


    @POST("users/{idUser}/famille")
    fun addUserToFamille(@Path("idUser") idUser: Int, @Body familyInfo: FamilyInfo): Call<Void>
}

