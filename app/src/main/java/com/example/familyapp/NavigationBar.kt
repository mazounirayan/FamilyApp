package com.example.familyapp

import android.content.Intent
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.familyapp.activity.TaskActivity

object NavigationBar{

    fun setupNavigationClicks(activity: AppCompatActivity) {
        activity.findViewById<ImageView>(R.id.back_button)?.setOnClickListener {
            activity.onBackPressed()
        }

        activity.findViewById<ImageView>(R.id.user_profile_icon)?.setOnClickListener {
            Toast.makeText(activity, "Redirection vers le profil utilisateur", Toast.LENGTH_SHORT).show()
        }

        activity.findViewById<ImageView>(R.id.home_icon)?.setOnClickListener {
            val intent = Intent(activity, MainActivity::class.java)
            activity.startActivity(intent)
        }

        activity.findViewById<ImageView>(R.id.messages_icon)?.setOnClickListener {
            navigateToHomeFragment(activity)
            println("messages_icon")

        }

        activity.findViewById<ImageView>(R.id.add_icon)?.setOnClickListener {

            navigateToHomeFragment(activity)
            println("add_icon")

        }

        activity.findViewById<ImageView>(R.id.list_icon)?.setOnClickListener {
            val intent = Intent(activity, TaskActivity::class.java)
            activity.startActivity(intent)
        }
    }

    private fun navigateToHomeFragment(activity: AppCompatActivity) {
        val fragmentManager = activity.supportFragmentManager
        val transaction = fragmentManager.beginTransaction()

        transaction.replace(R.id.fragment_container, HomeFragment())
            .addToBackStack(null)
            .commit()
    }
}