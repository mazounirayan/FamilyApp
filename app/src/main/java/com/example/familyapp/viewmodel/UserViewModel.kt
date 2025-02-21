package com.example.familyapp.viewmodel

import UserRepository
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.familyapp.data.model.user.AddUserRequest
import com.example.familyapp.data.model.user.UpdateUserRequest
import com.example.familyapp.data.model.user.User
import retrofit2.Callback
import androidx.activity.result.launch
import androidx.lifecycle.viewModelScope
import com.example.familyapp.network.dto.autentDto.SignUpRequest
import com.example.familyapp.network.dto.rewardsDto.rewardsDto
import kotlinx.coroutines.launch
import android.view.View
import androidx.lifecycle.LifecycleOwner
import com.example.familyapp.network.RetrofitClient
import com.example.familyapp.network.services.UserService
import com.example.familyapp.utils.SessionManager
import retrofit2.Call
import retrofit2.Response

class UserViewModel(private val userRepository: UserRepository, val context: LifecycleOwner) : ViewModel() {
    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val nom = MutableLiveData<String>()
    val prenom = MutableLiveData<String>()
    val motDePasse = MutableLiveData<String>()
    val role = MutableLiveData<String>()
    val codeFamille = MutableLiveData<String>()
    private val _loginResult = MutableLiveData<Result<Unit>>()
    val loginResult: LiveData<Result<Unit>> = _loginResult
    val isChild = MutableLiveData<Boolean>()
    val nomFamille = MutableLiveData<String>()
    private val _signInResult = MutableLiveData<Result<Unit>>()
    val signInResult: LiveData<Result<Unit>> = _signInResult
    private val _user = MutableLiveData<List<User>>()
    val user: LiveData<List<User>> get() = _user
    val logoutStatus: LiveData<Boolean> get() = userRepository.logoutStatus
    private val userService = RetrofitClient.instance.create(UserService::class.java)
    init {
        role.observeForever { selectedRole ->
            isChild.value = selectedRole == "Enfant"
        }
    }
    private val _successMessages = MutableLiveData<String>()
    val successMessages: LiveData<String> = _successMessages
    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> get() = _users
    private val _errorMessages = MutableLiveData<String>()
    val errorMessages: LiveData<String>
        get() = _errorMessages
    fun fetchUsers(familyId: Int) {
        userRepository.getMembers(familyId).observeForever { fetchedUsers ->
            _users.value = fetchedUsers ?: emptyList()
        }
    }

    fun deleteUser(userId: Int,familyId: Int) {
        userRepository.deleteUser(userId) { result ->
            result.onSuccess {
                fetchUsers(familyId)
            }
            result.onFailure { error ->

            }
        }
    }

    fun addUser(addUserRequest: AddUserRequest) {
        Log.d("UserViewModel", "Attempting to add user: $addUserRequest")
        userRepository.createUser(addUserRequest,1) { result ->
            result.onSuccess { user ->
                Log.d("UserViewModel", "User added successfully: ${user.id}")
                fetchUsers(1)
            }
            result.onFailure { error ->
                Log.e("UserViewModel", "Failed to add user: ${error.message}")
            }
        }
    }
    fun updateUser(userId: Int, updateUserRequest: UpdateUserRequest) {
        userRepository.updateUser(userId, updateUserRequest) { result ->
            result.onSuccess {
                _successMessages.postValue("User successfully updated")
                fetchUsers(1)
            }
            result.onFailure { error ->
                _errorMessages.postValue(error.message ?: "An unknown error occurred")
            }
        }
    }

    
    val userData = userRepository.userData
    val tokenData = userRepository.tokenData
    private val _userByTokenResult = MutableLiveData<Result<Unit>>()
    val userByTokenResult: LiveData<Result<Unit>> get() = _userByTokenResult
    val userDataByToken: LiveData<User> get() = userRepository.userDataByToken

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


    fun getUserByToken(token: String) {
        userRepository.getUserByToken(token) { result ->
            result.fold(
                onSuccess = {
                    Log.d("UserViewModel", "Utilisateur récupéré avec succès")
                    _userByTokenResult.value = Result.success(Unit)
                },
                onFailure = { exception ->
                    Log.e("UserViewModel", "Erreur lors de la récupération de l'utilisateur : ${exception.message}")
                    _userByTokenResult.value = Result.failure(exception)
                }
            )
        }
    }

    private val _signUpResult = MutableLiveData<Result<Unit>>()
    val signUpResult: LiveData<Result<Unit>> = _signUpResult

    fun signUp() {
        viewModelScope.launch {
            try {

                val signUpRequest = SignUpRequest(
                    nom = nom.value.orEmpty().trim(),
                    prenom = prenom.value.orEmpty().trim(),
                    email = email.value.orEmpty().trim(),
                    motDePasse = motDePasse.value.orEmpty().trim(),
                    role = role.value.orEmpty(),
                    codeFamille = if (isChild.value == true) codeFamille.value?.trim() else null,
                    nomFamille = if (isChild.value != true) nomFamille.value?.trim() else null
                )

                // Envoi de la requête via le repository
                userRepository.signUp(signUpRequest) { result ->
                    _signUpResult.value = result.map { } // Conversion en Result<Unit>
                }
            } catch (e: Exception) {
                // Capture des erreurs générales
                Log.e("SignUp", "Erreur lors de l'inscription : ${e.message}")
                _signUpResult.value = Result.failure(e)
            }
        }
    }

    fun logout(userId: Int) {
        userRepository.logout(userId)
    }
    
    fun fetchUser(id:Int){
        _user.value
        this.userRepository.users.observe(this.context) { data ->
            this@UserViewModel._user.value = data
        }

        this.userRepository.getMembers(id)
    }

    /*fun fetchTask(idUser: Int) {
        _task.value
        this.taskRepo.tasks.observe(this.context) { data ->
            this@TaskViewModel._task.value = data
        }

        this.taskRepo.getTaskFromUser(idUser)
    }*/
}
