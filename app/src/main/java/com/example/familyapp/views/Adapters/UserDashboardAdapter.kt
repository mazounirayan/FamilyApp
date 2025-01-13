package com.example.familyapp.views.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.familyapp.R
import com.example.familyapp.data.model.user.User

class UserDashboardAdapter(private val userList: List<User>) : RecyclerView.Adapter<UserDashboardAdapter.UserViewHolder>() {

    // ViewHolder pour afficher le nom et le score
    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userName: TextView = itemView.findViewById(R.id.username)
        val userScore: TextView = itemView.findViewById(R.id.user_score)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_dashboard_view_holder, parent, false)
        return UserViewHolder(view)
    }

    override fun getItemCount(): Int = userList.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]
        holder.userName.text = user.nom
        holder.userScore.text = "Score: ${position * 10}"
    }
}
