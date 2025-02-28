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
import com.example.familyapp.network.mapper.mapMessageDtoToMessage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Date

class MessageRepository(context: Context) {

    private val messageService = RetrofitClient.instance.create(MessageService::class.java)

    private val _messages = MutableLiveData<List<Message>>()
    val messages: LiveData<List<Message>> get() = _messages
    private val _newMessages = MutableLiveData<List<Message>>()
    val newMessages: LiveData<List<Message>> get() = _newMessages

    fun getMessagesOfChat(chatId: Int) {
        val call = messageService.getMessagesOfChat(chatId)

        call.enqueue(object : Callback<List<MessageDto>> {
            override fun onResponse(
                call: Call<List<MessageDto>>,
                response: Response<List<MessageDto>>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    _messages.value = responseBody?.let{
                        it.map { value ->
                            mapMessageDtoToMessage(value)
                        }
                    }
                } else {
                    Log.e("MessageRepository", "Erreur HTTP : ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<MessageDto>>, t: Throwable) {
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

    fun getNewMessagesForUser(userId: Int, date: String) {
        val call = messageService.newMessagesOfUser(userId, date)

        call.enqueue(object : Callback<List<Message>> {
            override fun onResponse(call: Call<List<Message>>, response: Response<List<Message>>) {
                if (response.isSuccessful) {
                    _newMessages.postValue(response.body())
                } else {
                    Log.e("MessageRepository", "Erreur HTTP : ${response.code()}")
                    _newMessages.postValue(emptyList())
                }
            }

            override fun onFailure(call: Call<List<Message>>, t: Throwable) {
                Log.e("MessageRepository", "Erreur réseau : ${t.message}")
                _newMessages.postValue(emptyList())
            }
        })
    }

}
