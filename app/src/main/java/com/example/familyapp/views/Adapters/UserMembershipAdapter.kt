package com.example.familyapp.views.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.familyapp.R
import com.example.familyapp.data.model.user.User
 import com.example.familyapp.views.Holders.UserMembershipHolder
import com.google.android.material.card.MaterialCardView


class UserMembershipAdapter(
    private var members: MutableList<User>,
    private val onDeleteUser: (Int) -> Unit
) : RecyclerView.Adapter<UserMembershipHolder>() {

    private var filteredMembers: MutableList<User> = ArrayList(members)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserMembershipHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_family_member, parent, false)
        return UserMembershipHolder(view)
    }

    override fun onBindViewHolder(holder: UserMembershipHolder, position: Int) {
        val member = filteredMembers[position]
        holder.userName.text = member.nom
        holder.userRole.text = member.role

        holder.itemView.findViewById<MaterialCardView>(R.id.delete_button).setOnClickListener {
            onDeleteUser(member.id)
        }
    }

    override fun getItemCount(): Int = filteredMembers.size

    fun updateData(newMembers: List<User>) {
        members.clear()
        members.addAll(newMembers)
        filteredMembers = ArrayList(newMembers)
        notifyDataSetChanged()
    }

    fun filter(query: String) {
        filteredMembers = if (query.isEmpty()) {
            ArrayList(members)
        } else {
            members.filter {
                it.nom.contains(query, ignoreCase = true) ||
                        it.prenom.contains(query, ignoreCase = true) ||
                        it.role.contains(query, ignoreCase = true) ||
                        it.email.contains(query, ignoreCase = true)
            }.toMutableList()
        }
        notifyDataSetChanged()
    }
}

