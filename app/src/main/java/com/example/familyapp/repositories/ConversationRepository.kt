package com.example.familyapp.repositories

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.familyapp.MainApplication
import com.example.familyapp.app_utils.NetworkUtils
import com.example.familyapp.data.model.conversation.Conversation
import com.example.familyapp.db.entities.ConversationEntity
import com.example.familyapp.network.RetrofitClient
import com.example.familyapp.network.services.ChatService
import com.example.familyapp.network.services.TaskService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ConversationRepository(context: Context) {
    private val chatService = RetrofitClient.instance.create(ChatService::class.java)
    private val scope = CoroutineScope(SupervisorJob())
    private val db = MainApplication.database
    private val conversationDao = db.conversationDao()
    private val context = context
    fun getChatsByUserId(userId: Int): LiveData<List<Conversation>> {
        val data = MutableLiveData<List<Conversation>>()
        if (NetworkUtils.isOnline(context)) {
            chatService.listChatsByUser(userId).enqueue(object : Callback<List<Conversation>> {
                override fun onResponse(call: Call<List<Conversation>>, response: Response<List<Conversation>>) {
                    if (response.isSuccessful) {
                        Log.d("ConversationRepository", "API call successful, data received")
                        val conversations = response.body()
                        conversations?.let {
                            scope.launch {
                                saveConversationsLocally(it, userId)
                            }
                            data.value = it
                        }
                    } else {
                            Log.e("ConversationRepository", "API call failed: ${response.errorBody()?.string()}")
                            // En cas d'échec, récupérer les données locales
                            loadConversationsFromLocalDb(userId, data)
                    }
                }

                override fun onFailure(call: Call<List<Conversation>>, t: Throwable) {
                    Log.e("ConversationRepository", "API call failed: ${t.message}")
                }
            })     } else {
                loadConversationsFromLocalDb(userId, data)
            }
            return data
    }
        private fun loadConversationsFromLocalDb(userId: Int, data: MutableLiveData<List<Conversation>>) {
            val localConversations = conversationDao.getConversationsByUserId(userId)
            localConversations.observeForever {
                val conversations = it.map { entity ->
                    Conversation(
                        id = entity.id,
                        name = entity.name,
                        lastMessage = entity.lastMessage,
                        messageTime = entity.messageTime
                        ,profileImage = entity.profileImage
                    )
                }
                data.value = conversations
            }
        }
        private suspend fun saveConversationsLocally(conversations: List<Conversation>, userId: Int) {
            val conversationEntities = conversations.map {
                ConversationEntity(
                    id = it.id,
                    name = it.name,
                    lastMessage = it.lastMessage,
                    messageTime = it.messageTime
                    ,profileImage = it.profileImage
                )
            }

            conversationDao.insertConversations(conversationEntities)
        }
}
