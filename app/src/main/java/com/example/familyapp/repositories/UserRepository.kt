import android.util.Log

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.familyapp.data.model.user.User
import com.example.familyapp.network.AuthService
import com.example.familyapp.network.RetrofitClient
import com.example.familyapp.network.dto.autentDto.LoginResponse
import retrofit2.Call
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import retrofit2.Callback
import retrofit2.Response
import android.content.Context
import android.widget.Toast
import com.example.familyapp.data.model.user.AddUserRequest
import com.example.familyapp.network.dto.autentDto.FamilyInfo
import com.example.familyapp.network.dto.autentDto.LoginRequest
import com.example.familyapp.network.dto.userDto.UserDTO
 import com.example.familyapp.network.mapper.mapUserDtoToUser
import com.example.familyapp.network.mapper.mapAddUserRequestToUserDto


import com.example.familyapp.network.services.UserService


class UserRepository(context: Context) {
    private val authService = RetrofitClient.instance.create(AuthService::class.java)
    val userData: LiveData<User> get() = _userData
    private val _userData = MutableLiveData<User>()

    private val userService = RetrofitClient.instance.create(UserService::class.java)
    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> get() = _users
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
    // Dans UserRepository
    fun getMembers(familyId: Int): LiveData<List<User>> {
        val data = MutableLiveData<List<User>>()
        userService.getMembers(familyId).enqueue(object : Callback<List<UserDTO>> {
            override fun onResponse(call: Call<List<UserDTO>>, response: Response<List<UserDTO>>) {
                if (response.isSuccessful) {
                    data.postValue(response.body()?.map { dto -> mapUserDtoToUser(dto) } ?: emptyList())
                } else {
                    data.postValue(emptyList())
                }
            }

            override fun onFailure(call: Call<List<UserDTO>>, t: Throwable) {
                data.postValue(emptyList())
            }
        })
        return data
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


}