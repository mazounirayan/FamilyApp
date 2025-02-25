package com.example.familyapp.repositories

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.familyapp.MainApplication
import com.example.familyapp.data.model.chat.CreateChat
import com.example.familyapp.db.entities.ChatEntity
import com.example.familyapp.network.RetrofitClient
import com.example.familyapp.network.services.ChatService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatRepository(context: Context) {

    private val chatService = RetrofitClient.instance.create(ChatService::class.java)

    private val _chatCreationStatus = MutableLiveData<Boolean>()
    val chatCreationStatus: MutableLiveData<Boolean> get() = _chatCreationStatus
    private val db = MainApplication.database
    private val chatDao = db.chatDao()
    private val scope = CoroutineScope(SupervisorJob())
    fun addChat(chat: CreateChat) {
        val call = chatService.addMessage(chat)

        call.enqueue(object : Callback<CreateChat> {
            override fun onResponse(call: Call<CreateChat>, response: Response<CreateChat>) {
                if (response.isSuccessful) {
                    Log.d("ChatRepository", "Chat créé avec succès")
                    _chatCreationStatus.value = true // Indique que le chat a été créé avec succès
                    val createdChat = response.body()

                    // Si le chat a été créé avec succès, persister les données dans la base de données locale
                    createdChat?.let {
                        Log.d("ChatRepository", "Chat créé avec succès")
                        scope.launch {
                            // Sauvegarde du chat dans la base de données locale
                            //chatDao.addChats(listOf(ChatEntity(idChat = it.idChat, libelle = it.libelle)))
                        }
                    }
                } else {
                    Log.e("ChatRepository", "Erreur HTTP : ${response.code()}")
                    _chatCreationStatus.value = false // Indique une erreur lors de la création du chat
                }
            }

            override fun onFailure(call: Call<CreateChat>, t: Throwable) {
                Log.e("ChatRepository", "Erreur réseau : ${t.message}")
                _chatCreationStatus.value = false // Indique une erreur réseau
            }
        })
    }
}
