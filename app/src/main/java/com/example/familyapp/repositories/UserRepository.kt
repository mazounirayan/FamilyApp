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
import com.example.familyapp.data.model.user.LogoutResponse
import com.example.familyapp.network.UserService
import com.example.familyapp.data.model.user.AddUserRequest
import com.example.familyapp.data.model.user.UpdateUserRequest
import com.example.familyapp.network.dto.autentDto.FamilyInfo
import com.example.familyapp.network.dto.autentDto.LoginRequest
import com.example.familyapp.network.dto.userDto.UserDTO
 import com.example.familyapp.network.dto.autentDto.SignUpRequest
import com.example.familyapp.network.dto.autentDto.UsersbytokenRequest
import com.example.familyapp.network.dto.userDto.UserDTO
import com.example.familyapp.network.mapper.mapUserDtoToUser
import com.example.familyapp.network.mapper.mapAddUserRequestToUserDto


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

                        /*scope.launch {
                            insertUserInDb(user)
                        }*/

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
}