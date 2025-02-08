package com.example.familyapp.viewmodel


import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.familyapp.data.model.task.Task
import com.example.familyapp.data.model.task.TaskUpdate
import com.example.familyapp.network.dto.taskDto.TaskDto
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

    fun addTask(task:TaskDto){
        this.taskRepo.addTask(task)
    }

    fun patchTask(id:Int,task: TaskUpdate){
        this.taskRepo.patchTask(id,task)
    }

    /*fun refreshTasks() {
        // Force une mise à jour des données observées
        _task.postValue(_task.value)
   */
}
