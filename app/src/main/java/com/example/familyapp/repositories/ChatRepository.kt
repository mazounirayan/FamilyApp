package com.example.familyapp.repositories

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.familyapp.MainApplication
import com.example.familyapp.app_utils.NetworkUtils
import com.example.familyapp.data.model.chat.CreateChat
import com.example.familyapp.db.entities.ChatEntity
import com.example.familyapp.network.RetrofitClient
import com.example.familyapp.network.services.ChatService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatRepository(context : Context) {
    private val context: Context =context
    private val chatService = RetrofitClient.instance.create(ChatService::class.java)

    private val _chatCreationStatus = MutableLiveData<Boolean>()
    val chatCreationStatus: MutableLiveData<Boolean> get() = _chatCreationStatus
    private val db = MainApplication.database
    private val chatDao = db.chatDao()
    private val scope = CoroutineScope(SupervisorJob())
    fun addChat(chat: CreateChat) {
        if (NetworkUtils.isOnline(context)) {
            val call = chatService.addMessage(chat)

            call.enqueue(object : Callback<CreateChat> {
                override fun onResponse(call: Call<CreateChat>, response: Response<CreateChat>) {
                    if (response.isSuccessful) {
                        Log.d("ChatRepository", "Chat créé avec succès")
                        _chatCreationStatus.value = true
                        val createdChat = response.body()


                        createdChat?.let {
                            Log.d("ChatRepository", "Chat créé avec succès")
                            scope.launch {
                                val chatEntity = mapCreateChatToChatEntity(it)
                                saveChatsInLocalDb(listOf(chatEntity))
                            }
                        }
                    } else {
                        Log.e("ChatRepository", "Erreur HTTP : ${response.code()}")
                        _chatCreationStatus.value = false
                    }
                }

                override fun onFailure(call: Call<CreateChat>, t: Throwable) {
                    Log.e("ChatRepository", "Erreur réseau : ${t.message}")
                    _chatCreationStatus.value = false // Indique une erreur réseau
                }
            })
        } else {
            // Sauvegarder le chat localement si hors ligne
            scope.launch {
                val chatEntity = mapCreateChatToChatEntity(chat)
                saveChatsInLocalDb(listOf(chatEntity))
                _chatCreationStatus.value = true // Indique que le chat a été sauvegardé localement
            }
        }
    }
    private fun mapCreateChatToChatEntity(createChat: CreateChat): ChatEntity {
        return ChatEntity(
            idUser = createChat.idUser,
            idChat = createChat.idChat
        )
    }
    suspend fun saveChatsInLocalDb(chats: List<ChatEntity>) {
        val chatDao = this.db.chatDao()

        try {
            chatDao.addChats(chats)
            Log.d("saveChatsInLocalDb", "Chats sauvegardés avec succès")
        } catch (e: Exception) {
            Log.e("saveChatsInLocalDb", "Erreur lors de la sauvegarde des chats : ${e.message}")
        }
    }
    fun syncChats() {
        scope.launch {
            val unsyncedChats = chatDao.getUnsyncedChats()
            unsyncedChats.forEach { chat ->
                val createChat = CreateChat(idUser = chat.idUser, idChat = chat.idChat ?: -1)
                val call = chatService.addMessage(createChat)

                call.enqueue(object : Callback<CreateChat> {
                    override fun onResponse(call: Call<CreateChat>, response: Response<CreateChat>) {
                        if (response.isSuccessful) {
                            val createdChat = response.body()
                            createdChat?.let {
                                scope.launch {
                                    chatDao.updateChatId(chat.localId, it.idChat) // Vous devez implémenter cette méthode dans votre DAO
                                }
                            }
                        }
                    }

                    override fun onFailure(call: Call<CreateChat>, t: Throwable) {
                        Log.e("ChatRepository", "Erreur réseau lors de la synchronisation : ${t.message}")
                    }
                })
            }
        }
    }
}