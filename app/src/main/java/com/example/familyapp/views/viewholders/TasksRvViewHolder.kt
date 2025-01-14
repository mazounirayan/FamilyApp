package com.example.familyapp.views.viewholders

import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.familyapp.R

class TasksRvViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    val taskName: TextView = itemView.findViewById(R.id.task_name)
    //val taskConfirmButton: ImageButton = itemView.findViewById(R.id.task_confirm_button)
    //val taskUnfoldButton: TextView = itemView.findViewById(R.id.task_unfold_button)

}