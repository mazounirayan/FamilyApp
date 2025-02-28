package com.example.familyapp.network.services

import com.example.familyapp.data.model.chat.Chat
import com.example.familyapp.data.model.message.Message
import com.example.familyapp.network.dto.messageDto.MessageDto
import com.example.familyapp.network.dto.messageDto.NewMessageDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import java.util.Date

interface MessageService {
    @GET("messages/chat/{id}")
    fun getMessagesOfChat(@Path("id") id:Int): Call<List<MessageDto>>

    @POST("messages")
    fun addMessage(@Body message: NewMessageDto): Call<NewMessageDto>

    @GET("/messages/new/{userId}/{date}")
    fun newMessagesOfUser(@Path("userId") userId: Int, @Path("date") date: String): Call<List<Message>>
}