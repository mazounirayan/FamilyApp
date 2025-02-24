package com.example.familyapp.views.fragments.Conversation
import ConversationsViewModel
import UserRepository
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.familyapp.R
import com.example.familyapp.data.model.user.User
import com.example.familyapp.repositories.ConversationRepository
import com.example.familyapp.utils.SessionManager
import com.example.familyapp.viewmodel.UserViewModel
import com.example.familyapp.viewmodel.factories.ConversationsViewModelFactory
import com.example.familyapp.views.Adapters.SelectUsersAdapter
import com.example.familyapp.views.fragments.message.ChatFragment
import com.example.familyapp.viewmodel.factories.UserViewModelFactory


class SelectUsersFragment : Fragment() {

    private lateinit var usersAdapter: SelectUsersAdapter
    private val selectedUserIds = mutableListOf<Int>()
    private var isGroupCreation: Boolean = false

     private val userViewModel: UserViewModel by viewModels {
        UserViewModelFactory(UserRepository(requireContext()), this)
    }

    private val conversationViewModel: ConversationsViewModel by viewModels {
        ConversationsViewModelFactory(ConversationRepository(requireContext()))
    }

    private val currentUserIdFamille = SessionManager.currentUser!!.idFamille ?: -1

    companion object {
        fun newInstance(isGroup: Boolean) = SelectUsersFragment().apply {
            arguments = Bundle().apply { putBoolean("isGroupCreation", isGroup) }
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_select_users, container, false)
        isGroupCreation = arguments?.getBoolean("isGroupCreation") ?: false

        Log.d(TAG, "Mode création: ${if (isGroupCreation) "groupe" else "chat privé"}")

        val etConversationName = view.findViewById<EditText>(R.id.etConversationName)
        val recyclerView = view.findViewById<RecyclerView>(R.id.usersRecyclerView)
        val confirmBtn = view.findViewById<Button>(R.id.btnConfirmSelection)
        val title = view.findViewById<TextView>(R.id.tvTitle)

        title.text = if (isGroupCreation) "Créer un groupe" else "Créer un chat privé"

        usersAdapter = SelectUsersAdapter(isGroupCreation) { userId, isSelected ->
            if (isSelected) {
                selectedUserIds.add(userId)
                Log.d(TAG, "Utilisateur ajouté: $userId")
            } else {
                selectedUserIds.remove(userId)
                Log.d(TAG, "Utilisateur retiré: $userId")
            }
            Log.d(TAG, "Liste des utilisateurs sélectionnés: $selectedUserIds")
        }

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = usersAdapter

        loadUsers()

        confirmBtn.setOnClickListener {
            val conversationName = etConversationName.text.toString().trim()


            if (conversationName.isEmpty()) {
                Toast.makeText(requireContext(), "Le nom est requis", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (selectedUserIds.isEmpty() || (!isGroupCreation && selectedUserIds.size > 1)) {
                Toast.makeText(
                    requireContext(),
                    if (isGroupCreation) "Sélectionnez au moins un utilisateur"
                    else "Sélectionnez exactement un utilisateur",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val participants = selectedUserIds.toMutableList().apply {
                add(SessionManager.currentUser!!.id)
            }
            Log.d(TAG, "Liste finale des participants: $participants")


            conversationViewModel.createChat(conversationName, participants) { chat ->
                if (chat != null) {
                    Log.d(TAG, " Conversation créée avec succès. ID: ${chat.idChat}")
                    parentFragmentManager.popBackStack()

                } else {
                    Log.e(TAG, " Échec de la création de la conversation")
                    Toast.makeText(requireContext(), "Erreur lors de la création du chat", Toast.LENGTH_SHORT).show()
                }
            }
        }

        return view
    }


    private fun loadUsers() {
        val idFamille = 1
        if (idFamille == -1) {
            Toast.makeText(requireContext(), "Aucun ID famille trouvé.", Toast.LENGTH_SHORT).show()
            return
        }

        userViewModel.users.observe(viewLifecycleOwner) { users ->
            val filteredUsers = users.filter { it.id != SessionManager.currentUser!!.id }
            usersAdapter.submitList(filteredUsers)
        }
        userViewModel.fetchUsers(idFamille)
    }

}
