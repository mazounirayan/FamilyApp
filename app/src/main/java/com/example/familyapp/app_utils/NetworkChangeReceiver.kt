package com.example.familyapp.app_utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.familyapp.repositories.ChatRepository

class NetworkChangeReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val chatRepository = ChatRepository(context)
        if (NetworkUtils.isOnline(context)) {
            chatRepository.syncChats()
        }
    }
}