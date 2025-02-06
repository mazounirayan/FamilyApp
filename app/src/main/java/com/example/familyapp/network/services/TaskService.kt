package com.example.familyapp.network.services

import com.example.familyapp.network.dto.taskDto.TaskDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface TaskService {
    @GET("taches/user/{id}")
    fun getTaskFromUser(@Path("id") id: Int): Call<List<TaskDto>>

    @GET("taches/famille/{id}")
    fun getAllTasks(@Path("id") id: Int): Call<List<TaskDto>>
}
