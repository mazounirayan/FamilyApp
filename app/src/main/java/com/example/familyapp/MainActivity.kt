package com.example.familyapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.familyapp.db.dao.UserDao
import com.example.familyapp.db.daos.ChatDao
import com.example.familyapp.db.daos.ConversationDao
import com.example.familyapp.db.daos.FamilleDao
import com.example.familyapp.repositories.ChatRepository
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var userDao: UserDao
    private lateinit var chatDao: ChatDao
    private lateinit var familleDao: FamilleDao
    private lateinit var conversationDao: ConversationDao
    private lateinit var chatRepository: ChatRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        val db = MainApplication.database
        userDao = db.userDao()
        chatDao = db.chatDao()
        familleDao = db.familleDao()
        conversationDao = db.conversationDao()
        userDao = db.userDao()
        chatDao =   db.chatDao()

        chatRepository = ChatRepository(applicationContext)


        chatRepository.syncChats()




        lifecycleScope.launch {
            val users = userDao.getUserById(1)
              Log.d("RoomDB", "Utilisateurs: $users")
        }

        NavigationBar.setupNavigationClicks(this)

    }
}