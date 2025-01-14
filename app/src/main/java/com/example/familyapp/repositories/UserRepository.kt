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
import com.example.familyapp.network.dto.autentDto.LoginRequest
import com.example.familyapp.network.mapper.mapUserDtoToUser


class UserRepository(context: Context) {
    private val authService = RetrofitClient.instance.create(AuthService::class.java)
    private val _userData = MutableLiveData<User>()
    val userData: LiveData<User> get() = _userData

  /*
    private val db = Room.databaseBuilder(
        context,
        AppDatabase::class.java, "family_app_db"
    ).build()
*/
    val scope = CoroutineScope(SupervisorJob())

    fun login(email: String, password: String, onResult: (Result<Unit>) -> Unit) {
        val loginRequest = LoginRequest(email, password)
        val call = authService.login(loginRequest)

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