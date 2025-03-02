package com.example.familyapp.views.fragments.dashbord

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.familyapp.R
import com.example.familyapp.data.model.task.TaskStatus

class TaskStatusAdapter(
    private val tasks: List<TaskStatus>
) : RecyclerView.Adapter<TaskStatusAdapter.TaskStatusViewHolder>() {

    // ViewHolder pour lier les vues
    class TaskStatusViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val taskStatusTitle: TextView = view.findViewById(R.id.task_status_title)
        val progressPercentage: TextView = view.findViewById(R.id.tv_progress_percentage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskStatusViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task_status, parent, false)
        return TaskStatusViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskStatusViewHolder, position: Int) {
        val task = tasks[position]

        // Lier les données
        holder.taskStatusTitle.text = task.taskName
        holder.progressPercentage.text = "${task.score}%"
    }

    override fun getItemCount(): Int = tasks.size
}

