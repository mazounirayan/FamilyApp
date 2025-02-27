package com.example.familyapp.db.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.familyapp.db.entities.FamilleEntity

@Dao
interface FamilleDao {
    @Query("SELECT * FROM familles WHERE idFamille = :idFamille")
    fun getFamilleById(idFamille: Int): LiveData<FamilleEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFamilles(familles: List<FamilleEntity>)

    @Query("DELETE FROM familles WHERE idFamille = :idFamille")
    suspend fun deleteFamilleById(idFamille: Int)
}