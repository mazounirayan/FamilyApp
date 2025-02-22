package com.example.familyapp.viewmodel

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.familyapp.data.model.message.Message
import com.example.familyapp.network.dto.messageDto.MessageDto
import com.example.familyapp.network.dto.messageDto.NewMessageDto
import com.example.familyapp.repositories.MessageRepository

class MessageViewModel(
    private val messageRepo: MessageRepository,
    private val fragment: Fragment
) : ViewModel() {

    private val _messages = MutableLiveData<List<Message>>()
    val messages: LiveData<List<Message>> get() = _messages

    private val _messageCreationStatus = MutableLiveData<Boolean>()
    val messageCreationStatus: LiveData<Boolean> get() = _messageCreationStatus

    fun fetchMessagesOfChat(chatId: Int) {
        messageRepo.messages.observeForever { data ->
            _messages.value = data
        }
        messageRepo.getMessagesOfChat(chatId)
    }

    fun addMessage(message: NewMessageDto) {
        val tempLiveData = MutableLiveData<Boolean>()
        messageRepo.addMessage(message)

        // Observer temporaire pour capter l'état de création du message
        tempLiveData.observeForever { status ->
            _messageCreationStatus.value = status
        }
    }
}
