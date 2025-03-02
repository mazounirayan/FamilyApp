package com.example.familyapp.views.fragments.conversation
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
import com.example.familyapp.data.model.chat.CreateChat
import com.example.familyapp.data.model.user.User
import com.example.familyapp.repositories.ConversationRepository
import com.example.familyapp.utils.SessionManager
import com.example.familyapp.viewmodel.UserViewModel
import com.example.familyapp.viewmodel.factories.ConversationsViewModelFactory
import com.example.familyapp.views.adapters.SelectUsersAdapter
import com.example.familyapp.viewmodel.factories.UserViewModelFactory


class SelectUsersFragment : Fragment() {

    private lateinit var usersAdapter: SelectUsersAdapter
    private val selectedUserIds = mutableListOf<Int>()
    private var isGroupCreation: Int = 0
    private var idChat: Int = 0


    private val userViewModel: UserViewModel by viewModels {
        UserViewModelFactory(UserRepository(requireContext()), this)
    }

    private val conversationViewModel: ConversationsViewModel by viewModels {
        ConversationsViewModelFactory(ConversationRepository(requireContext()))
    }


    companion object {
        fun newInstance(isGroup: Int, idChat: Int? = null) = SelectUsersFragment().apply {
            arguments = Bundle().apply {
                putInt("isGroupCreation", isGroup)
                idChat?.let { putInt("idChat", it) }
            }
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_select_users, container, false)
        isGroupCreation = arguments?.getInt("isGroupCreation") ?: 0
        idChat = arguments?.getInt("idChat") ?: 0


        Log.d(TAG, "Mode création: ${if (isGroupCreation == 1) "groupe" else if(isGroupCreation==0) "chat privé" else "add member" }")

        val etConversationName = view.findViewById<EditText>(R.id.etConversationName)
        val recyclerView = view.findViewById<RecyclerView>(R.id.usersRecyclerView)
        val confirmBtn = view.findViewById<Button>(R.id.btnConfirmSelection)
        val title = view.findViewById<TextView>(R.id.tvTitle)

        if(isGroupCreation == 2){
            etConversationName.visibility = View.GONE
        }

        title.text = if (isGroupCreation == 1) "Créer un groupe" else if(isGroupCreation==0) "Créer un chat privé" else "Ajouter un membre"

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


            if (conversationName.isEmpty() && (isGroupCreation == 1 || isGroupCreation == 0) ) {
                Toast.makeText(requireContext(), "Le nom est requis", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (selectedUserIds.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    if (isGroupCreation == 1) "Sélectionnez au moins un utilisateur"
                    else "Sélectionnez exactement un utilisateur",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val participants = selectedUserIds.toMutableList().apply {
                add(SessionManager.currentUser!!.id)
            }
            Log.d(TAG, "Liste finale des participants: $participants")

            if(isGroupCreation == 0 || isGroupCreation == 1){
                conversationViewModel.createChat(conversationName, participants) { chat ->
                    if (chat != null) {
                        Log.d(TAG, " Conversation créée avec succès. ID: ${chat.idChat}")
                        parentFragmentManager.popBackStack()

                    } else {
                        Log.e(TAG, " Échec de la création de la conversation")
                        Toast.makeText(requireContext(), "Erreur lors de la création du chat", Toast.LENGTH_SHORT).show()
                    }
                }
            }else{

                for(participant in participants){
                    conversationViewModel.addChat(CreateChat(participant,idChat))
                }

            }

        }

        return view
    }


    private var currentChatUserIds: List<Int>? = null
    private var currentUsers: List<User>? = null

    private fun loadUsers() {
        val idFamille = SessionManager.currentUser!!.idFamille
        if (idFamille == -1) {
            Toast.makeText(requireContext(), "Aucun ID famille trouvé.", Toast.LENGTH_SHORT).show()
            return
        }

        val isGroupCreation = arguments?.getInt("isGroupCreation") ?: 0

        if (isGroupCreation == 2) {
            conversationViewModel.usersOfChat.observe(viewLifecycleOwner) { chatUserIds ->
                currentChatUserIds = chatUserIds
                updateUserList(isGroupCreation)
            }
            conversationViewModel.fetchUsersOfChat(idChat)
        }

        userViewModel.users.observe(viewLifecycleOwner) { users ->
            currentUsers = users.filter { it.id != SessionManager.currentUser!!.id }
            updateUserList(isGroupCreation)
        }

        if(idFamille != null){
            userViewModel.fetchUsers(idFamille)
        }
    }

    private fun updateUserList(isGroupCreation: Int) {
        if (isGroupCreation == 2) {
            if (currentUsers == null || currentChatUserIds == null) return

            val filteredUsers = currentUsers!!.filter { user -> user.id !in currentChatUserIds!! }
            usersAdapter.submitList(filteredUsers)
        } else {
            currentUsers?.let { usersAdapter.submitList(it) }
        }
    }

}
