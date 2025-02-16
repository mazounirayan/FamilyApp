package com.example.familyapp.views.fragments.Conversation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.familyapp.data.model.conversation.Conversation
import com.example.familyapp.databinding.FragmentConversationsBinding
import com.example.familyapp.views.Adapters.ConversationsAdapter

class ConversationsFragment : Fragment() {
    private lateinit var binding: FragmentConversationsBinding
    private lateinit var conversationsAdapter: ConversationsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentConversationsBinding.inflate(inflater, container, false)
        conversationsAdapter = ConversationsAdapter(emptyList())
        binding.conversationsList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = conversationsAdapter
        }
        loadConversations()
        return binding.root
    }

    private fun loadConversations() {

        val sampleConversations = listOf(
            Conversation("1", "Alice", "Hey, how are you?", "2m ago", ""),
            Conversation("2", "Bob", "Meeting tomorrow?", "5m ago", "")
        )
        conversationsAdapter.updateConversations(sampleConversations)
    }
}
