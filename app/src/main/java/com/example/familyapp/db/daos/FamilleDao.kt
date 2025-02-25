package com.example.familyapp.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.familyapp.db.entities.FamilleEntity

@Dao
interface FamilleDao {
    @Query("SELECT * FROM familles WHERE idFamille = :familleId")
    suspend fun getFamilleById(familleId: Int): FamilleEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFamille(famille: FamilleEntity)
}