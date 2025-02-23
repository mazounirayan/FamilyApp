package com.example.familyapp.repositories

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.familyapp.data.model.conversation.Conversation
import com.example.familyapp.network.RetrofitClient
import com.example.familyapp.network.services.ChatService
import com.example.familyapp.network.services.TaskService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ConversationRepository(context: Context) {
    private val chatService = RetrofitClient.instance.create(ChatService::class.java)

    fun getChatsByUserId(userId: Int): LiveData<List<Conversation>> {
        val data = MutableLiveData<List<Conversation>>()
        chatService.listChatsByUser(userId).enqueue(object : Callback<List<Conversation>> {
            override fun onResponse(call: Call<List<Conversation>>, response: Response<List<Conversation>>) {
                if (response.isSuccessful) {
                    Log.d("ConversationRepository", "API call successful, data received")
                    data.value = response.body()
                } else {
                    Log.e("ConversationRepository", "API call failed: ${response.errorBody()?.string()}")

                }
            }

            override fun onFailure(call: Call<List<Conversation>>, t: Throwable) {
                Log.e("ConversationRepository", "API call failed: ${t.message}")
            }
        })
        return data
    }

}
