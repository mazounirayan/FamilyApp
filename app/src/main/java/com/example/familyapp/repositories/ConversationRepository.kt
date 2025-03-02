package com.example.familyapp.repositories

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.familyapp.data.model.chat.Chat
import com.example.familyapp.data.model.chat.CreateChat
import com.example.familyapp.data.model.conversation.ChatCreateRequest
import com.example.familyapp.data.model.conversation.Conversation
import com.example.familyapp.network.RetrofitClient
import com.example.familyapp.network.dto.chatDto.ChatDto
import com.example.familyapp.network.dto.chatDto.ConversationDto

import com.example.familyapp.network.mapper.mapChatDtoToChat
import com.example.familyapp.network.mapper.mapConversationDtoToConversation
import com.example.familyapp.network.services.ChatService
import com.example.familyapp.network.services.MessageService

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ConversationRepository(context: Context) {
    private val chatService = RetrofitClient.instance.create(ChatService::class.java)
    private val messageService = RetrofitClient.instance.create(MessageService::class.java)
    private val _chatQuitStatus = MutableLiveData<Boolean>()
    val chatQuitStatus: LiveData<Boolean> get() = _chatQuitStatus
    private val _chatCreationStatus = MutableLiveData<Boolean>()
    val chatCreationStatus: MutableLiveData<Boolean> get() = _chatCreationStatus

    fun getChatsByUserId(userId: Int): LiveData<List<Conversation>> {
        val data = MutableLiveData<List<Conversation>>()
        chatService.listChatsByUser(userId).enqueue(object : Callback<List<ConversationDto>> {
            override fun onResponse(call: Call<List<ConversationDto>>, response: Response<List<ConversationDto>>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    data.value = responseBody?.map { conversationDto ->
                        mapConversationDtoToConversation(conversationDto)
                    }
                } else {
                    Log.e("ConversationRepository", "API call failed: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<List<ConversationDto>>, t: Throwable) {
                Log.e("ConversationRepository", "API call failed: ${t.message}")
            }
        })
        return data
    }


    fun createChat(libelle: String, participants: List<Int>, onResult: (Chat?) -> Unit) {
        val chatRequest = ChatCreateRequest(libelle, participants)
        chatService.createChat(chatRequest).enqueue(object : Callback<ChatDto> {
            override fun onResponse(call: Call<ChatDto>, response: Response<ChatDto>) {
                if (response.isSuccessful) {
                    val chat = response.body()?.let { mapChatDtoToChat(it) }
                    if (chat != null) {

                    } else {
                        onResult(null)
                    }
                } else {
                    onResult(null)
                }
            }

            override fun onFailure(call: Call<ChatDto>, t: Throwable) {
                onResult(null)
            }
        })
    }



    fun addChat(chat: CreateChat) {
        val call = chatService.addMessage(chat)

        call.enqueue(object : Callback<CreateChat> {
            override fun onResponse(call: Call<CreateChat>, response: Response<CreateChat>) {
                if (response.isSuccessful) {
                    Log.d("ChatRepository", "Chat créé avec succès")
                    _chatCreationStatus.value = true
                } else {
                    Log.e("ChatRepository", "Erreur HTTP : ${response.code()}")
                    _chatCreationStatus.value = false
                }
            }

            override fun onFailure(call: Call<CreateChat>, t: Throwable) {
                Log.e("ChatRepository", "Erreur réseau : ${t.message}")
                _chatCreationStatus.value = false
            }
        })
    }


    fun quitChat(userId: Int, chatId: Int) {
        val call = chatService.quitChat(userId, chatId)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.d("ChatRepository", "Utilisateur a quitté le chat avec succès")
                    _chatQuitStatus.postValue(true)
                } else {
                    Log.e("ChatRepository", "Erreur HTTP : ${response.code()}")
                    _chatQuitStatus.postValue(false)
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("ChatRepository", "Erreur réseau : ${t.message}")
                _chatQuitStatus.postValue(false)
            }
        })
    }

    fun getUsersOfChat(chatId: Int, onResult: (Result<List<Int>>) -> Unit) {
        chatService.getUsersOfChat(chatId).enqueue(object : Callback<List<Int>> {
            override fun onResponse(
                call: Call<List<Int>>,
                response: Response<List<Int>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { userIds ->
                        onResult(Result.success(userIds))
                    } ?: onResult(Result.failure(Exception("Réponse vide")))
                } else {
                    onResult(Result.failure(Exception("Erreur HTTP : ${response.code()}")))
                }
            }

            override fun onFailure(call: Call<List<Int>>, t: Throwable) {
                onResult(Result.failure(t))
            }
        })
    }

}
