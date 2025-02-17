package com.example.familyapp.network.services

import com.example.familyapp.data.model.conversation.Conversation
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ChatService {
    @GET("users/{userId}/chats")
    fun listChatsByUser(@Path("userId") userId: Int): Call<List<Conversation>>
}
