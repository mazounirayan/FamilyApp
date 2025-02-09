package com.example.familyapp.repositories

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.familyapp.network.RetrofitClient
import com.example.familyapp.network.dto.messageDto.MessageDto
import com.example.familyapp.network.services.MessageService
import com.example.familyapp.data.model.message.Message
import com.example.familyapp.network.dto.messageDto.NewMessageDto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MessageRepository(context: Context) {

    private val messageService = RetrofitClient.instance.create(MessageService::class.java)

    private val _messages = MutableLiveData<List<Message>>()
    val messages: LiveData<List<Message>> get() = _messages

    fun getMessagesOfChat(chatId: Int) {
        val call = messageService.getMessagesOfChat(chatId)

        call.enqueue(object : Callback<List<Message>> {
            override fun onResponse(
                call: Call<List<Message>>,
                response: Response<List<Message>>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    _messages.value = responseBody?.map { it }
                } else {
                    Log.e("MessageRepository", "Erreur HTTP : ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Message>>, t: Throwable) {
                Log.e("MessageRepository", "Erreur réseau : ${t.message}")
            }
        })
    }

    fun addMessage(message: NewMessageDto) {
        val call = messageService.addMessage(message)

        call.enqueue(object : Callback<NewMessageDto> {
            override fun onResponse(
                call: Call<NewMessageDto>,
                response: Response<NewMessageDto>
            ) {
                if (response.isSuccessful) {
                    Log.d("MessageRepository", "Message ajouté avec succès : ${response.body()}")
                } else {
                    Log.e("MessageRepository", "Erreur HTTP : ${response.code()}")
                }
            }

            override fun onFailure(call: Call<NewMessageDto>, t: Throwable) {
                Log.e("MessageRepository", "Erreur réseau : ${t.message}")
            }
        })
    }
}
