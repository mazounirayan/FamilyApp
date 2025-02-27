package com.example.familyapp.db.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.familyapp.db.entities.TaskEntity

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks")
    suspend fun getAllTasks(): List<TaskEntity>

    @Query("SELECT * FROM tasks WHERE idUser = :idUser")
    fun getTasksByUser(idUser: Int): LiveData<List<TaskEntity>>


    @Query("SELECT * FROM tasks WHERE idFamille = :idFamille")
    fun getTasksByFamille(idFamille: Int): LiveData<List<TaskEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(tasks: List<TaskEntity>)

    @Update
    suspend fun updateTask(task: TaskEntity)

    @Delete
    suspend fun deleteTask(task: TaskEntity)

    @Query("DELETE FROM tasks")
    suspend fun clearAll()

    @Query("SELECT * FROM tasks WHERE idTache = :id LIMIT 1")
    suspend fun getTaskById(id: Int): TaskEntity?
}