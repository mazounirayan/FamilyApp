package com.example.familyapp.views.fragments.dashbord

import UserRepository
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.familyapp.R
import com.example.familyapp.data.model.user.User
import com.example.familyapp.utils.SessionManager
import com.example.familyapp.viewmodel.UserViewModel
import com.example.familyapp.viewmodel.factories.UserViewModelFactory
import com.example.familyapp.views.fragments.ManageFamilyFragment

class UsersFragment : Fragment() {

    private val userViewModel: UserViewModel by viewModels {
        UserViewModelFactory(
            UserRepository(this.requireContext()),
            fragment = this
        )
    }
    private val currentUserIdFamille= SessionManager.currentUser!!.idFamille ?:-1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.item_dashboard_users, container, false)
        val userList = view.findViewById<LinearLayout>(R.id.user_list)

         userViewModel.users.observe(viewLifecycleOwner) { users ->
            userList.removeAllViews()

            users.take(2).forEach { user ->
                val userTextView = TextView(context).apply {
                    text = "${user.prenom} ${user.nom}"
                    textSize = 16f
                    setPadding(0, 8, 0, 8)
                }
                userList.addView(userTextView)
            }
        }

         userViewModel.fetchUsers(1)

         val viewMoreUsers = view.findViewById<TextView>(R.id.view_more_users)
        viewMoreUsers.setOnClickListener {
            redirectToFullUsersFragment()
        }

        return view
    }

    private fun redirectToFullUsersFragment() {

        val fragment = ManageFamilyFragment()
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment) // Assurez-vous que ce conteneur existe dans votre activité
            .addToBackStack(null)
            .commit()

    }
}
