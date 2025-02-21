package com.example.familyapp.views.Holders

import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.familyapp.R

class TaskDashboardHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val taskTitle: TextView = itemView.findViewById(R.id.task_title)
   // val progressBar: ProgressBar = itemView.findViewById(R.id.circular_progress)
    val progressText: TextView = itemView.findViewById(R.id.tv_progress_percentage)
}