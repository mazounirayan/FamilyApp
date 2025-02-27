package com.example.familyapp.db.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.familyapp.db.entities.ConversationEntity

@Dao
interface ConversationDao {
    @Query("SELECT * FROM conversations WHERE Id = :userId")
    fun getConversationsByUserId(userId: Int): LiveData<List<ConversationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConversations(conversations: List<ConversationEntity>)

    @Query("DELETE FROM conversations WHERE Id = :userId")
    suspend fun deleteConversationsByUserId(userId: Int)
}