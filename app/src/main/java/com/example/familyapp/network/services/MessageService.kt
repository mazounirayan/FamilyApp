package com.example.familyapp.network.services

import com.example.familyapp.data.model.message.Message
import com.example.familyapp.network.dto.messageDto.NewMessageDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface MessageService {
    @GET("messages/chat/{id}")
    fun getMessagesOfChat(@Path("id") id:Int): Call<List<Message>>

    @POST("messages")
    fun addMessage(@Body message: NewMessageDto): Call<NewMessageDto>
}