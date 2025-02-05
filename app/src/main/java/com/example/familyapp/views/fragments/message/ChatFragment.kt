package com.example.familyapp.views.fragments.message

import android.annotation.SuppressLint
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

        sendButton.setOnClickListener {
            val content = messageInput.text.toString().trim()
            if (content.isNotEmpty()) {
                sendMessage(content)
                messageInput.text.clear()
            }
        }

        // Chargez les messages initiaux
        loadInitialMessages()
    }

    private fun sendMessage(content: String) {
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
            id = System.currentTimeMillis().toString(),
            contenu = content,
            date_envoie = getCurrentTimestamp(),
            isVue = false,
            idUser = userId,
            idChat = chatId
        )

        messages.add(newMessage)
        chatAdapter.notifyItemInserted(messages.size - 1)
        recyclerView.scrollToPosition(messages.size - 1)
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun loadInitialMessages() {
        // Simuler des messages initiaux (remplacez par un appel API ou une base de données)
        messages.add(Message("1", "Bonjour !", "2023-10-01 10:00", true, 2, chatId))
        messages.add(Message("2", "Salut ! Comment ça va ?", "2023-10-01 10:01", true, currentUserId, chatId))

        chatAdapter.notifyDataSetChanged()
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
}
