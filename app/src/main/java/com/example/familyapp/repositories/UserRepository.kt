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
import com.example.familyapp.network.UserService
import com.example.familyapp.network.dto.autentDto.LoginRequest
import com.example.familyapp.network.dto.autentDto.SignUpRequest
import com.example.familyapp.network.dto.autentDto.SignUpResponse
import com.example.familyapp.network.dto.userDto.UserDTO
import com.example.familyapp.network.mapper.mapUserDtoToUser


class UserRepository(context: Context) {
    private val _userData = MutableLiveData<User>()
    val userData: LiveData<User> get() = _userData

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

    
    fun signUp(signUpRequest: SignUpRequest, onResult: (Result<Boolean>) -> Unit) {
        val call = userService.signUp(signUpRequest)

        call.enqueue(object : Callback<UserDTO> {
            override fun onResponse(call: Call<UserDTO>, response: Response<UserDTO>) {
                if (response.isSuccessful) {
                    // Inscription réussie
                    onResult(Result.success(true))
                } else {
                    // Gestion des erreurs HTTP avec un message détaillé
                    val errorMessage = "Erreur HTTP ${response.code()}: ${response.errorBody()?.string() ?: response.message()}"
                    Log.e("SignUpResponse", errorMessage)
                    onResult(Result.failure(Exception(errorMessage)))
                }
            }

            override fun onFailure(call: Call<UserDTO>, t: Throwable) {
                // Gestion des erreurs réseau
                val errorMessage = t.message ?: "Erreur réseau inconnue"
                Log.e("SignUpFailure", errorMessage)
                onResult(Result.failure(Exception(errorMessage)))
            }
        })
    }
    
    fun getMembers(id:Int) {
        val call = userService.getAllUsers(id)

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
}