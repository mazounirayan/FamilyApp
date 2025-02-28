import android.util.Log

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.familyapp.data.model.user.User
import com.example.familyapp.network.RetrofitClient
import com.example.familyapp.network.dto.autentDto.LoginResponse
import retrofit2.Call
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import retrofit2.Callback
import retrofit2.Response
import android.content.Context
import com.example.familyapp.data.model.user.AddUserRequest
import com.example.familyapp.data.model.user.LogoutResponse
import com.example.familyapp.data.model.user.UpdateUserRequest
import com.example.familyapp.network.dto.autentDto.FamilyInfo
import com.example.familyapp.network.dto.autentDto.LoginRequest
 import com.example.familyapp.network.dto.autentDto.SignUpRequest
import com.example.familyapp.network.dto.messageDto.MaxIdMessageDto
import com.example.familyapp.network.dto.userDto.UserDTO
import com.example.familyapp.network.mapper.mapAddUserRequestToUserDto
import com.example.familyapp.network.mapper.mapUserDtoToUser


import com.example.familyapp.network.services.UserService
import com.example.familyapp.utils.SessionManager


class UserRepository(context: Context) {
    val userData: LiveData<User> get() = _userData
    private val _userData = MutableLiveData<User>()
    private val _tokenData = MutableLiveData<String>()
    val tokenData: LiveData<String> get() = _tokenData

    private val userService = RetrofitClient.instance.create(UserService::class.java)
    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> get() = _users
    private val _logoutStatus = MutableLiveData<Boolean>()
    val logoutStatus: MutableLiveData<Boolean> get() = _logoutStatus
    private val _userDataByToken = MutableLiveData<User>()
    val userDataByToken: LiveData<User> get() = _userDataByToken
    /*
      private val db = Room.databaseBuilder(
          context,
          AppDatabase::class.java, "family_app_db"
      ).build()
  */
    val scope = CoroutineScope(SupervisorJob())


    fun login(email: String, password: String, onResult: (Result<Unit>) -> Unit) {
        val loginRequest = LoginRequest(email, password)
        val call = userService.login(loginRequest)
        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    loginResponse?.let {
                        val user = mapUserDtoToUser(it.user)
                        _userData.value = user
                        _tokenData.value = it.token
                        onResult(Result.success(Unit))

                    }
                } else {
                    onResult(Result.failure(Exception("Erreur : ${response.message()}")))
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e("LoginError", t.message ?: "Erreur réseau")
                onResult(Result.failure(t))
            }
        })
    }

    /*private suspend fun insertUserInDb(user: User) {

    }*/
      fun getUserByToken(token: String, onResult: (Result<Unit>) -> Unit) {
        val call = userService.getUserByToken(token)
        call.enqueue(object : Callback<UserDTO> {
            override fun onResponse(call: Call<UserDTO>, response: Response<UserDTO>) {
                if (response.isSuccessful) {
                    val userDto = response.body()
                    userDto?.let {
                        val user = mapUserDtoToUser(it) // Convertir UserDTO en User
                        _userDataByToken.value = user
                        SessionManager.currentUser = user
                        onResult(Result.success(Unit))
                    } ?: onResult(Result.failure(Exception("Réponse vide")))
                } else {
                    onResult(Result.failure(Exception("Erreur HTTP : ${response.code()}")))
                }
            }

            override fun onFailure(call: Call<UserDTO>, t: Throwable) {
                Log.e("UserRepository", "Erreur réseau : ${t.message}")
                onResult(Result.failure(t))
            }
        })
    }

    
    fun signUp(signUpRequest: SignUpRequest, onResult: (Result<Boolean>) -> Unit) {
        val call = userService.signUp(signUpRequest)

        call.enqueue(object : Callback<UserDTO> {
            override fun onResponse(call: Call<UserDTO>, response: Response<UserDTO>) {
                if (response.isSuccessful) {
                    onResult(Result.success(true))
                } else {
                    val errorMessage = "Erreur HTTP ${response.code()}: ${response.errorBody()?.string() ?: response.message()}"
                    Log.e("SignUpResponse", errorMessage)
                    onResult(Result.failure(Exception(errorMessage)))
                }
            }

            override fun onFailure(call: Call<UserDTO>, t: Throwable) {
                val errorMessage = t.message ?: "Erreur réseau inconnue"
                Log.e("SignUpFailure", errorMessage)
                onResult(Result.failure(Exception(errorMessage)))
            }
        })
    }
    fun getMembersTask(id:Int) {
        val call = userService.getMembers(id)

        call.enqueue(object : Callback<List<UserDTO>> {
            override fun onResponse(
                call: Call<List<UserDTO>>,
                response: Response<List<UserDTO>>
            ) {


                if (response.isSuccessful) {
                    val responseBody = response.body()
                    _users.value = responseBody?.map { userDto ->
                        mapUserDtoToUser(userDto) // Mapper chaque UserDTO en User
                    }
                }

                else {
                    Log.e("UserRepository", "Erreur HTTP : ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<UserDTO>>, t: Throwable) {
                Log.e("TaskRepository", "Erreur réseau : ${t.message}")
            }
        })
    }

    fun getMembers(familyId: Int): LiveData<List<User>> {
        val data = MutableLiveData<List<User>>()
        userService.getMembers(familyId).enqueue(object : Callback<List<UserDTO>> {
            override fun onResponse(call: Call<List<UserDTO>>, response: Response<List<UserDTO>>) {
                if (response.isSuccessful) {
                    data.postValue(response.body()?.map { dto -> mapUserDtoToUser(dto) } ?: emptyList())
                } else {
                    Log.e("UserRepository", "Échec de la récupération des membres: ${response.code()}")
                    data.postValue(emptyList())
                }
            }

            override fun onFailure(call: Call<List<UserDTO>>, t: Throwable) {
                data.postValue(emptyList())
            }
        })
        return data
    }


    fun logout(userId: Int) {
        val call = userService.logout(userId)

        call.enqueue(object : Callback<LogoutResponse> {
            override fun onResponse(call: Call<LogoutResponse>, response: Response<LogoutResponse>) {
                if (response.isSuccessful) {
                    Log.d("UserRepository", "Déconnexion réussie : ${response.body()}")
                    _logoutStatus.value = true
                } else {
                    Log.e("UserRepository", "Erreur HTTP : ${response.code()}")
                    _logoutStatus.value = false
                }
            }

            override fun onFailure(call: Call<LogoutResponse>, t: Throwable) {
                Log.e("UserRepository", "Erreur réseau : ${t.message}")
                _logoutStatus.value = false
            }
        })
    }
    fun deleteUser(userId: Int, onResult: (Result<Unit>) -> Unit) {
        userService.deleteUser(userId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    onResult(Result.success(Unit))
                } else {
                    onResult(Result.failure(Exception("Erreur de suppression : ${response.code()} - ${response.message()}")))
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                onResult(Result.failure(Exception("Erreur réseau lors de la suppression: ${t.message}")))
            }
        })
    }
    fun createUser(addUserRequest: AddUserRequest, idFamille: Int, onResult: (Result<User>) -> Unit) {
        val addUserRequestDTO = mapAddUserRequestToUserDto(addUserRequest)
        userService.createUser(addUserRequestDTO).enqueue(object : Callback<UserDTO> {
            override fun onResponse(call: Call<UserDTO>, response: Response<UserDTO>) {
                if (response.isSuccessful) {
                    response.body()?.let { userDto ->
                        try {
                            val user = mapUserDtoToUser(userDto)
                            _users.postValue(listOf(user))
                            addUserToFamille(user.id, idFamille, { familyResult ->
                                if (familyResult.isSuccess) {
                                    onResult(Result.success(user))
                                } else {
                                    onResult(Result.failure(Exception("Failed to add user to family")))
                                }
                            })
                        } catch (e: Exception) {
                            Log.e("CreateUser", "Failed to map user data: ${e.message}", e)
                            onResult(Result.failure(Exception("Failed to map user data: ${e.message}")))
                        }
                    } ?: run {
                        Log.e("CreateUser", "No user data received")
                        onResult(Result.failure(Exception("Failed to parse user data")))
                    }
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("CreateUser", "Failed to create user: $errorBody")
                    onResult(Result.failure(Exception("Failed to create user: $errorBody")))
                }
            }

            override fun onFailure(call: Call<UserDTO>, t: Throwable) {
                Log.e("CreateUser", "Network error: ${t.message}", t)
                onResult(Result.failure(Exception("Network error: ${t.message}")))
            }
        })
    }
    fun addUserToFamille(idUser: Int, idFamille: Int, onFamilyResult: (Result<Unit>) -> Unit) {
        val familyInfo = FamilyInfo(idFamille)
        userService.addUserToFamille(idUser, familyInfo).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    onFamilyResult(Result.success(Unit))
                } else {
                    onFamilyResult(Result.failure(Exception("Failed to add user to family: ${response.message()}")))
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                onFamilyResult(Result.failure(Exception("Network error: ${t.message}")))
            }
        })
    }
    fun updateUser(userId: Int, updateUserRequest: UpdateUserRequest, onResult: (Result<Unit>) -> Unit) {
        userService.updateUser(userId, updateUserRequest).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    onResult(Result.success(Unit))
                } else {
                    onResult(Result.failure(Exception("Failed to update user: ${response.message()}")))
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                onResult(Result.failure(Exception("Network error: ${t.message}")))
            }
        })
    }

    fun getMaxMessageId(familyId: Int, onResult: (Result<MaxIdMessageDto>) -> Unit) {
        userService.getMaxMessageId(familyId).enqueue(object : Callback<MaxIdMessageDto> {
            override fun onResponse(
                call: Call<MaxIdMessageDto>,
                response: Response<MaxIdMessageDto>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { maxIdList ->
                        onResult(Result.success(maxIdList))
                    } ?: onResult(Result.failure(Exception("Réponse vide")))
                } else {
                    onResult(Result.failure(Exception("Erreur HTTP : ${response.code()}")))
                }
            }

            override fun onFailure(call: Call<MaxIdMessageDto>, t: Throwable) {
                onResult(Result.failure(t))
            }
        })
    }


}
