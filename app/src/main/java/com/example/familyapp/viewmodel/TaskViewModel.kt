package com.example.familyapp.viewmodel


import android.util.Log
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.familyapp.data.model.task.Task
import com.example.familyapp.repositories.TaskRepository

class TaskViewModel(
    private val taskRepo: TaskRepository,
    val context:LifecycleOwner
): ViewModel() {



    private val _task = MutableLiveData<List<Task>>()

    val task: LiveData<List<Task>> get() = _task

    fun fetchTask(idUser: Int) {
        _task.value
        this.taskRepo.tasks.observe(this.context) { data ->
            this@TaskViewModel._task.value = data
        }

        this.taskRepo.getTaskFromUser(idUser)
    }
    fun fetchAllTasks(idFamille: Int) {
        Log.d("TaskViewModel", "🔹 Début du fetchAllTasks pour idFamille : $idFamille") // 🔥 Vérifie si c'est bien appelé

        _task.value = emptyList() // Réinitialise la liste des tâches avant de charger
        this.taskRepo.tasks.observe(this.context) { data ->
            Log.d("TaskViewModel", "🔹 Tâches reçues du repo : ${data.size}") // 🔥 Vérifie si le repo retourne des données
            _task.value = data
        }

        this.taskRepo.getAllTasks(idFamille)
        Log.d("TaskViewModel", "🔹 Après appel de getAllTasks dans le repository") // 🔥 Vérifie si ça plante après
    }

}
