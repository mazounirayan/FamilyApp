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
    val taskName: TextView = itemView.findViewById(R.id.task_name)
    val taskDetailsSection: LinearLayout = itemView.findViewById(R.id.task_details_section)
    val taskDescription: TextView = itemView.findViewById(R.id.task_description)
    val taskDueDate: TextView = itemView.findViewById(R.id.task_due_date)
    val buttonDone: Button = itemView.findViewById(R.id.button_done)
    val buttonInProgress: Button = itemView.findViewById(R.id.button_in_progress)
    val buttonToDo: Button = itemView.findViewById(R.id.button_to_do)
    val cardViewTask: CardView = itemView.findViewById(R.id.card_view_task)
    val taskUnfoldButton: ImageButton= itemView.findViewById(R.id.task_unfold_button)

}