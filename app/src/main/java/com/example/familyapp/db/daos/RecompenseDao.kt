package com.example.familyapp.db.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.familyapp.db.entities.RecompenseEntity

@Dao
interface RecompenseDao {
    @Query("SELECT * FROM recompenses")
    fun getAllRecompenses(): LiveData<List<RecompenseEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecompenses(recompenses: List<RecompenseEntity>)

    @Query("DELETE FROM recompenses WHERE idRecompense = :idRecompense")
    suspend fun deleteRecompenseById(idRecompense: Int)

    @Query("DELETE FROM recompenses")
    suspend fun deleteAllRecompenses()
}