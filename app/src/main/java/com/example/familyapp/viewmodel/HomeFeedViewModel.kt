package com.example.familyapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.familyapp.models.User
import com.example.familyapp.repositories.UserRepository
import kotlinx.coroutines.launch

class ConnexionViewModel(private val userRepository: UserRepository) : ViewModel() {

    // LiveData pour observer l'état de la connexion
    private val _loginResult = MutableLiveData<Result<User?>>()
    val loginResult: LiveData<Result<User?>> get() = _loginResult

    // LiveData pour observer l'état de l'inscription
    private val _signupResult = MutableLiveData<Result<Boolean>>()
    val signupResult: LiveData<Result<Boolean>> get() = _signupResult

    // Fonction pour gérer la connexion
    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            try {
                val user = userRepository.getUserByEmailAndPassword(email, password)
                if (user != null) {
                    _loginResult.postValue(Result.success(user))
                } else {
                    _loginResult.postValue(Result.failure(Exception("Utilisateur non trouvé ou mot de passe incorrect.")))
                }
            } catch (e: Exception) {
                _loginResult.postValue(Result.failure(e))
            }
        }
    }

    // Fonction pour gérer l'inscription
    fun signupUser(newUser: User) {
        viewModelScope.launch {
            try {
                val success = userRepository.createUser(newUser)
                if (success) {
                    _signupResult.postValue(Result.success(true))
                } else {
                    _signupResult.postValue(Result.failure(Exception("Échec de l'inscription.")))
                }
            } catch (e: Exception) {
                _signupResult.postValue(Result.failure(e))
            }
        }
    }
}
