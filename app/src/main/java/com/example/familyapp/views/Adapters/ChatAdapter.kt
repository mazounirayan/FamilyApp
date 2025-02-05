package com.example.familyapp.views.Adapters
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.familyapp.R
import com.example.familyapp.data.model.message.Message

class ChatAdapter(
    private val messages: List<Message>,
    private val currentUserId: Int
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_SENT = 1
        private const val VIEW_TYPE_RECEIVED = 2
    }

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].idUser == currentUserId) {
            VIEW_TYPE_SENT
        } else {
            VIEW_TYPE_RECEIVED
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_SENT) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_message_send, parent, false)
            SentMessageViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_message_received, parent, false)
            ReceivedMessageViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]
        if (holder is SentMessageViewHolder) {
            holder.bind(message)
        } else if (holder is ReceivedMessageViewHolder) {
            holder.bind(message)
        }
    }

    override fun getItemCount(): Int = messages.size

    // ViewHolder pour les messages envoyés (par l'utilisateur actuel)
    class SentMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val contenuText = itemView.findViewById<TextView>(R.id.text_gchat_message_me)
        private val dateText = itemView.findViewById<TextView>(R.id.text_gchat_timestamp_me)

        fun bind(message: Message) {
            contenuText.text = message.contenu
            dateText.text = message.date_envoie
        }
    }

    // ViewHolder pour les messages reçus (d'autres utilisateurs)
    class ReceivedMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val contenuText = itemView.findViewById<TextView>(R.id.text_gchat_message_other)
        private val dateText = itemView.findViewById<TextView>(R.id.text_gchat_timestamp_other)

        fun bind(message: Message) {
            contenuText.text = message.contenu
            dateText.text = message.date_envoie
        }
    }
}