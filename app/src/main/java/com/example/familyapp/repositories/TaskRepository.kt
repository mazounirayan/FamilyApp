package com.example.familyapp.repositories

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.familyapp.data.model.task.Task
import com.example.familyapp.network.RetrofitClient
import com.example.familyapp.network.dto.taskDto.TaskDto
import com.example.familyapp.network.mapper.mapTaskDtoToTask
import com.example.familyapp.network.services.TaskService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TaskRepository(context: Context) {

    private val taskService = RetrofitClient.instance.create(TaskService::class.java)

    private val _tasks = MutableLiveData<List<Task>>()

    val tasks: LiveData<List<Task>> get() = _tasks

    fun getTaskFromUser(idUser: Int) {
        val call = taskService.getTaskFromUser(idUser)

        call.enqueue(object : Callback<List<TaskDto>> {
            override fun onResponse(
                call: Call<List<TaskDto>>,
                response: Response<List<TaskDto>>
            ) {

                if (response.isSuccessful) {
                    val response = response.body()
                    _tasks.value = response?.let {
                        it.map{ value ->
                            mapTaskDtoToTask(value)
                        }
                    }
                } else {
                    Log.e("TaskRepository", "Erreur HTTP : ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<TaskDto>>, t: Throwable) {
                Log.e("TaskRepository", "Erreur réseau : ${t.message}")
            }
        })
    }
}