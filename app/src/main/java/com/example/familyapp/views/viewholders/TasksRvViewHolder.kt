package com.example.familyapp.views.viewholders

import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.familyapp.R


class TasksRvViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val taskName = itemView.findViewById<TextView>(R.id.task_name)
    val taskDetailsSection = itemView.findViewById<LinearLayout>(R.id.task_details_section)
    val taskDescription = itemView.findViewById<TextView>(R.id.task_description)
    val taskDueDate = itemView.findViewById<TextView>(R.id.task_due_date)
    val buttonDone = itemView.findViewById<Button>(R.id.button_done)
    val buttonInProgress = itemView.findViewById<Button>(R.id.button_in_progress)
    val buttonToDo = itemView.findViewById<Button>(R.id.button_to_do)
    val cardViewTask = itemView.findViewById<CardView>(R.id.card_view_task)
    val taskUnfoldButton = itemView.findViewById<ImageButton>(R.id.task_unfold_button)

}