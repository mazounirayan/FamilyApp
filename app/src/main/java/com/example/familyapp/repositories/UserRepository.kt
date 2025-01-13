package com.example.familyapp.repositories

import android.content.Context
import android.util.Log
import com.example.familyapp.network.AuthService
import com.example.familyapp.network.RetrofitClient
import com.example.familyapp.network.dto.autentDto.LoginRequest
import com.example.familyapp.network.dto.autentDto.LoginResponse
import com.example.familyapp.network.mapper.mapUserDtoToUser
import retrofit2.Call
import retrofit2.Response

class UserRepository(private val context: Context) {

    // Instance du service Retrofit
    private val authService = RetrofitClient.instance.create(AuthService::class.java)

    /* Base de données locale
    private val db = Room.databaseBuilder(
        context,
        AppDatabase::class.java, "family_app_db"
    ).build()
    */

    // Fonction pour effectuer un login via l'API
    fun login(email: String, motDePasse: String, callback: (Result<Unit>) -> Unit) {
        val call = authService.login(LoginRequest(email, motDePasse))

        call.enqueue(object : retrofit2.Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    loginResponse?.let {
                        val user = mapUserDtoToUser(it.user)
                        Log.d("UserRepository", "Bienvenue, ${user.nom} ${user.prenom}!")
                    }
                    callback(Result.success(Unit)) // Connexion réussie
                } else {
                    callback(Result.failure(Exception("Erreur : ${response.message()}"))) // Erreur HTTP
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                callback(Result.failure(t)) // Exception réseau
            }
        })
    }


    /* Fonction pour insérer un utilisateur dans la base de données locale
    suspend fun insertUserFromApi(user: User) {
        val userDao = db.userDao()

        val userEntity = UserEntity(
            id = user.id,
            nom = user.nom,
            prenom = user.prenom,
            email = user.email,
            profession = user.profession,
            numTel = user.numTel,
            role = user.role,
            idFamille = user.idFamille
        )

        userDao.insertUser(userEntity)
    }
    */
}