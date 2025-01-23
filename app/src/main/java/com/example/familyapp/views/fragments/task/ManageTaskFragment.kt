package com.example.familyapp.views.fragments.task

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.familyapp.R
import com.example.familyapp.data.model.task.Task
import com.example.familyapp.repositories.TaskRepository
import com.example.familyapp.viewmodel.TaskViewModel
import com.example.familyapp.viewmodel.factories.TaskViewModelFactory
import com.example.familyapp.views.recycler_view_adapter.TasksRvAdapter

class ManageTaskFragment : Fragment() {

    private lateinit var tasksRv: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private val taskViewModel: TaskViewModel by viewModels {
        TaskViewModelFactory(TaskRepository(this.requireContext()),this)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_manage_task, container, false)
        val pourcentage = view.findViewById<TextView>(R.id.pourcentage)
        val textTacheFini = view.findViewById<TextView>(R.id.tachesfini)
        val progressBar = view.findViewById<ProgressBar>(R.id.stats_progressbar)

        taskViewModel.task.observe(viewLifecycleOwner) { tasks ->
            var pourcentageCalcul = 0
            for (task in tasks) {

                if(task.status == "FINI"){
                    pourcentageCalcul+=1
                }

            }
            if(tasks.isEmpty()){
                textTacheFini.text = "Aucune tache a faire !"
            }else if(tasks.size == 1){
                textTacheFini.text = "$pourcentageCalcul tache sur ${tasks.size} fini !"
            }else{
                textTacheFini.text = "$pourcentageCalcul taches sur ${tasks.size} fini !"
            }

            if(tasks.isNotEmpty()){
                pourcentageCalcul = (pourcentageCalcul*100)/tasks.size
                progressBar.progress = pourcentageCalcul
                pourcentage.text = "$pourcentageCalcul %"
            }else{
                progressBar.progress = 0
                pourcentage.text = "0 %"
            }

        }
        view.findViewById<Button>(R.id.add_task_button).setOnClickListener {
            val supportFragmentManager = activity?.supportFragmentManager

            supportFragmentManager?.commit {
                replace<NewTaskFragment>(R.id.fragment_container)
                setReorderingAllowed(true)
                addToBackStack("name")
            }
        }

        this.swipeRefreshLayout = view.findViewById(R.id.task_fragment_manage)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchData(view)
        setUpSwipeToRefreshListeners()
    }

    private fun getTasks(data: List<Task>): List<Task> {
        return data
    }

    private fun setUpTasksRv(tasks: List<Task>, fragmentView: View) {
        this.tasksRv = fragmentView.findViewById(R.id.list_tasks_rv)
        this.tasksRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        this.tasksRv.adapter = TasksRvAdapter(tasks, taskViewModel)
    }

    private fun fetchData(fragmentView: View) {
        // La vue s'abonne à la réponse
        taskViewModel.task.observe(viewLifecycleOwner) { data ->
            setUpTasksRv(getTasks(data), fragmentView)
            this.swipeRefreshLayout.isRefreshing = false
        }
        taskViewModel.fetchTask(1)
    }

    private fun setUpSwipeToRefreshListeners() {
        this.swipeRefreshLayout.setOnRefreshListener {
            this.taskViewModel.fetchTask(1)
        }
    }

}