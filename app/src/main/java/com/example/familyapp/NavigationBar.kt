package com.example.familyapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.familyapp.activity.TaskActivity
import com.example.familyapp.views.fragments.task.ManageTaskFragment

open class NavigationBar: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_bars)

        findViewById<ImageView>(R.id.back_button).setOnClickListener {
            handleBackButtonClick(this)
        }

        findViewById<ImageView>(R.id.user_profile_icon).setOnClickListener {
            handleUserProfileClick(this)
        }

        findViewById<ImageView>(R.id.home_icon).setOnClickListener {
            handleHomeIconClick(this)
        }

        findViewById<ImageView>(R.id.messages_icon).setOnClickListener {
            handleMessagesIconClick(this)
        }

        findViewById<ImageView>(R.id.add_icon).setOnClickListener {
            handleCreateIconClick(this,)
        }

        findViewById<ImageView>(R.id.list_icon).setOnClickListener {
            handleTaskListIconClick(this)
        }
    }

    companion object {
        fun handleBackButtonClick(context: Context) {
            Toast.makeText(context, "Redirection vers le l'element précédent", Toast.LENGTH_SHORT)
                .show()
        }

        fun handleUserProfileClick(context: Context) {
            Toast.makeText(context, "Redirection vers le profil utilisateur", Toast.LENGTH_SHORT)
                .show()
        }

        fun handleMessagesIconClick(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }

        fun handleHomeIconClick(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }

        fun handleCreateIconClick(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }

        fun handleTaskListIconClick(context: Context) {
            val intent = Intent(context, TaskActivity::class.java)
            context.startActivity(intent)
        }

    }

}