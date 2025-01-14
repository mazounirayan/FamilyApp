package com.example.familyapp.views.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.familyapp.R
import com.example.familyapp.data.model.user.User
 import com.example.familyapp.views.Holders.UserMembershipHolder

class UserMembershipAdapter(
    private val members: List<User>
) : RecyclerView.Adapter<UserMembershipHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserMembershipHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_family_member, parent, false)
        return UserMembershipHolder(view)
    }

    override fun onBindViewHolder(holder: UserMembershipHolder, position: Int) {
        val member = members[position]
        holder.userName.text = member.nom
        holder.userRole.text = member.role
    }

    override fun getItemCount(): Int = members.size
}
