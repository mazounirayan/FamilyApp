package com.example.familyapp.views.holders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.familyapp.R
class UserMembershipHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val userName: TextView = itemView.findViewById(R.id.username)
    val userRole: TextView = itemView.findViewById(R.id.user_role)
    val userEmail: TextView = itemView.findViewById(R.id.user_email)
    val userPoints: TextView = itemView.findViewById(R.id.user_points)
}

