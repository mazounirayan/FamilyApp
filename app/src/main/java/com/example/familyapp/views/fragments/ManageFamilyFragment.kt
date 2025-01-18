package com.example.familyapp.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.familyapp.R
import com.example.familyapp.data.model.user.User
import com.example.familyapp.views.Adapters.UserMembershipAdapter
import com.example.familyapp.views.AddUserDialogFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ManageFamilyFragment : Fragment() {

    private lateinit var familyRecyclerView: RecyclerView
    private lateinit var userMembershipAdapter: UserMembershipAdapter
    private val userList = mutableListOf<User>()

    private val familyMembers = listOf(
        User(1, "Fils", "Prenom", "email@example.com", "1234",  "0000", "User", 1, "2023-01-01",65,"",0),
        User(2, "Fils01", "Prenom", "email@example.com", "5678", "0000", "User", 1, "2023-01-01",70,"",0),
        User(2, "Fils02", "Prenom", "email@example.com", "5678",  "0000", "User", 1, "2023-01-01",70,"",0)
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_manage_family,container,false)
        userMembershipAdapter = UserMembershipAdapter(familyMembers)

        view.findViewById<FloatingActionButton>(R.id.add_member_button).setOnClickListener {
            val dialogFragment = AddUserDialogFragment { newUser ->
                 userList.add(newUser)

                userMembershipAdapter.notifyItemInserted(userList.size - 1)
            }
            dialogFragment.show(parentFragmentManager, "AddUserDialog")
        }

            /*AddUserDialog { user ->
                userList.add(user)
                userMembershipAdapter.notifyItemInserted(userList.size - 1)
            }*/





        return view;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.familyRecyclerView = view.findViewById(R.id.family_recycler_view)
        familyRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
        familyRecyclerView.adapter = userMembershipAdapter

    }
    /*override fun onCreateView(savedInstanceState: Bundle?,inflater: LayoutInflater, container:ViewGroup?) {
        super.onCreate(savedInstanceState)
        val view = infla(R.layout.activity_manage_family)

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
    }*/
}
