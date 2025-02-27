package com.example.familyapp.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.familyapp.db.entities.UserEntity

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)


    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun getUserById(id: Int): UserEntity?

    @Query("UPDATE users SET idFamille = :idFamille WHERE id = :idUser")
    suspend fun updateUserFamille(idUser: Int, idFamille: Int)

    @Query("UPDATE users SET role = :role WHERE id = :id")
    suspend fun updateRole(id: Int, role: String)
    @Query("UPDATE users SET nom = :nom WHERE id = :id")
    suspend fun updateNom(id: Int, nom: String)

    @Query("UPDATE users SET prenom = :prenom WHERE id = :id")
    suspend fun updatePrenom(id: Int, prenom: String)

    @Query("UPDATE users SET email = :email WHERE id = :id")
    suspend fun updateEmail(id: Int, email: String)
    @Query("SELECT * FROM users WHERE idFamille = :idFamille")
    fun getMembersByFamilyId(idFamille: Int): LiveData<List<UserEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMembers(users: List<UserEntity>)
}
