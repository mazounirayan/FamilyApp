package com.example.familyapp.repositories

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.familyapp.data.model.chat.Chat
import com.example.familyapp.data.model.chat.CreateChat
import com.example.familyapp.data.model.conversation.ChatCreateRequest
import com.example.familyapp.network.RetrofitClient
import com.example.familyapp.network.dto.messageDto.NewMessageDto
import com.example.familyapp.network.services.ChatService
import com.example.familyapp.network.services.MessageService
import com.example.familyapp.utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChatRepository(context: Context) {

    private val chatService = RetrofitClient.instance.create(ChatService::class.java)


    private val _chatCreationStatus = MutableLiveData<Boolean>()
    val chatCreationStatus: MutableLiveData<Boolean> get() = _chatCreationStatus

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


}
