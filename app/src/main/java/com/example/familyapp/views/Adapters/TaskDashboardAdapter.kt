package com.example.familyapp.views.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.familyapp.R
import com.example.familyapp.data.model.user.TaskDashbord
import com.example.familyapp.views.Holders.TaskDashboardHolder

class TaskDashboardAdapter(private val taskList: List<TaskDashbord>) : RecyclerView.Adapter<TaskDashboardHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskDashboardHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_dashboard_tsk_progress, parent, false)
        return TaskDashboardHolder(view)
    }

    override fun getItemCount(): Int = taskList.size

    override fun onBindViewHolder(holder: TaskDashboardHolder, position: Int) {
        val task = taskList[position]
        holder.taskTitle.text = task.taskTitle

    }
}
