package com.example.familyapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.familyapp.db.dao.UserDao
import com.example.familyapp.db.daos.ChatDao
import com.example.familyapp.db.daos.FamilleDao
import com.example.familyapp.db.daos.MessageDao
import com.example.familyapp.db.daos.RecompenseDao
import com.example.familyapp.db.daos.TaskDao
import com.example.familyapp.db.entities.ChatEntity
import com.example.familyapp.db.entities.FamilleEntity
import com.example.familyapp.db.entities.UserEntity
import com.example.familyapp.db.entities.MessageEntity
import com.example.familyapp.db.entities.RecompenseEntity
import com.example.familyapp.db.entities.TaskEntity
import com.example.familyapp.db.entities.UserChatCrossRef

@Database(
    entities = [UserEntity::class ,ChatEntity::class, MessageEntity::class,
               FamilleEntity::class, TaskEntity::class,  UserChatCrossRef::class,
               RecompenseEntity::class,
               ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun chatDao(): ChatDao
    abstract fun messageDao(): MessageDao
    abstract fun familleDao(): FamilleDao
    abstract fun taskDao(): TaskDao
    abstract fun recompenseDao(): RecompenseDao


}

