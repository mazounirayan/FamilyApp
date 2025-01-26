package com.example.familyapp.viewmodel


import UserRepository
import androidx.activity.result.launch
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.familyapp.data.model.user.User
import com.example.familyapp.network.dto.rewardsDto.rewardsDto
import kotlinx.coroutines.launch

class UserViewModel(private val userRepository: UserRepository) : ViewModel() {
    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    private val _loginResult = MutableLiveData<Result<Unit>>()
    val loginResult: LiveData<Result<Unit>> = _loginResult

    private val _signInResult = MutableLiveData<Result<Unit>>()
    val signInResult: LiveData<Result<Unit>> = _signInResult



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

    private val _loading = MutableLiveData<Boolean>()


    val signUpResult: LiveData<SignUpResponseDto?> = userRepository.signUpResult

    fun signUp(nom: String, prenom: String, email: String, motDePasse: String, idFamille: Int) {
        val signUpRequest = SignUpRequestDto(nom, prenom, email, motDePasse, idFamille)
        viewModelScope.launch {
            authRepo.signUp(signUpRequest)
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