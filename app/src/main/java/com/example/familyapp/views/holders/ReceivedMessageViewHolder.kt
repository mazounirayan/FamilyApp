package com.example.familyapp.views.holders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.familyapp.R
import com.example.familyapp.data.model.message.Message
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class ReceivedMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val dateHeaderText: TextView = itemView.findViewById(R.id.text_gchat_date_other)
    private val messageText: TextView = itemView.findViewById(R.id.text_gchat_message_other)
    private val timestampText: TextView = itemView.findViewById(R.id.text_gchat_timestamp_other)
    private val senderName: TextView = itemView.findViewById(R.id.text_gchat_user_other)

    fun bind(message: Message, previousMessage: Message?) {
        messageText.text = message.contenu
        senderName.text = "${message.user.prenom} ${message.user.nom}"

        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }
        val outputTimeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val outputDateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

        val messageDate: Date? = try {
            inputFormat.parse(message.dateEnvoie)
        } catch (e: Exception) {
            null
        }
        timestampText.text = messageDate?.let { outputTimeFormat.format(it) } ?: message.dateEnvoie

        val currentMessageDateString = if (message.dateEnvoie.length >= 10)
            message.dateEnvoie.substring(0, 10)
        else message.dateEnvoie

        val previousMessageDateString = previousMessage?.dateEnvoie?.let {
            if (it.length >= 10) it.substring(0, 10) else it
        } ?: ""

        if (previousMessage == null || currentMessageDateString != previousMessageDateString) {
            dateHeaderText.visibility = View.VISIBLE
            val parsedDate: Date? = try {
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(currentMessageDateString)
            } catch (e: Exception) {
                null
            }
            dateHeaderText.text = parsedDate?.let { outputDateFormat.format(it) } ?: currentMessageDateString
        } else {
            dateHeaderText.visibility = View.GONE
        }
    }
}