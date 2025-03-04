package com.example.familyapp

import android.content.Intent
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.familyapp.views.fragments.conversation.ConversationsFragment
import com.example.familyapp.views.fragments.dashbord.DashboardFragment
import com.example.familyapp.views.fragments.manageFamily.ManageFamilyFragment
import com.example.familyapp.views.fragments.task.ManageTaskFragment

object NavigationBar{

    
    fun setupNavigationClicks(activity: AppCompatActivity) {

        val supportFragmentManager = activity.supportFragmentManager

        supportFragmentManager.commit {
            replace<DashboardFragment>(R.id.fragment_container)
            setReorderingAllowed(true)
            addToBackStack("name")
        }

        activity.findViewById<ImageView>(R.id.user_profile_icon)?.setOnClickListener {
            Toast.makeText(activity, "Redirection vers le profil utilisateur", Toast.LENGTH_SHORT).show()
            val intent = Intent(activity, ProfileActivity::class.java)
            activity.startActivity(intent)
        }

        activity.findViewById<ImageView>(R.id.home_icon)?.setOnClickListener {
            val supportFragmentManager = activity.supportFragmentManager

            supportFragmentManager.commit {
                replace<DashboardFragment>(R.id.fragment_container)
                setReorderingAllowed(true)
                addToBackStack("name")
            }

        }

        activity.findViewById<ImageView>(R.id.messages_icon)?.setOnClickListener {
            val supportFragmentManager = activity.supportFragmentManager

            supportFragmentManager.commit {
                replace<ConversationsFragment>(R.id.fragment_container)
                setReorderingAllowed(true)
                addToBackStack("name")
            }
        }

        activity.findViewById<ImageView>(R.id.add_icon)?.setOnClickListener {

            val supportFragmentManager = activity.supportFragmentManager

            supportFragmentManager.commit {
                replace<ManageFamilyFragment>(R.id.fragment_container)
                setReorderingAllowed(true)
                addToBackStack("name")
            }
        }

        activity.findViewById<ImageView>(R.id.list_icon)?.setOnClickListener {
            val supportFragmentManager = activity.supportFragmentManager

            supportFragmentManager.commit {
                replace<ManageTaskFragment>(R.id.fragment_container)
                setReorderingAllowed(true)
                addToBackStack("name") // Name can be null
            }
        }

    }

}