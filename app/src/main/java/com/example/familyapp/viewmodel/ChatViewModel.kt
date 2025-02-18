package com.example.familyapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.familyapp.data.model.chat.CreateChat
import com.example.familyapp.repositories.ChatRepository

class ChatViewModel(
    private val chatRepo: ChatRepository
) : ViewModel() {

    private val _chatCreationStatus = MutableLiveData<Boolean>()
    val chatCreationStatus: LiveData<Boolean> get() = _chatCreationStatus

    fun addChat(chat: CreateChat) {
        chatRepo.chatCreationStatus.observeForever { status ->
            _chatCreationStatus.value = status
        }
        chatRepo.addChat(chat)
    }
}
