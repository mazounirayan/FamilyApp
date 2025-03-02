package com.example.familyapp.viewmodel


import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.familyapp.data.model.task.Task
import com.example.familyapp.data.model.task.TaskUpdate
import com.example.familyapp.data.model.task.TaskUpdateFull
import com.example.familyapp.network.dto.taskDto.TaskRequestDto
import com.example.familyapp.repositories.TaskRepository
import com.example.familyapp.utils.SessionManager

class TaskViewModel(
    private val taskRepo: TaskRepository,
    val context:LifecycleOwner
): ViewModel() {



    private val _task = MutableLiveData<List<Task>>()

    val task: LiveData<List<Task>> get() = _task
    private val _taskDeletionStatus = MutableLiveData<Boolean>()
    val taskDeletionStatus: LiveData<Boolean> get() = _taskDeletionStatus

    fun fetchTask(id: Int) {
        _task.value
        this.taskRepo.tasks.observe(this.context) { data ->
            this@TaskViewModel._task.value = data
        }
        if(SessionManager.currentUser!!.role == "Parent"){
            this.taskRepo.getAllTasks(id)
        }else{
            this.taskRepo.getTaskFromUser(id)
        }
    }

    fun addTask(task: TaskRequestDto){
        this.taskRepo.addTask(task)
    }

    fun patchTask(id:Int,task: TaskUpdate){
        this.taskRepo.patchTask(id,task)
    }


    fun patchTaskFull(id:Int,task: TaskUpdateFull){
        this.taskRepo.patchTaskFull(id,task)
    }


    fun deleteTask(taskId: Int) {
        taskRepo.deleteTask(taskId) { result ->
            result.onSuccess {
                _taskDeletionStatus.postValue(true)
            }
            result.onFailure {
                _taskDeletionStatus.postValue(false)
            }
        }
    }

    fun fetchAllTasks(idFamille: Int) {

        _task.value = emptyList()
        this.taskRepo.tasks.observe(this.context) { data ->
             _task.value = data
        }

        this.taskRepo.getAllTasks(idFamille)
     }

}
