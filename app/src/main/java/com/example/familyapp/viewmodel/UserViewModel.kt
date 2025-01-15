package com.example.familyapp.viewmodel


import UserRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UserViewModel(private val userRepository: UserRepository) : ViewModel() {
    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    private val _loginResult = MutableLiveData<Result<Unit>>()
    val loginResult: LiveData<Result<Unit>> = _loginResult


    val userData = userRepository.userData

    fun login() {

        val emailValue = "a@a.aaa"//email.value.orEmpty().trim()
        val passwordValue = "azerty"//password.value.orEmpty().trim()

        if (emailValue.isEmpty() || passwordValue.isEmpty()) {
            _loginResult.value = Result.failure(Exception("Les champs ne peuvent pas être vides"))
            return
        }

        userRepository.login(emailValue, passwordValue) { result ->
            _loginResult.value = result
        }
    }

    /*fun fetchTask(idUser: Int) {
        _task.value
        this.taskRepo.tasks.observe(this.context) { data ->
            this@TaskViewModel._task.value = data
        }

        this.taskRepo.getTaskFromUser(idUser)
    }*/
}