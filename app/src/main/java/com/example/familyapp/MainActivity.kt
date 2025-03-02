package com.example.familyapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import com.example.familyapp.db.dao.UserDao
import com.example.familyapp.db.daos.ChatDao
import com.example.familyapp.db.daos.ConversationDao
import com.example.familyapp.db.daos.FamilleDao
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.familyapp.network.services.MessagePollingService

class MainActivity : AppCompatActivity() {

    private lateinit var notificationBadge: View
    private lateinit var userDao: UserDao
    private lateinit var chatDao: ChatDao
    private lateinit var familleDao: FamilleDao
    private lateinit var conversationDao: ConversationDao
    private val newMessageReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            notificationBadge.visibility = View.VISIBLE
        }
    }

    fun removeNotificationBadge() {
        notificationBadge.visibility = View.GONE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        NavigationBar.setupNavigationClicks(this)
        notificationBadge = findViewById(R.id.notification_badge)
        Intent(this, MessagePollingService::class.java).also { intent ->
            startService(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(newMessageReceiver, IntentFilter("com.example.familyapp.NEW_MESSAGE"))
    }

    override fun onStop() {
        LocalBroadcastManager.getInstance(this)
            .unregisterReceiver(newMessageReceiver)
        super.onStop()
    }
}
