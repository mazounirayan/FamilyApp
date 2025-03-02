package com.example.familyapp.views.fragments.manageFamily


import UserRepository
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.familyapp.R
import com.example.familyapp.data.model.user.User
import com.example.familyapp.utils.SessionManager
import com.example.familyapp.viewmodel.UserViewModel
import com.example.familyapp.viewmodel.factories.UserViewModelFactory
import com.example.familyapp.views.adapters.UserMembershipAdapter
import com.example.familyapp.views.AddUserDialogFragment

class ManageFamilyFragment : Fragment() {

    private lateinit var familyRecyclerView: RecyclerView
    private lateinit var userMembershipAdapter: UserMembershipAdapter
    private val currentUserIdFamily = SessionManager.currentUser!!.idFamille ?: -1

    private val viewModel: UserViewModel by viewModels {
        UserViewModelFactory(
            UserRepository(this.requireContext()),
            fragment = this
        )
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_manage_family, container, false)



        familyRecyclerView = view.findViewById(R.id.family_recycler_view)
        userMembershipAdapter = UserMembershipAdapter(mutableListOf(),
            onDeleteUser = { userId ->
                viewModel.deleteUser(userId,currentUserIdFamily)
            },
            onEditUser = { user ->
                showUpdateUserDialog(user)
            }
        )
        familyRecyclerView.layoutManager = LinearLayoutManager(context)
        familyRecyclerView.adapter = userMembershipAdapter

        viewModel.users.observe(viewLifecycleOwner) { users ->
            userMembershipAdapter.updateData(users)
        }


        familyRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        familyRecyclerView.adapter = userMembershipAdapter

        viewModel.users.observe(viewLifecycleOwner) { users ->
            userMembershipAdapter.updateData(users)
        }

        viewModel.fetchUsers(currentUserIdFamily)

        val addButton = view.findViewById<Button>(R.id.add_member_button)
        addButton.setOnClickListener {
            val dialogFragment = AddUserDialogFragment { newUserRequest ->
                viewModel.addUser(newUserRequest)
            }
            dialogFragment.show(parentFragmentManager, "AddUserDialog")
        }


        val searchToggleButton = view.findViewById<Button>(R.id.search_toggle_button)
        val searchView = view.findViewById<SearchView>(R.id.search_view)

        searchToggleButton.setOnClickListener {
            if (searchView.visibility == View.GONE) {
                searchView.visibility = View.VISIBLE
                searchToggleButton.visibility = View.GONE
            }
        }

        searchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                searchView.visibility = View.GONE
                searchToggleButton.visibility = View.VISIBLE
            }
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                userMembershipAdapter.filter(newText ?: "")
                return true
            }
        })
        setupObservers()

        return view
    }
    private fun setupObservers() {
        viewModel.errorMessages.observe(viewLifecycleOwner){ errorMsg ->
            Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show()
        }

        viewModel.users.observe(viewLifecycleOwner){ users ->
            userMembershipAdapter.updateData(users)
        }
    }
    private fun showUpdateUserDialog(user: User) {
        val dialogFragment = UpdateUserDialogFragment(user) { updateUserRequest ->
            viewModel.updateUser(
                user.id, updateUserRequest,
            )
        }
        dialogFragment.show(parentFragmentManager, "UpdateUserDialog")
    }
}
