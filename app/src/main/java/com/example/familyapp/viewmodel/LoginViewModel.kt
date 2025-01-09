package com.example.familyapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.familyapp.repositories.UserRepository
import com.example.familyapp.network.dto.autentDto.LoginRequest
import kotlinx.coroutines.launch

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {

    // Champs observables pour l'email et le mot de passe
    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    // État de la connexion (succès ou erreur)
    private val _loginResult = MutableLiveData<Result<Boolean>>()
    val loginResult: LiveData<Result<Boolean>> = _loginResult

    // Fonction pour gérer la logique de connexion
    fun login() {
        //val emailValue = email.value.orEmpty().trim()
        //val passwordValue = password.value.orEmpty().trim()
        val emailValue = "pierre.dupont@mail.com"
        val passwordValue = "motdepasse123"
        if (emailValue.isEmpty() || passwordValue.isEmpty()) {
            _loginResult.value = Result.failure(Exception("Les champs ne peuvent pas être vides"))
            return
        }

        viewModelScope.launch {
            try {
                userRepository.login(emailValue, passwordValue)
                _loginResult.value = Result.success(true)
            } catch (exception: Exception) {
                _loginResult.value = Result.failure(exception)
            }
        }
    }
}