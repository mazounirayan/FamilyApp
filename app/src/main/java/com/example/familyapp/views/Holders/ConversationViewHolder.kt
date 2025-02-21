package com.example.familyapp.views.Holders

import android.text.format.DateUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.familyapp.R
import com.example.familyapp.data.model.conversation.Conversation
import java.text.SimpleDateFormat
import java.util.*

class ConversationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val contactName: TextView = itemView.findViewById(R.id.contact_name)
    private val lastMessage: TextView = itemView.findViewById(R.id.last_message)
    private val messageTime: TextView = itemView.findViewById(R.id.message_time)
    private val profileImage: ImageView = itemView.findViewById(R.id.profile_image)

    fun bind(conversation: Conversation) {
        contactName.text = conversation.name
        lastMessage.text = conversation.lastMessage

        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        val date = sdf.parse(conversation.messageTime) ?: Date()
        messageTime.text = DateUtils.getRelativeTimeSpanString(date.time, System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS)

        Glide.with(itemView.context)
            .load(conversation.profileImage)
            .placeholder(R.drawable.baseline_account_circle_24)
            .into(profileImage)
    }
}