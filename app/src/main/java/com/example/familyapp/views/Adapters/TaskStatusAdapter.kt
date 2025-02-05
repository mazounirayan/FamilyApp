package com.example.familyapp.views.fragments.dashbord

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.familyapp.R

class TaskStatusAdapter(private val statusList: List<Pair<String, Int>>) :
    RecyclerView.Adapter<TaskStatusAdapter.TaskStatusViewHolder>() {

    class TaskStatusViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.task_status_title)
        val progressCircle: ProgressBar = itemView.findViewById(R.id.tv_progress_percentage)
        val percentage: TextView = itemView.findViewById(R.id.task_title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskStatusViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task_status, parent, false)
        return TaskStatusViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskStatusViewHolder, position: Int) {
        val (statusTitle, progress) = statusList[position]
        holder.title.text = statusTitle
        holder.progressCircle.progress = progress
        holder.percentage.text = "$progress%"
    }

    override fun getItemCount(): Int = statusList.size
}
