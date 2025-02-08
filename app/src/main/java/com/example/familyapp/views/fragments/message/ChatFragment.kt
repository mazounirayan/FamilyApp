package com.example.familyapp.views.fragments.message

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.familyapp.R
import com.example.familyapp.data.model.message.Message
import com.example.familyapp.network.dto.messageDto.MessageDto
import com.example.familyapp.repositories.MessageRepository
import com.example.familyapp.viewmodel.MessageViewModel
import com.example.familyapp.viewmodel.factories.MessageViewModelFactory
import com.example.familyapp.views.Adapters.ChatAdapter
import com.example.familyapp.websocket.SocketIOClient
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChatFragment : Fragment() {

    private val currentUserId = 1 // ID de l'utilisateur actuel
    private val chatId = 1 // ID du chat (constante)

    private lateinit var webSocketClient: SocketIOClient
    private lateinit var recyclerView: RecyclerView
    private lateinit var chatAdapter: ChatAdapter
    private val messages = mutableListOf<Message>()

    private val messageViewModel: MessageViewModel by viewModels {
        MessageViewModelFactory(MessageRepository(requireContext()),this)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chat, container, false)

        // Initialisation du RecyclerView et de l'adaptateur
        recyclerView = view.findViewById(R.id.recycler_gchat)
        chatAdapter = ChatAdapter(messages, currentUserId)
        recyclerView.adapter = chatAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext()).apply {
            stackFromEnd = true // Les nouveaux messages apparaissent en bas
        }

        // Initialisation du client WebSocket
        webSocketClient = SocketIOClient(this)
        webSocketClient.connect(currentUserId)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialisez le RecyclerView et l'adaptateur
        recyclerView = view.findViewById(R.id.recycler_gchat)
        chatAdapter = ChatAdapter(messages, currentUserId)
        recyclerView.adapter = chatAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext()).apply {
            stackFromEnd = true // Les nouveaux messages apparaissent en bas
        }

        val sendButton = view.findViewById<Button>(R.id.button_gchat_send)
        val messageInput = view.findViewById<EditText>(R.id.edit_gchat_message)

        observeMessages()

        messageViewModel.fetchMessagesOfChat(chatId)

        sendButton.setOnClickListener {
            val content = messageInput.text.toString().trim()
            if (content.isNotEmpty()) {
                sendMessage(content)
                messageInput.text.clear()
            }
        }


    }

    private fun sendMessage(content: String) {

        val newMessageDto = MessageDto(
            contenu = content,
            idUser = currentUserId,
            idChat = chatId,
            dateEnvoie = getCurrentTimestamp(),
            isVue = false // Par défaut, le message n'est pas vu au moment de l'envoi
        )
        messageViewModel.addMessage(newMessageDto)

        addMessageToChat(content, currentUserId)
        webSocketClient.sendMessage(createMessageJson(content).toString())
    }


    fun messageReceived(content: String, userId: Int) {
        requireActivity().runOnUiThread {
            addMessageToChat(content, userId)
        }
    }

    /**
     * Ajoute un message à la liste et met à jour l'interface utilisateur.
     */
    private fun addMessageToChat(content: String, userId: Int) {
        if (!::chatAdapter.isInitialized) {
            println("Erreur : chatAdapter n'est pas encore initialisé")
            return
        }

        val newMessage = Message(
            idMessage = System.currentTimeMillis().toString(),
            contenu = content,
            dateEnvoie = getCurrentTimestamp(),
            isVue = false,
            idUser = userId,
            idChat = chatId
        )

        messages.add(newMessage)
        chatAdapter.notifyItemInserted(messages.size - 1)
        recyclerView.scrollToPosition(messages.size - 1)
    }


    private fun getCurrentTimestamp(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return dateFormat.format(Date())
    }

    private fun createMessageJson(content: String): JSONObject {
        return JSONObject().apply {
            put("familyId", chatId)
            put("senderId", currentUserId)
            put("content", content)
        }
    }

    private fun observeMessages() {
        messageViewModel.messages.observe(viewLifecycleOwner, Observer { newMessages ->
            messages.clear()
            messages.addAll(newMessages)
            chatAdapter.notifyDataSetChanged()
            recyclerView.scrollToPosition(messages.size - 1) // Scroller vers le dernier message
        })
    }
}
