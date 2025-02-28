package com.example.familyapp.views.fragments.task

import android.app.AlertDialog
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.familyapp.viewmodel.TaskViewModel
import com.example.familyapp.views.recycler_view_adapter.TasksRvAdapter

class SwipeToDeleteTaskCallback(
    private val adapter: TasksRvAdapter,
    private val viewModel: TaskViewModel,
    private val lifecycleOwner: LifecycleOwner
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        val task = adapter.getTaskAt(position)

        AlertDialog.Builder(adapter.context)
            .setTitle("Supprimer la tâche")
            .setMessage("Êtes-vous sûr de vouloir supprimer cette tâche ?")
            .setPositiveButton("Oui") { _, _ ->
                viewModel.deleteTask(task.idTache)
                viewModel.taskDeletionStatus.observe(lifecycleOwner) { success ->
                    if (success) {
                        adapter.deleteItem(position)
                    } else {
                        Toast.makeText(adapter.context, "Erreur lors de la suppression", Toast.LENGTH_SHORT).show()
                        adapter.notifyItemChanged(position)
                    }
                }
            }
            .setNegativeButton("Non") { _, _ ->
                adapter.notifyItemChanged(position)
            }
            .show()
    }
}
