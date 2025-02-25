package com.example.familyapp.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.familyapp.db.entities.ChatEntity
import com.example.familyapp.db.entities.UserChatCrossRef

@Dao
interface ChatDao {
    @Query("SELECT * FROM chats")
    fun getAllChats(): List<ChatEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addChats(chats: List<ChatEntity>)
}