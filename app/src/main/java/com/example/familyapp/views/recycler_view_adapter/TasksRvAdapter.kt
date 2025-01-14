package com.example.familyapp.views.recycler_view_adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.familyapp.R
import com.example.familyapp.data.model.task.Task
import com.example.familyapp.views.viewholders.TasksRvViewHolder

class TasksRvAdapter(val tasks: List<Task>): RecyclerView.Adapter<TasksRvViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksRvViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_rv_cell, parent, false)

        return TasksRvViewHolder(view)
    }

    override fun getItemCount(): Int {
        return tasks.size

    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onBindViewHolder(holder: TasksRvViewHolder, position: Int) {
        val taskData = this.tasks[position]

        //holder.postUserInfoCardView.setRenderEffect(RenderEffect.createBlurEffect(20f, 20f, Shader.TileMode.DECAL))
        // Binder le VH et le model
        holder.taskName.text = taskData.nom
    }

}