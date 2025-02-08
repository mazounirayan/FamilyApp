package com.example.familyapp.network.services

import com.example.familyapp.data.model.chat.CreateChat
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ChatService {

    @POST("chats/user")
    fun addMessage(@Body chat: CreateChat) : Call<CreateChat>

}