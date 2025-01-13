package com.example.familyapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.familyapp.data.model.user.User
import com.example.familyapp.views.Adapters.UserMembershipAdapter
import com.example.familyapp.views.AddUserDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ManageFamilyActivity : AppCompatActivity() {

    private lateinit var familyRecyclerView: RecyclerView
    private lateinit var userMembershipAdapter: UserMembershipAdapter
    private val userList = mutableListOf<User>()

    private val familyMembers = listOf(
        User(1, "Fils", "Prenom", "email@example.com", "1234", "Profession", "0000", "User", 1, "2023-01-01",65),
        User(2, "Fils01", "Prenom", "email@example.com", "5678", "Profession", "0000", "User", 1, "2023-01-01",70),
        User(2, "Fils02", "Prenom", "email@example.com", "5678", "Profession", "0000", "User", 1, "2023-01-01",70)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_family)

        familyRecyclerView = findViewById(R.id.family_recycler_view)
        userMembershipAdapter = UserMembershipAdapter(familyMembers)
        familyRecyclerView.layoutManager = LinearLayoutManager(this)
        familyRecyclerView.adapter = userMembershipAdapter

        val addButton = findViewById<FloatingActionButton>(R.id.add_member_button)
        addButton.setOnClickListener {
            AddUserDialog { user ->
                 userList.add(user)
                userMembershipAdapter.notifyItemInserted(userList.size - 1)
            }.show(supportFragmentManager, "AddUserDialog")
        }
    }
}
