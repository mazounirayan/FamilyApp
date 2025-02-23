package com.example.familyapp.views.fragments.Conversation

import ConversationsViewModel
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.familyapp.data.model.conversation.Conversation
import com.example.familyapp.databinding.FragmentConversationsBinding
import com.example.familyapp.repositories.ConversationRepository
import com.example.familyapp.viewmodel.UserViewModel
import com.example.familyapp.viewmodel.factories.ConversationsViewModelFactory
import com.example.familyapp.views.Adapters.ConversationsAdapter

class ConversationsFragment : Fragment() {
    private lateinit var binding: FragmentConversationsBinding
    private val viewModel: ConversationsViewModel by viewModels {
        ConversationsViewModelFactory(ConversationRepository(this.requireContext()))
    }
    private lateinit var adapter: ConversationsAdapter
    private var allConversations = mutableListOf<Conversation>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentConversationsBinding.inflate(inflater, container, false)
        adapter = ConversationsAdapter(mutableListOf())
        binding.conversationsList.adapter = adapter
        binding.conversationsList.layoutManager = LinearLayoutManager(context)

        viewModel.conversations.observe(viewLifecycleOwner, Observer { conversations ->
            allConversations = conversations.toMutableList()
            adapter.updateConversations(conversations)
        })

        binding.searchField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                filterConversations(s.toString())
            }
        })

        viewModel.loadConversations(1)
        return binding.root
    }

    private fun filterConversations(query: String) {
        val filteredList = allConversations.filter {
            it.name.contains(query, ignoreCase = true)
        }
        adapter.updateConversations(filteredList)
    }
}
