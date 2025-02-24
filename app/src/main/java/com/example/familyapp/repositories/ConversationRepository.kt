package com.example.familyapp.repositories

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.familyapp.data.model.chat.Chat
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ConversationRepository(context: Context) {
    private val chatService = RetrofitClient.instance.create(ChatService::class.java)
    private val messageService = RetrofitClient.instance.create(MessageService::class.java)

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



}
