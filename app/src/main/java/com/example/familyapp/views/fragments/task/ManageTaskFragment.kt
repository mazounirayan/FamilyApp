package com.example.familyapp.views.fragments.task

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.familyapp.R
import com.example.familyapp.app_utils.TaskUpdateListener
import com.example.familyapp.data.model.task.Task
import com.example.familyapp.repositories.TaskRepository
import com.example.familyapp.utils.SessionManager
import com.example.familyapp.viewmodel.TaskViewModel
import com.example.familyapp.viewmodel.factories.TaskViewModelFactory
import com.example.familyapp.views.recycler_view_adapter.TasksRvAdapter

class ManageTaskFragment : Fragment(), TaskUpdateListener {

    private lateinit var tasksRv: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private val taskViewModel: TaskViewModel by viewModels {
        TaskViewModelFactory(TaskRepository(this.requireContext()), this)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_manage_task, container, false)

        taskViewModel.task.observe(viewLifecycleOwner) { tasks ->
            updateProgress(tasks) // Mettre à jour les valeurs initiales
        }

        val filterSpinner = view.findViewById<Spinner>(R.id.filter_spinner)
        val filterOptions = listOf("Tous", "À faire", "En cours", "Fini")
        val spinnerAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            filterOptions
        )

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        filterSpinner.adapter = spinnerAdapter

        // Gérer la sélection du filtre
        filterSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedFilter = filterOptions[position]
                applyFilter(selectedFilter)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
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


    private fun applyFilter(filter: String) {
        taskViewModel.task.observe(viewLifecycleOwner) { tasks ->
            val filteredTasks = when (filter) {
                "À faire" -> tasks.filter { it.status == "A_FAIRE" }
                "En cours" -> tasks.filter { it.status == "EN_COURS" }
                "Fini" -> tasks.filter { it.status == "FINI" }
                else -> tasks // Tous les éléments
            }
            (tasksRv.adapter as TasksRvAdapter).updateTasks(filteredTasks)
        }
    }

    override fun onTaskUpdated() {
        // Recalculer et mettre à jour les valeurs après un changement de statut
        updateProgress(taskViewModel.task.value ?: emptyList())
    }

    @SuppressLint("SetTextI18n")
    private fun updateProgress(tasks: List<Task>) {
        var pourcentageCalcul = 0
        for (task in tasks) {
            if (task.status == "FINI") {
                pourcentageCalcul += 1
            }
        }

        val textTacheFini = requireView().findViewById<TextView>(R.id.tachesfini)
        val progressBar = requireView().findViewById<ProgressBar>(R.id.stats_progressbar)
        val pourcentage = requireView().findViewById<TextView>(R.id.pourcentage)

        if (tasks.isEmpty()) {
            textTacheFini.text = "Aucune tache a faire !"
            progressBar.progress = 0
            pourcentage.text = "0 %"
            return
        }

        if (tasks.size == 1) {
            textTacheFini.text = "$pourcentageCalcul tache sur ${tasks.size} fini !"
        } else {
            textTacheFini.text = "$pourcentageCalcul taches sur ${tasks.size} fini !"
        }

        pourcentageCalcul = (pourcentageCalcul * 100) / tasks.size
        progressBar.progress = pourcentageCalcul
        pourcentage.text = "$pourcentageCalcul %"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchData(view)
        setUpSwipeToRefreshListeners()
    }

    private fun setUpTasksRv(tasks: List<Task>, fragmentView: View) {
        this.tasksRv = fragmentView.findViewById(R.id.list_tasks_rv)
        this.tasksRv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        // Passer l'instance du fragment comme listener au constructeur de l'adaptateur
        this.tasksRv.adapter = TasksRvAdapter(tasks, taskViewModel, this)
    }

    private fun getTasks(data: List<Task>): List<Task> {
        return data
    }

    private fun fetchData(fragmentView: View) {
        // La vue s'abonne à la réponse
        taskViewModel.task.observe(viewLifecycleOwner) { data ->
            setUpTasksRv(getTasks(data), fragmentView)
            this.swipeRefreshLayout.isRefreshing = false
        }
        taskViewModel.fetchTask(SessionManager.currentUser!!.id)
    }

    private fun setUpSwipeToRefreshListeners() {
        this.swipeRefreshLayout.setOnRefreshListener {
            this.taskViewModel.fetchTask(SessionManager.currentUser!!.id)
        }
    }

}