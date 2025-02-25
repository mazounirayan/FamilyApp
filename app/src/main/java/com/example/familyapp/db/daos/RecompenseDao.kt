package com.example.familyapp.db.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.familyapp.db.entities.RecompenseEntity

@Dao
interface RecompenseDao {
    @Query("SELECT * FROM recompenses WHERE idFamille = :familleId")
    suspend fun getRecompensesByFamille(familleId: Int): List<RecompenseEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecompense(recompense: RecompenseEntity)

    @Update
    suspend fun updateRecompense(recompense: RecompenseEntity)

    @Delete
    suspend fun deleteRecompense(recompense: RecompenseEntity)
}