package com.example.familyapp.network.services

import com.example.familyapp.network.dto.rewardsDto.rewardsDto
import com.example.familyapp.network.dto.taskDto.TaskDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface RewardService {
    @GET("familles/{id}/recompenses")
    fun getRecompense(@Path("id") id:Int): Call<List<rewardsDto>>
}