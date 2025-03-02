package com.example.familyapp.views.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.familyapp.R
import com.example.familyapp.data.model.conversation.Conversation
import com.example.familyapp.views.holders.ConversationViewHolder

class ConversationsAdapter(
    private var conversations: MutableList<Conversation> = mutableListOf(),
    private val onConversationClick: (Conversation) -> Unit,
    val context: Context
) : RecyclerView.Adapter<ConversationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConversationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_conversation, parent, false)
        return ConversationViewHolder(view)
    }

    override fun onBindViewHolder(holder: ConversationViewHolder, position: Int) {
        val conversation = conversations[position]
        holder.bind(conversation)

         holder.itemView.setOnClickListener {
            onConversationClick(conversation)
        }
    }

    override fun getItemCount(): Int = conversations.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateConversations(newConversations: List<Conversation>) {
        conversations.clear()
        conversations.addAll(newConversations)
        notifyDataSetChanged()
    }

    fun deleteItem(position: Int) {
        conversations.removeAt(position)
        notifyItemRemoved(position)
    }

    fun getConversationAt(position: Int): Conversation {
        return conversations[position]
    }

}
