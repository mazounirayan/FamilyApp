package com.example.familyapp.viewmodel


import UserRepository
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.familyapp.data.model.task.Task
import com.example.familyapp.data.model.user.User

class UserViewModel(private val userRepository: UserRepository,
                    val context: LifecycleOwner
) : ViewModel() {
    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    private val _loginResult = MutableLiveData<Result<Unit>>()
    val loginResult: LiveData<Result<Unit>> = _loginResult


    private val _user = MutableLiveData<List<User>>()

    val user: LiveData<List<User>> get() = _user

    fun login() {

        val emailValue = "jean.dupont@example.com"//email.value.orEmpty().trim()
        val passwordValue = "password123"//password.value.orEmpty().trim()

        if (emailValue.isEmpty() || passwordValue.isEmpty()) {
            _loginResult.value = Result.failure(Exception("Les champs ne peuvent pas être vides"))
            return
        }

        userRepository.login(emailValue, passwordValue) { result ->
            _loginResult.value = result
        }
    }

    fun fetchUser(id:Int){
        _user.value
        this.userRepository.users.observe(this.context) { data ->
            this@UserViewModel._user.value = data
        }

        this.userRepository.getAllUsers(id)
    }
}