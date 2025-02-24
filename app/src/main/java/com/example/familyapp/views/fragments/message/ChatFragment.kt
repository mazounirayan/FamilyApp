package com.example.familyapp.views.fragments.message

import android.os.Bundle
import android.se.omapi.Session
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
import com.example.familyapp.data.model.user.UserMessage
import com.example.familyapp.network.dto.messageDto.NewMessageDto
import com.example.familyapp.repositories.MessageRepository
import com.example.familyapp.utils.SessionManager
import com.example.familyapp.viewmodel.MessageViewModel
import com.example.familyapp.viewmodel.factories.MessageViewModelFactory
import com.example.familyapp.views.Adapters.ChatAdapter
import com.example.familyapp.websocket.SocketIOClient
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChatFragment : Fragment() {

    private val chatId: Int by lazy { arguments?.getInt("chatId") ?: -1 }
    private val currentUserId = SessionManager.currentUser!!.id

    private lateinit var webSocketClient: SocketIOClient
    private lateinit var recyclerView: RecyclerView
    private lateinit var chatAdapter: ChatAdapter
    private val messages = mutableListOf<Message>()
    private val nom = SessionManager.currentUser!!.nom
    private val prenom = SessionManager.currentUser!!.prenom

    private val messageViewModel: MessageViewModel by viewModels {
        MessageViewModelFactory(MessageRepository(requireContext()), this)
    }

    companion object {
        fun newInstance(chatId: Int): ChatFragment {
            val fragment = ChatFragment()
            val bundle = Bundle()
            bundle.putInt("chatId", chatId)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chat, container, false)

        recyclerView = view.findViewById(R.id.recycler_gchat)
        chatAdapter = ChatAdapter(messages, currentUserId)
        recyclerView.adapter = chatAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext()).apply {
            stackFromEnd = true
        }

        webSocketClient = SocketIOClient(this)
        webSocketClient.connect(currentUserId)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
        val newMessageDto = NewMessageDto(
            contenu = content,
            idUser = currentUserId,
            idChat = chatId,
            dateEnvoie = getCurrentTimestamp(),
            isVue = false
        )
        messageViewModel.addMessage(newMessageDto)
        addMessageToChat(content, currentUserId, nom, prenom)
        webSocketClient.sendMessage(createMessageJson(content).toString())
    }

    fun messageReceived(content: String, userId: Int, nom: String, prenom: String) {
        requireActivity().runOnUiThread {
            addMessageToChat(content, userId, nom, prenom)
        }
    }

    private fun addMessageToChat(content: String, userId: Int, nom: String, prenom: String) {
        if (!::chatAdapter.isInitialized) {
            println("Erreur : chatAdapter n'est pas encore initialisé")
            return
        }

        val newMessage = Message(
            idMessage = System.currentTimeMillis().toString(),
            contenu = content,
            dateEnvoie = getCurrentTimestamp(),
            isVue = false,
            user = UserMessage(userId, nom, prenom),
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
            put("senderNom", nom)
            put("senderPrenom", prenom)
            put("content", content)
        }
    }

    private fun observeMessages() {
        messageViewModel.messages.observe(viewLifecycleOwner, Observer { newMessages ->
            messages.clear()
            messages.addAll(newMessages)
            chatAdapter.notifyDataSetChanged()
            recyclerView.scrollToPosition(messages.size - 1)
        })
    }
}
