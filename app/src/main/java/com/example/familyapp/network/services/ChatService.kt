package com.example.familyapp.network.services

import com.example.familyapp.data.model.chat.Chat
import com.example.familyapp.data.model.conversation.Conversation
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import com.example.familyapp.data.model.chat.CreateChat
import com.example.familyapp.data.model.conversation.ChatCreateRequest
import com.example.familyapp.network.dto.chatDto.ChatDto
import com.example.familyapp.network.dto.chatDto.ConversationDto
import retrofit2.http.Body
import retrofit2.http.POST
interface ChatService {
    @GET("users/{userId}/chats")
    fun listChatsByUser(@Path("userId") userId: Int): Call<List<ConversationDto>>

    @POST("chats")
    fun createChat(@Body chatRequest: ChatCreateRequest): Call<ChatDto>

     @POST("chats/user")
    fun addMessage(@Body chat: CreateChat) : Call<CreateChat>
}



