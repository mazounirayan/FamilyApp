package com.example.familyapp.network.services

import UserRepository
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import androidx.core.app.NotificationCompat
import androidx.lifecycle.Observer
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.familyapp.data.model.message.Message
import com.example.familyapp.repositories.MessageRepository
import com.example.familyapp.utils.SessionManager
import com.example.familyapp.viewmodel.MessageViewModel
import com.example.familyapp.viewmodel.UserViewModel


class MessagePollingService : Service() {
    private val handler = Handler(Looper.getMainLooper())
    private val pollingInterval = 4000L // 1 minute
    private lateinit var messageViewModel: MessageViewModel
    private lateinit var userViewModel: UserViewModel
    private var user = SessionManager.currentUser
    private lateinit var notificationManager: NotificationManager

    override fun onCreate() {
        super.onCreate()
        val messageRepo = MessageRepository(this)
        messageViewModel = MessageViewModel(messageRepo)
        val userRepo = UserRepository(this)
        userViewModel = UserViewModel(userRepo)
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "chat_messages",
                "Messages",
                NotificationManager.IMPORTANCE_HIGH // Importance élevée pour une visibilité maximale
            ).apply {
                description = "Notifications pour les nouveaux messages"
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    private val pollingRunnable = object : Runnable {
        override fun run() {
            checkForNewMessages()
            handler.postDelayed(this, pollingInterval)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        handler.post(pollingRunnable)
        return START_STICKY
    }


    private fun showNotification(message: Message) {
        val notification = NotificationCompat.Builder(this, "chat_messages")
            .setContentTitle("Nouveau message")
            .setContentText(message.contenu)
            .setSmallIcon(com.example.familyapp.R.drawable.ic_default_icon)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setOngoing(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL) // active son, vibration et lumière
            .build()

        notificationManager.notify(1, notification)
        val intent = Intent("com.example.familyapp.NEW_MESSAGE")
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)

    }


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }


    private fun checkForNewMessages() {
        val userId = SessionManager.currentUser?.id ?: return
        val lastMessageId = getLastMessageId()

        val oneTimeObserver = object : Observer<List<Message>> {
            override fun onChanged(messages: List<Message>) {
                if (messages.isNotEmpty()) {
                    if (!SessionManager.isChatActive) {
                        updateLastMessageId(messages.maxOf { it.idMessage.toLong() }.toString())
                        showNotification(messages[0])
                    }
                }
                messageViewModel.newMessages.removeObserver(this)
            }
        }

        messageViewModel.newMessages.observeForever(oneTimeObserver)
        messageViewModel.fetchNewMessagesForUser(userId, lastMessageId.toString())
    }

    private fun getLastMessageId(): Int? {
        userViewModel.fetchMaxMessageId(user!!.id)
        val maxId = userViewModel.maxMessageIds.value?.maxIdMessage
        val prefs = getSharedPreferences("MessagePollingPrefs", Context.MODE_PRIVATE)


        return maxId?.let { prefs.getInt("lastMessageId", it) }

    }

    private fun updateLastMessageId(id: String) {
        val prefs = getSharedPreferences("MessagePollingPrefs", Context.MODE_PRIVATE)
        prefs.edit().putInt("lastMessageId", id.toInt()).apply()
    }

}

