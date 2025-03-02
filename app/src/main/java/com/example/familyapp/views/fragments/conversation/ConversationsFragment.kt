package com.example.familyapp.views.fragments.conversation

import ConversationsViewModel
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.familyapp.MainActivity
import com.example.familyapp.R
import com.example.familyapp.data.model.conversation.Conversation
import com.example.familyapp.databinding.FragmentConversationsBinding
import com.example.familyapp.repositories.ConversationRepository
import com.example.familyapp.utils.SessionManager
import com.example.familyapp.viewmodel.factories.ConversationsViewModelFactory
import com.example.familyapp.views.adapters.ConversationsAdapter
import java.text.SimpleDateFormat
import java.text.ParseException
import java.util.*

class ConversationsFragment : Fragment() {
    private lateinit var binding: FragmentConversationsBinding
    private val viewModel: ConversationsViewModel by viewModels {
        ConversationsViewModelFactory(ConversationRepository(this.requireContext()))
    }
    private lateinit var adapter: ConversationsAdapter
    private var allConversations = mutableListOf<Conversation>()
    private val currentUserId = SessionManager.currentUser!!.id

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {


        binding = FragmentConversationsBinding.inflate(inflater, container, false)

        adapter = ConversationsAdapter(mutableListOf(), { conversation ->
            val chatFragment = ChatFragment.newInstance(conversation.id)
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, chatFragment)
                .addToBackStack(null)
                .commit()
        }, requireContext())


        val itemTouchHelper = ItemTouchHelper(SwipeToDeleteChatCallback(adapter, viewModel, SessionManager.currentUser!!.id, viewLifecycleOwner))

        itemTouchHelper.attachToRecyclerView(binding.conversationsList)

        binding.conversationsList.adapter = adapter
        binding.conversationsList.layoutManager = LinearLayoutManager(context)

        viewModel.conversations.observe(viewLifecycleOwner){ conversations ->
            allConversations = conversations
                .sortedByDescending { conversation -> parseDate(conversation.messageTime) }
                .toMutableList()

            adapter.updateConversations(allConversations)
        }


        binding.searchField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                filterConversations(s.toString())
            }
        })


        binding.btnCreatePrivateChat.setOnClickListener {
            openSelectUsersFragment(group = 0)
        }


        binding.btnCreateGroupChat.setOnClickListener {
            openSelectUsersFragment(group = 1)
        }

        viewModel.loadConversations(currentUserId)
        return binding.root
    }



    private fun openSelectUsersFragment(group: Int) {
        val selectUsersFragment = SelectUsersFragment.newInstance(group)
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, selectUsersFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun filterConversations(query: String) {
        val filteredList = allConversations.filter {
            it.name.contains(query, ignoreCase = true)
        }
        adapter.updateConversations(filteredList)
    }


    override fun onResume() {
        super.onResume()
        SessionManager.isChatActive = true
        (activity as? MainActivity)?.removeNotificationBadge()
    }

    override fun onPause() {
        super.onPause()
        SessionManager.isChatActive = false
    }

    private fun parseDate(dateString: String?): Long {
        if (dateString.isNullOrEmpty()) return 0L

        return try {
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            sdf.timeZone = TimeZone.getTimeZone("UTC")
            sdf.parse(dateString)?.time ?: 0L
        } catch (e: ParseException) {
            0L
        }
    }

}