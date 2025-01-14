package com.example.familyapp.views.Holders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.familyapp.R

class UserDashboardHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val userName: TextView = itemView.findViewById(R.id.username)
    val userScore: TextView = itemView.findViewById(R.id.user_score)
}
