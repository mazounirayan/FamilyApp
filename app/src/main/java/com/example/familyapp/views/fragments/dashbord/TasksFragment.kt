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
import com.example.familyapp.utils.SessionManager
import com.example.familyapp.viewmodel.TaskViewModel
import com.example.familyapp.views.fragments.task.ManageTaskFragment



class TasksFragment : Fragment() {

    private val taskViewModel: TaskViewModel by viewModels {
        TaskViewModelFactory(TaskRepository(requireContext()), this)
    }
    private var idFamille = SessionManager.currentUser!!.idFamille

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
                    text = task.nom 
                    textSize = 16f
                    setPadding(0, 8, 0, 8)
                }
                taskListLayout.addView(taskTextView)
            }
        }


        idFamille?.let { taskViewModel.fetchAllTasks(it) }

        val viewMoreTasks = view.findViewById<TextView>(R.id.view_more_tasks)
        viewMoreTasks.setOnClickListener {
            redirectToFullTasksFragment()
        }

        return view
    }

    private fun redirectToFullTasksFragment() {
        val fragment = ManageTaskFragment()

        // Utilisez l'activity parent pour accéder au FragmentManager principal
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}