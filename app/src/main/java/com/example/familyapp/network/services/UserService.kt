package com.example.familyapp.network.services

 import com.example.familyapp.data.model.user.UpdateUserRequest
 import com.example.familyapp.network.dto.autentDto.FamilyInfo
import com.example.familyapp.network.dto.autentDto.LoginRequest
import com.example.familyapp.network.dto.autentDto.LoginResponse
import com.example.familyapp.network.dto.userDto.UserDTO
 import com.example.familyapp.network.dto.userDto.AddUserRequestDTO

 import retrofit2.Call
 import retrofit2.http.Body
 import retrofit2.http.DELETE
 import retrofit2.http.GET
 import retrofit2.http.PATCH
 import retrofit2.http.POST
import retrofit2.http.Path

interface UserService {
    @GET("users")
     fun getAllUsers(): Call<List<UserDTO>>
package com.example.familyapp.network

import com.example.familyapp.data.model.user.LogoutResponse
import com.example.familyapp.data.model.user.User
import com.example.familyapp.network.dto.autentDto.LoginRequest
import com.example.familyapp.network.dto.autentDto.LoginResponse
import com.example.familyapp.network.dto.autentDto.SignUpRequest
import com.example.familyapp.network.dto.autentDto.UsersbytokenRequest
import com.example.familyapp.network.dto.userDto.UserDTO
import org.json.XML
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

    @PATCH("users/{id}")
    fun updateUser(@Path("id") userId: Int, @Body updateUserRequest: UpdateUserRequest): Call<Void>


    @POST("users/{idUser}/famille")
    fun addUserToFamille(@Path("idUser") idUser: Int, @Body familyInfo: FamilyInfo): Call<Void>
}

    @POST("auth/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @POST("/auth/signup")
    fun signUp(@Body request: SignUpRequest): Call<UserDTO>

    @GET("users/{userId}")
    suspend fun getUserDetails(@Path("userId") userId: Int): User

    @DELETE("/auth/logout/{id}")
    fun logout(@Path("id") id:Int) : Call<LogoutResponse>

    @GET("/usersbytoken/{token}")
    fun getUserByToken(@Path("token") token: String): Call<UserDTO>
}


