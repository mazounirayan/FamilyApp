package com.example.familyapp.network.services

import com.example.familyapp.data.model.task.TaskUpdate
import com.example.familyapp.network.dto.taskDto.TaskDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path


interface TaskService {

    @GET("taches/user/{id}")
    fun getTaskFromUser(@Path("id") id: Int): Call<List<TaskDto>>

    @GET("taches/famille/{id}")
    fun getAllTasks(@Path("id") id: Int): Call<List<TaskDto>>


    @POST("taches")
    fun addTask(@Body task: TaskDto): Call<TaskDto>

    @PATCH("taches/{id}")
    fun patchTask(@Path("id") id:Int, @Body task: TaskUpdate): Call<TaskDto>
}
