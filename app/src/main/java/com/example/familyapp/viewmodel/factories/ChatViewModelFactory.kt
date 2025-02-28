package com.example.familyapp.viewmodel.factories

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.familyapp.repositories.MessageRepository
import com.example.familyapp.viewmodel.MessageViewModel

class ChatViewModelFactory (
    private val repository: MessageRepository,
    private val fragment: Fragment
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MessageViewModel::class.java)) {
            return ChatViewModelFactory(repository, fragment) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}