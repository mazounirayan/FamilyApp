package com.example.familyapp.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.familyapp.db.entities.MessageEntity

@Dao
interface MessageDao {
    @Query("SELECT * FROM messages WHERE idChat = :chatId ORDER BY dateEnvoie DESC")
    suspend fun getMessagesForChat(chatId: Int): List<MessageEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: MessageEntity)
}