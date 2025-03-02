package com.example.familyapp.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.familyapp.db.entities.MessageEntity

@Dao
interface MessageDao {
    @Query("SELECT * FROM messages WHERE idChat = :chatId ORDER BY dateEnvoie DESC")
    suspend fun getMessagesByChatId(chatId: Int): List<MessageEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessages(messages: List<MessageEntity>)

    @Query("DELETE FROM messages WHERE idMessage = :chatId")
    suspend fun deleteMessagesByChatId(chatId: Int)
}