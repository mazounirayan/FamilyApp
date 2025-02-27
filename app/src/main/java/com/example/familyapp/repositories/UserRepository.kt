import android.util.Log

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.familyapp.data.model.user.User
import com.example.familyapp.network.RetrofitClient
import com.example.familyapp.network.dto.autentDto.LoginResponse
import retrofit2.Call

import retrofit2.Callback
import retrofit2.Response
import android.content.Context
import androidx.lifecycle.MediatorLiveData
import androidx.room.Room
import com.example.familyapp.app_utils.NetworkUtils
import com.example.familyapp.data.model.user.AddUserRequest
import com.example.familyapp.data.model.user.LogoutResponse
import com.example.familyapp.data.model.user.UpdateUserRequest
import com.example.familyapp.db.AppDatabase
import com.example.familyapp.db.dao.UserDao
import com.example.familyapp.db.entities.UserEntity
import com.example.familyapp.network.dto.autentDto.FamilyInfo
import com.example.familyapp.network.dto.autentDto.LoginRequest
 import com.example.familyapp.network.dto.autentDto.SignUpRequest
import com.example.familyapp.network.dto.userDto.UserDTO
import com.example.familyapp.network.mapper.mapAddUserRequestToUserDto
import com.example.familyapp.network.mapper.mapUserDtoToUser


import com.example.familyapp.network.services.UserService
import com.example.familyapp.utils.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class UserRepository(context: Context) {
    val userData: LiveData<User> get() = _userData
    private val _userData = MutableLiveData<User>()
    private val _tokenData = MutableLiveData<String>()
    val tokenData: LiveData<String> get() = _tokenData
    private val context = context ;
    private val userService = RetrofitClient.instance.create(UserService::class.java)
    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> get() = _users
    private val _logoutStatus = MutableLiveData<Boolean>()
    val logoutStatus: MutableLiveData<Boolean> get() = _logoutStatus
    private val _userDataByToken = MutableLiveData<User>()
    val userDataByToken: LiveData<User> get() = _userDataByToken
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
    /*
      private val db = Room.databaseBuilder(
          context,
          AppDatabase::class.java, "family_app_db"
      ).build()
  */
    private val userDao: UserDao
    private val db: AppDatabase = Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java, "family_app_db"
    ).build()

    init {
        userDao = db.userDao()
    }


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
                        val userEntity = UserEntity(
                            id = user.id,
                            nom = user.nom,
                            prenom = user.prenom,
                            email = user.email,
                            motDePasse = user.motDePasse,
                            numTel = user.numTel,
                            role = user.role,
                            idFamille = user.idFamille,
                            dateInscription = user.dateInscription,
                            coins = user.coins,
                            avatar = user.avatar,
                            totalPoints = user.totalPoints
                        )
                        CoroutineScope(Dispatchers.IO).launch {
                            userDao.insertUser(userEntity)
                        }
                        /*scope.launch {
                            insertUserInDb(user)a
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

    fun getUserByToken(token: String, onResult: (Result<Unit>) -> Unit) {
        val call = userService.getUserByToken(token)
        call.enqueue(object : Callback<UserDTO> {
            override fun onResponse(call: Call<UserDTO>, response: Response<UserDTO>) {
                if (response.isSuccessful) {
                    val userDto = response.body()
                    userDto?.let {
                        val user = mapUserDtoToUser(it)
                        val userEntity =    UserEntity(
                            id = it.id,
                            nom = it.nom,
                            prenom = user.prenom,
                            email = user.email,
                            motDePasse = user.motDePasse,
                            numTel = user.numTel,
                            role = user.role,
                            idFamille = user.idFamille,
                            dateInscription = user.dateInscription,
                            coins = user.coins,
                            avatar = user.avatar,
                            totalPoints = user.totalPoints
                        )
                        CoroutineScope(Dispatchers.IO).launch {
                            userDao.insertUser(   userEntity)

                        }// Convertir UserDTO en User
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
    fun mapUserToEntity(user: User): UserEntity {
        return UserEntity(
            id = user.id,
            nom = user.nom,
            prenom = user.prenom,
            email = user.email,
            motDePasse = user.motDePasse,
            numTel = user.numTel,
            role = user.role,
            idFamille = user.idFamille,
            dateInscription = user.dateInscription,
            coins = user.coins,
            avatar = user.avatar,
            totalPoints = user.totalPoints
        )
    }
    fun mapUserEntityToUser(entity: UserEntity): User {
        return User(
            id = entity.id,
            nom = entity.nom,
            prenom = entity.prenom,
            email = entity.email,
            motDePasse = "", // Remplir avec une valeur par défaut ou le récupérer d'une autre source
            numTel = entity.numTel,
            role = entity.role,
            idFamille = entity.idFamille,
            dateInscription =entity.dateInscription,
            coins = entity.coins,
            avatar = entity.avatar,
            totalPoints = entity.totalPoints,
            chats = emptyList()
        )
    }


    fun getMembers(familyId: Int): LiveData<List<User>> {
        val result = MediatorLiveData<List<User>>()

        // Charger les données locales en premier
        val localDataSource = userDao.getMembersByFamilyId(familyId)
        result.addSource(localDataSource) { localUsers ->
            localUsers?.let {
                result.postValue(it.map { entity -> mapUserEntityToUser(entity) })
            }
        }

        // Vérifier si l'appareil est en ligne avant d'appeler l'API
        if (NetworkUtils.isOnline(context)) {
            userService.getMembers(familyId).enqueue(object : Callback<List<UserDTO>> {
                override fun onResponse(call: Call<List<UserDTO>>, response: Response<List<UserDTO>>) {
                    if (response.isSuccessful) {
                        response.body()?.let { userDtoList ->
                            val users = userDtoList.map { dto -> mapUserDtoToUser(dto) }

                            // Mettre à jour la base de données locale
                            scope.launch {
                                try {
                                    val entities = users.map { user -> mapUserToEntity(user) }
                                    userDao.insertMembers(entities)
                                    Log.d("UserRepository", "Base de données locale mise à jour avec ${entities.size} membres")
                                } catch (e: Exception) {
                                    Log.e("UserRepository", "Erreur lors de la mise à jour locale: ${e.message}")
                                }
                            }

                            // Mettre à jour les données affichées
                            result.postValue(users)
                        }
                    } else {
                        Log.e("UserRepository", "Erreur HTTP: ${response.code()} - ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<List<UserDTO>>, t: Throwable) {
                    Log.e("UserRepository", "Erreur réseau: ${t.message}")
                }
            })
        } else {
            Log.d("UserRepository", "Appareil hors ligne, utilisation des données locales uniquement")
        }

        return result
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


                            val userEntity = UserEntity(
                                id = user.id,
                                nom = user.nom,
                                prenom = user.prenom,
                                email = user.email,
                                motDePasse = user.motDePasse,
                                numTel = user.numTel,
                                role = user.role,
                                idFamille = user.idFamille,
                                dateInscription = user.dateInscription,
                                coins = user.coins,
                                avatar = user.avatar,
                                totalPoints = user.totalPoints
                            )
                            CoroutineScope(Dispatchers.IO).launch {
                                userDao.insertUser(userEntity)
                            }

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
                    CoroutineScope(Dispatchers.IO).launch {
                        userDao.updateUserFamille(idUser, idFamille)
                    }
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
                    CoroutineScope(Dispatchers.IO).launch {
                        updateUserInRoom(userId, updateUserRequest)
                    }
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
    private suspend fun updateUserInRoom(userId: Int, updateUserRequest: UpdateUserRequest) {
        with(userDao) {
            updateUserRequest.nom?.takeIf { it.isNotEmpty() }?.let { updateNom(userId, it) }
            updateUserRequest.prenom?.takeIf { it.isNotEmpty() }?.let { updatePrenom(userId, it) }
            updateUserRequest.email?.takeIf { it.isNotEmpty() }?.let { updateEmail(userId, it) }
            updateUserRequest.role?.takeIf { it.isNotEmpty() }?.let { updateRole(userId, it) }
        }
    }
}