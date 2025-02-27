package com.example.familyapp.repositories

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.familyapp.MainApplication
import com.example.familyapp.app_utils.NetworkUtils
import com.example.familyapp.data.model.task.Priorite
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
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TaskRepository(context: Context) {

    private val taskService = RetrofitClient.instance.create(TaskService::class.java)

    private val _tasks = MutableLiveData<List<Task>>()
    private val context = context
    private val scope = CoroutineScope(SupervisorJob())
    private val db = MainApplication.database
    private val taskDao = db.taskDao()
    val tasks: LiveData<List<Task>> get() = _tasks
    fun getTaskFromUser(idUser: Int) {
        val call = taskService.getTaskFromUser(idUser)
        if (NetworkUtils.isOnline(context)) {
        call.enqueue(object : Callback<List<TaskDto>> {
            override fun onResponse(
                call: Call<List<TaskDto>>,
                response: Response<List<TaskDto>>
            ) {

                if (response.isSuccessful) {
                    val response = response.body()
                    response?.let {
                        // Convertir les DTO en modèles
                        val tasks = it.map { value ->
                            mapTaskDtoToTask(value)
                        }
                        _tasks.value = tasks


                        scope.launch {
                            taskDao.insertAll(tasks.map { mapTaskToEntity(it) })
                        }
                    }
                } else {
                    Log.e("TaskRepository", "Erreur HTTP : ${response.code()}")
                    loadTasksFromLocalDb(idUser)
                }
            }

            override fun onFailure(call: Call<List<TaskDto>>, t: Throwable) {
                Log.e("TaskRepository", "Erreur réseau : ${t.message}")
                loadTasksFromLocalDb(idUser)
            }
        })}else {
            loadTasksFromLocalDb(idUser)
        }
    }
    private fun loadTasksFromLocalDb(idUser: Int) {
        // Récupérer les tâches locales depuis la base de données
        val localTasks = taskDao.getTasksByUser(idUser)
        localTasks.observeForever {
            // Convertir les entités en modèles
            val tasks = it.map { entity ->
                Task(
                    idTache = entity.idTache,
                    nom = entity.nom,
                    description = entity.description,
                    idUser = entity.idUser,
                    idFamille = entity.idFamille,
                    dateDebut = entity.dateDebut,
                    dateFin = entity.dateFin,
                    status = entity.status,
                    type = entity.type,
                    priorite = try {
                        entity.priorite?.let { prioString ->
                            Priorite.valueOf(prioString)
                        }
                    } catch (e: IllegalArgumentException) {
                        null
                    }
                )
            }
            // Mettre à jour les données avec les tâches locales
            _tasks.postValue(tasks)
        }
    }
    fun addTask(task: TaskDto) {
        if (NetworkUtils.isOnline(context)) {
            // L'appareil est en ligne, envoyer la tâche au serveur
            val call = taskService.addTask(task)

            call.enqueue(object : Callback<TaskDto> {
                override fun onResponse(call: Call<TaskDto>, response: Response<TaskDto>) {
                    if (response.isSuccessful) {
                        Log.d("TaskRepository", "Tâche ajoutée avec succès")
                        // Ajouter également à la base de données locale
                        response.body()?.let { taskDto ->
                            val newTask = mapTaskDtoToTask(taskDto)
                            CoroutineScope(Dispatchers.IO).launch {
                                taskDao.insertAll(listOf(mapTaskToEntity(newTask)))
                            }
                        }
                    } else {
                        Log.e("TaskRepository", "Erreur HTTP : ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<TaskDto>, t: Throwable) {
                    Log.e("TaskRepository", "Erreur réseau : ${t.message}")
                }
            })
        } else {
            // L'appareil est hors ligne, stocker la tâche en local
            Log.e("TaskRepository", "L'appareil est hors ligne, stockage de la tâche en local")

            // Convertir TaskDto en Task puis en TaskEntity
            val newTask = mapTaskDtoToTask(task)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    taskDao.insertAll(listOf(mapTaskToEntity(newTask)))
                    Log.d("TaskRepository", "Tâche stockée en local")
                } catch (e: Exception) {
                    Log.e("TaskRepository", "Erreur lors de l'ajout local : ${e.message}")
                }
            }
        }
    }



    fun patchTask(id: Int, task: TaskUpdate) {
        if (NetworkUtils.isOnline(context)) {
            // L'appareil est en ligne, mettre à jour la tâche
            val call = taskService.patchTask(id, task)

            call.enqueue(object : Callback<TaskDto> {
                override fun onResponse(call: Call<TaskDto>, response: Response<TaskDto>) {
                    if (response.isSuccessful) {
                        Log.d("TaskRepository", "Tâche mise à jour avec succès")
                        // Mettre à jour également la version locale
                        response.body()?.let { taskDto ->
                            val updatedTask = mapTaskDtoToTask(taskDto)
                            CoroutineScope(Dispatchers.IO).launch {
                                taskDao.updateTask(mapTaskToEntity(updatedTask))
                            }
                        }
                    } else {
                        Log.e("TaskRepository", "Erreur HTTP : ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<TaskDto>, t: Throwable) {
                    Log.e("TaskRepository", "Erreur réseau : ${t.message}")
                }
            })
        } else {
            Log.e("TaskRepository", "L'appareil est hors ligne, stockage des modifications en local")

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val existingTask = taskDao.getTaskById(id)
                    if (existingTask != null) {
                        val updatedEntity = existingTask.copy(
                            status = task.status ?: existingTask.status,
                        )
                        taskDao.updateTask(updatedEntity)
                        Log.d("TaskRepository", "Modifications stockées en local")
                    }
                } catch (e: Exception) {
                    Log.e("TaskRepository", "Erreur lors de la mise à jour locale : ${e.message}")
                }
            }
        }
    }
    fun getAllTasks(idFamille: Int) {
        Log.d("TaskRepository", "🔹 Requête API envoyée pour getAllTasks de la famille ID : $idFamille")

        if (NetworkUtils.isOnline(context)) {
            val call = taskService.getAllTasks(idFamille)
            call.enqueue(object : Callback<List<TaskDto>> {
                override fun onResponse(call: Call<List<TaskDto>>, response: Response<List<TaskDto>>) {
                    if (response.isSuccessful) {
                        val responseTasks = response.body()
                        Log.d("TaskRepository", "🔹 Réponse API reçue, taille : ${responseTasks?.size}")

                        val mappedTasks = responseTasks?.map { mapTaskDtoToTask(it) } ?: emptyList()
                        _tasks.value = mappedTasks
                        Log.d("TaskRepository", "🔹 Tâches après mapping : ${_tasks.value?.size}")


                        CoroutineScope(Dispatchers.IO).launch {
                            try {

                                taskDao.insertAll(mappedTasks.map { mapTaskToEntity(it) })
                                Log.d("TaskRepository", "🔹 Base de données locale mise à jour")
                            } catch (e: Exception) {
                                Log.e("TaskRepository", "❌ Erreur lors de la mise à jour locale : ${e.message}")
                            }
                        }
                    } else {
                        Log.e("TaskRepository", "❌ Erreur HTTP : ${response.code()}")
                        loadTasksFromLocalDb(idFamille)
                    }
                }

                override fun onFailure(call: Call<List<TaskDto>>, t: Throwable) {
                    Log.e("TaskRepository", "❌ Erreur réseau : ${t.message}")
                    loadTasksFromLocalDb(idFamille)
                }
            })
        } else {
            Log.d("TaskRepository", "📱 Appareil hors ligne, chargement des données locales")
            loadTasksFromLocalDb(idFamille)
        }
    }

    private fun mapTaskToEntity(task: Task): TaskEntity {
        return TaskEntity(
            idTache = task.idTache,
            nom = task.nom,
            description = task.description,
            idUser = task.idUser,
            idFamille = task.idFamille,
            dateDebut = task.dateDebut,
            dateFin = task.dateFin,
            status = task.status ,
            type = task.type,
            priorite = task.priorite.toString()
        )
    }


}