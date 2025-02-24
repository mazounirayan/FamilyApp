package com.example.familyapp.views.Holders

import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.familyapp.R
import com.example.familyapp.data.model.user.User

class SelectUserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val tvUserName: TextView = itemView.findViewById(R.id.tvUserName)
    private val cbSelectUser: CheckBox = itemView.findViewById(R.id.cbSelectUser)
    private val ivProfileImage: ImageView = itemView.findViewById(R.id.ivProfileImage)

    fun bind(user: User, isSelected: Boolean, onUserSelected: (Int, Boolean) -> Unit) {
        tvUserName.text = "${user.prenom} ${user.nom}"
        cbSelectUser.isChecked = isSelected

        cbSelectUser.setOnCheckedChangeListener(null)
        cbSelectUser.setOnCheckedChangeListener { _, isChecked ->
            onUserSelected(user.id, isChecked)
        }

        itemView.setOnClickListener {
            cbSelectUser.isChecked = !cbSelectUser.isChecked
        }
    }
}

