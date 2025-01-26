package com.example.familyapp.views.fragments.message

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.familyapp.R
import com.example.familyapp.data.model.message.Message
import com.example.familyapp.views.Adapters.ChatAdapter


class ChatFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var chatAdapter: ChatAdapter
    private val messages = mutableListOf<Message>()
    private val currentUserId = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chat, container, false)

        recyclerView = view.findViewById(R.id.recycler_gchat)
        chatAdapter = ChatAdapter(messages, currentUserId)

        recyclerView.adapter = chatAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext()).apply {
            stackFromEnd = true // Les nouveaux messages apparaissent en bas
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadInitialMessages()

        val sendButton = view.findViewById<Button>(R.id.button_gchat_send)
        val messageInput = view.findViewById<EditText>(R.id.edit_gchat_message)

        sendButton.setOnClickListener {
            val content = messageInput.text.toString().trim()
            if (content.isNotEmpty()) {
                sendMessage(content)
                messageInput.text.clear()
            }
        }
    }

    private fun loadInitialMessages() {
        // Simuler des messages initiaux (remplacez par un appel API ou une base de données)
        messages.add(Message("1", "Bonjour !", "2023-10-01 10:00", true, 2, 1))
        messages.add(Message("2", "Salut ! Comment ça va ?", "2023-10-01 10:01", true, currentUserId, 1))

        chatAdapter.notifyDataSetChanged()
    }

    private fun sendMessage(content: String) {
        // Ajouter un nouveau message envoyé par l'utilisateur actuel à la liste
        val newMessage = Message(
            id = System.currentTimeMillis().toString(), // ID unique basé sur le timestamp
            contenu = content,
            date_envoie = "2023-10-01 10:02", // Remplacez par la date actuelle dynamique
            isVue = false,
            idUser = currentUserId,
            idChat = 1
        )

        messages.add(newMessage)

        chatAdapter.notifyItemInserted(messages.size - 1) // Notifier l'ajout d'un nouvel élément
        recyclerView.scrollToPosition(messages.size - 1) // Faire défiler vers le bas

    }
}
