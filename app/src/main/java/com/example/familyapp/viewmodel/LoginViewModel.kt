package com.example.familyapp.viewmodel

import UserRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.familyapp.data.model.user.User
import kotlinx.coroutines.launch

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {
    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    private val _loginResult = MutableLiveData<Result<Unit>>()
    val loginResult: LiveData<Result<Unit>> = _loginResult


    val userData = userRepository.userData

    fun login() {
        val emailValue = email.value.orEmpty().trim()
        val passwordValue = password.value.orEmpty().trim()

        if (emailValue.isEmpty() || passwordValue.isEmpty()) {
            _loginResult.value = Result.failure(Exception("Les champs ne peuvent pas être vides"))
            return
        }

        userRepository.login(emailValue, passwordValue) { result ->
            _loginResult.value = result
        }
    }
}