package com.example.familyapp.views.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.familyapp.R
import com.example.familyapp.data.model.user.User
import com.example.familyapp.views.Holders.SelectUserViewHolder

class SelectUsersAdapter(
    private val isMultipleSelection: Int = 1,
    private val onUserSelected: (Int, Boolean) -> Unit
) : RecyclerView.Adapter<SelectUserViewHolder>() {

    private val users = mutableListOf<User>()
    private val selectedUserIds = mutableSetOf<Int>()

    fun submitList(userList: List<User>) {
        users.clear()
        users.addAll(userList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectUserViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user_selection, parent, false)
        return SelectUserViewHolder(view)
    }

    override fun onBindViewHolder(holder: SelectUserViewHolder, position: Int) {
        val user = users[position]
        val isSelected = selectedUserIds.contains(user.id)

        holder.bind(user, isSelected) { userId, isChecked ->
            if (isChecked) {
                if (isMultipleSelection==0) {
                    selectedUserIds.clear()
                    selectedUserIds.add(userId)
                    notifyDataSetChanged()
                } else {
                    selectedUserIds.add(userId)
                }
            } else {
                selectedUserIds.remove(userId)
            }
            onUserSelected(userId, isChecked)
        }
    }

    override fun getItemCount(): Int = users.size
}
