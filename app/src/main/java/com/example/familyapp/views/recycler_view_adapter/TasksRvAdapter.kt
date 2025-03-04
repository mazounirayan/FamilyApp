package com.example.familyapp.views.recycler_view_adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.familyapp.R
import com.example.familyapp.app_utils.TaskUpdateListener
import com.example.familyapp.data.model.task.StatusTache
import com.example.familyapp.data.model.task.Task
import com.example.familyapp.data.model.task.TaskUpdate
import com.example.familyapp.data.model.task.TaskUpdateFull
import com.example.familyapp.data.model.user.User
import com.example.familyapp.viewmodel.TaskViewModel
import com.example.familyapp.views.viewholders.TasksRvViewHolder
import java.text.SimpleDateFormat
import java.util.Locale


class TasksRvAdapter(
    private var tasks: MutableList<Task>,
    private val taskViewModel: TaskViewModel,
    private val taskUpdateListener: TaskUpdateListener,
    val context: Context,
    private val users: List<User>

) : RecyclerView.Adapter<TasksRvViewHolder>() {

    private val expandedPositions = mutableSetOf<Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksRvViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_rv_cell, parent, false)
        return TasksRvViewHolder(view)
    }

    fun deleteItem(position: Int) {
        tasks.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    fun getTaskAt(position: Int): Task {
        return tasks[position]
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: TasksRvViewHolder, position: Int) {
        val taskData = tasks[position]

        holder.taskName.text = taskData.nom
        holder.taskNameUser.text = taskData.user.prenom + " " + taskData.user.nom
        holder.taskDescription.text = taskData.description

        // Formatage de la date
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale.FRENCH)

        val dateFormatted = try {
            val date = inputFormat.parse(taskData.dateFin)
            outputFormat.format(date!!)
        } catch (e: Exception) {
            taskData.dateFin // En cas d'erreur, afficher la date brute
        }

        holder.taskDueDate.text = "Date limite : $dateFormatted"

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

        holder.buttonEdit.setOnClickListener {
            openEditTaskDialog(taskData)
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
        this.tasks = newTasks.toMutableList()
        notifyDataSetChanged()
    }

    @SuppressLint("MissingInflatedId")
    private fun openEditTaskDialog(task: Task) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_task, null)
        val editTextName = dialogView.findViewById<EditText>(R.id.edit_task_name)
        val editTextDescription = dialogView.findViewById<EditText>(R.id.edit_task_description)
        val assigneeSpinner = dialogView.findViewById<Spinner>(R.id.edit_task_assignee_spinner)

        editTextName.setText(task.nom)
        editTextDescription.setText(task.description)

        // Configuration du Spinner des utilisateurs
        val adapter = ArrayAdapter(
            context,
            android.R.layout.simple_spinner_item,
            users.map { "${it.prenom} ${it.nom}" } // Affiche "Prénom Nom"
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        assigneeSpinner.adapter = adapter

        // Sélectionner l'utilisateur actuel de la tâche
        val currentIndex = users.indexOfFirst { it.id == task.user.id }
        if (currentIndex != -1) {
            assigneeSpinner.setSelection(currentIndex)
        }

        AlertDialog.Builder(context)
            .setTitle("Modifier la tâche")
            .setView(dialogView)
            .setPositiveButton("Modifier") { _, _ ->
                val updatedTask = TaskUpdateFull(
                    nom = editTextName.text.toString(),
                    description = editTextDescription.text.toString(),
                    idUser = users[assigneeSpinner.selectedItemPosition].id // Nouvel utilisateur sélectionné
                )

                taskViewModel.patchTaskFull(task.idTache, updatedTask)

                // Mettre à jour l'affichage de la tâche
                task.nom = updatedTask.nom ?: task.nom
                task.description = updatedTask.description ?: task.description
                task.user = users[assigneeSpinner.selectedItemPosition]
                notifyDataSetChanged()
                taskUpdateListener.onTaskUpdated()
            }
            .setNegativeButton("Annuler", null)
            .show()
    }


}
