package com.example.familyapp.views.fragments.dashbord

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
 import com.example.familyapp.R
 import com.example.familyapp.viewmodel.factories.TaskViewModelFactory
import com.example.familyapp.repositories.TaskRepository
import com.example.familyapp.viewmodel.TaskViewModel


class TasksFragment : Fragment() {

     private val taskViewModel: TaskViewModel by viewModels {
        TaskViewModelFactory(TaskRepository(requireContext()), this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.item_dashboard_tasks_list, container, false)
        val taskListLayout = view.findViewById<LinearLayout>(R.id.task_list)

         taskViewModel.task.observe(viewLifecycleOwner) { tasks ->
            taskListLayout.removeAllViews()

            tasks.take(3).forEach { task ->
                val taskTextView = TextView(context).apply {
                    text = task.nom // Afficher le nom de la tâche
                    textSize = 16f
                    setPadding(0, 8, 0, 8)
                }
                taskListLayout.addView(taskTextView)
            }
        }


        taskViewModel.fetchAllTasks(1)

         val viewMoreTasks = view.findViewById<TextView>(R.id.view_more_tasks)
        viewMoreTasks.setOnClickListener {
            redirectToFullTasksFragment()
        }

        return view
    }

    private fun redirectToFullTasksFragment() {
       /*  val fragment = FullTasksFragment() // À créer si ce n'est pas encore fait
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()*/
    }
}
