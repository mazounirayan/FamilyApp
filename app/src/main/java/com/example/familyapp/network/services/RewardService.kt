package com.example.familyapp.network.services

import com.example.familyapp.network.dto.rewardsDto.rewardsDto
import com.example.familyapp.network.dto.taskDto.TaskDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface RewardService {
    @GET("familles/{id}/recompenses")
    fun getRecompense(@Path("id") id:Int): Call<List<rewardsDto>>

    @POST("familles/{idFamille}/recompenses")
    fun addRecompense(
        @Path("idFamille") idFamille: Int,
        @Body newReward: rewardsDto
    ): Call<rewardsDto>

    // Modifier une récompense existante
    @PATCH("recompenses/{id}")
    fun updateRecompense(
        @Path("id") id: Int,
        @Body updatedReward: rewardsDto
    ): Call<rewardsDto>

    // Supprimer une récompense
    @DELETE("recompenses/{id}")
    fun deleteRecompense(@Path("id") id: Int): Call<Void>

    // Acheter une récompense
    @POST("recompenses/{id}/buy")
    fun buyRecompense(
        @Path("id") id: Int,
        @Body requestBody: Map<String, Int> // Par exemple, { "idUser": 123 }
    ): Call<rewardsDto>


}