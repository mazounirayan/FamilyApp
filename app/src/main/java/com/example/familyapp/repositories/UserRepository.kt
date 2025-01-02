package com.example.familyapp.repositories

import android.content.Context
import android.util.Log
import com.example.familyapp.data.model.user.User
import com.example.familyapp.network.AuthService
import com.example.familyapp.network.RetrofitClient
import com.example.familyapp.network.dto.autentDto.LoginRequest
import retrofit2.Response
import com.example.familyapp.network.mapper.mapUserDtoToUser
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
    suspend fun login(email: String, motDePasse: String): Result<Unit> {
        return try {
            val response = authService.login(LoginRequest(email, motDePasse)).execute()
            if (response.isSuccessful) {
                val loginResponse = response.body()
                loginResponse?.let {
                    val user = mapUserDtoToUser(it.user)
                    Log.d("UserRepository", "Bienvenue, ${user.nom} ${user.prenom}!")
                }
                Result.success(Unit) // Connexion réussie
            } else {
                Result.failure(Exception("Erreur : ${response.message()}")) // Erreur réseau
            }
        } catch (e: Exception) {
            Result.failure(e) // Exception réseau
        }
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