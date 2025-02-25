package com.example.familyapp.views.recycler_view_adapter

import android.annotation.SuppressLint
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.familyapp.R
import com.example.familyapp.app_utils.TaskUpdateListener
import com.example.familyapp.data.model.task.StatusTache
import com.example.familyapp.data.model.task.Task
import com.example.familyapp.data.model.task.TaskUpdate
import com.example.familyapp.viewmodel.TaskViewModel
import com.example.familyapp.views.viewholders.TasksRvViewHolder


class TasksRvAdapter(
    private var tasks: List<Task>,
    private val taskViewModel: TaskViewModel,
    private val taskUpdateListener: TaskUpdateListener
) : RecyclerView.Adapter<TasksRvViewHolder>() {

    private val expandedPositions = mutableSetOf<Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksRvViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_rv_cell, parent, false)
        return TasksRvViewHolder(view)
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: TasksRvViewHolder, position: Int) {
        val taskData = tasks[position]

        holder.taskName.text = taskData.nom
        holder.taskNameUser.text = taskData.user.prenom + " " +taskData.user.nom
        holder.taskDescription.text = taskData.description
        holder.taskDueDate.text = "Date limite : ${taskData.dateFin}"
        holder.taskDetailsSection.visibility = if (expandedPositions.contains(position)) View.VISIBLE else View.GONE

        TransitionManager.beginDelayedTransition(holder.itemView as ViewGroup)

        when (taskData.status) {
            "A_FAIRE" -> {
                holder.cardViewTask.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.red_card))
                holder.taskUnfoldButton.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.red_card))
            }
            "EN_COURS" -> {
                holder.cardViewTask.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.yellow_card))
                holder.taskUnfoldButton.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.yellow_card))
            }
            "FINI" -> {
                holder.cardViewTask.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.primary_green))
                holder.taskUnfoldButton.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.primary_green))
            }
            else -> {
                holder.cardViewTask.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.grey_card))
                holder.taskUnfoldButton.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.grey_card))
            }
        }

        holder.itemView.setOnClickListener {
            if (expandedPositions.contains(position)) {
                expandedPositions.remove(position)
                notifyItemChanged(position)
            } else {
                expandedPositions.add(position)
                notifyItemChanged(position)
            }
        }

        holder.buttonDone.setOnClickListener {
            updateTaskStatus(taskData.idTache, StatusTache.FINI.name, position)
        }

        holder.buttonInProgress.setOnClickListener {
            updateTaskStatus(taskData.idTache, StatusTache.EN_COURS.name, position)
        }

        holder.buttonToDo.setOnClickListener {
            updateTaskStatus(taskData.idTache, StatusTache.A_FAIRE.name, position)
        }
    }

    private fun updateTaskStatus(id: Int, status: String, position: Int) {
        val taskUpdate = TaskUpdate(status)
        taskViewModel.patchTask(id, taskUpdate)
        tasks[position].status = status
        notifyItemChanged(position)
        taskUpdateListener.onTaskUpdated()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateTasks(newTasks: List<Task>) {
        this.tasks = newTasks
        notifyDataSetChanged()
    }
}
