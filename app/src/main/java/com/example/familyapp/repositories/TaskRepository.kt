package com.example.familyapp.repositories

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.familyapp.MainApplication
import com.example.familyapp.data.model.task.Task
import com.example.familyapp.data.model.task.TaskUpdate
import com.example.familyapp.db.daos.TaskDao
import com.example.familyapp.db.entities.TaskEntity
import com.example.familyapp.network.RetrofitClient
import com.example.familyapp.network.dto.taskDto.TaskDto
import com.example.familyapp.network.mapper.mapTaskDtoToTask
import com.example.familyapp.network.services.TaskService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TaskRepository(context: Context,private val taskDao: TaskDao) {

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
                    CoroutineScope(Dispatchers.IO).launch {
                        taskDao.insertAll(tasks.map { mapTaskToEntity(it) })
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

    fun addTask(task:TaskDto){
        val call = taskService.addTask(task)

        call.enqueue(object : Callback<TaskDto> {
            override fun onResponse(
                call: Call<TaskDto>,
                response: Response<TaskDto>
            ) {

                if (response.isSuccessful) {
                    Log.d("TaskRepository",response.message())
                    return
                } else {
                    Log.e("TaskRepository", "Erreur HTTP : ${response.errorBody()?.contentType()}")
                    Log.e("TaskRepository", "Erreur HTTP : ${response.code()}")
                }
            }

            override fun onFailure(call: Call<TaskDto>, t: Throwable) {
                Log.e("TaskRepository", "Erreur réseau : ${t.message}")
            }
        })
    }

    fun patchTask(id:Int, task: TaskUpdate){
        val call = taskService.patchTask(id,task)

        call.enqueue(object : Callback<TaskDto> {
            override fun onResponse(
                call: Call<TaskDto>,
                response: Response<TaskDto>
            ) {

                if (response.isSuccessful) {
                    return
                } else {
                    Log.e("TaskRepository", "Erreur HTTP : ${response.errorBody()?.contentType()}")
                    Log.e("TaskRepository", "Erreur HTTP : ${response.code()}")
                }
            }

            override fun onFailure(call: Call<TaskDto>, t: Throwable) {
                Log.e("TaskRepository", "Erreur réseau : ${t.message}")
            }
        })
    }
    fun getAllTasks(idFamille: Int) {
        Log.d("TaskRepository", "🔹 Requête API envoyée pour getAllTasks de la famille ID : $idFamille")

        val call = taskService.getAllTasks(idFamille)
        call.enqueue(object : Callback<List<TaskDto>> {
            override fun onResponse(call: Call<List<TaskDto>>, response: Response<List<TaskDto>>) {
                if (response.isSuccessful) {
                    val response = response.body()
                    Log.d("TaskRepository", "🔹 Réponse API reçue, taille : ${response?.size}")

                    _tasks.value = response?.map { mapTaskDtoToTask(it) }
                    Log.d("TaskRepository", "🔹 Tâches après mapping : ${_tasks.value?.size}")
                } else {
                    Log.e("TaskRepository", "❌ Erreur HTTP : ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<TaskDto>>, t: Throwable) {
                Log.e("TaskRepository", "❌ Erreur réseau : ${t.message}")
            }
        })
    }

    private fun mapTaskToEntity(task: Task): TaskEntity {
        return TaskEntity(
            id =
            nom = task.nom,
            description = task.description,
            dateEcheance = task.dateEcheance,
            idUser = task.idUser,
            idFamille = task.idFamille,
            etat = task.etat,
            idTache = task.idTache,,
            nom = TODO(),
            dateDebut = TODO(),
            dateFin = TODO(),
            status = TODO(),
            type = TODO(),
            priorite = TODO()
        )
    }


}