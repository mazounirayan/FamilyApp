package com.example.familyapp.views.fragments.task

import UserRepository
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
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
import com.example.familyapp.data.model.user.User
import com.example.familyapp.repositories.TaskRepository
import com.example.familyapp.utils.SessionManager
import com.example.familyapp.viewmodel.TaskViewModel
import com.example.familyapp.viewmodel.UserViewModel
import com.example.familyapp.viewmodel.factories.TaskViewModelFactory
import com.example.familyapp.viewmodel.factories.UserViewModelFactory
import com.example.familyapp.views.recycler_view_adapter.TasksRvAdapter

class ManageTaskFragment : Fragment(), TaskUpdateListener {
    private var user = SessionManager.currentUser
    private lateinit var tasksRv: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private var currentTasks: List<Task> = emptyList()

    private val taskViewModel: TaskViewModel by viewModels {
        TaskViewModelFactory(TaskRepository(this.requireContext()), this)
    }

    private val userViewModel: UserViewModel by viewModels {
        UserViewModelFactory(UserRepository(this.requireContext()), this)
    }

    private var familyMembers: List<User> = emptyList()

    companion object {
        const val TASK_STATUS_TODO = "A_FAIRE"
        const val TASK_STATUS_IN_PROGRESS = "EN_COURS"
        const val TASK_STATUS_DONE = "FINI"
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_manage_task, container, false)

        taskViewModel.task.observe(viewLifecycleOwner) { tasks ->
            updateProgress(tasks)
        }

        when (user?.role) {
            "Parent" -> setupParentView(view)
            else -> setupChildView(view)
        }

        this.swipeRefreshLayout = view.findViewById(R.id.task_fragment_manage)

        return view
    }

    private fun setupFilterSpinner(view: View, familyMembers: List<User>) {
        val filterSpinner = view.findViewById<Spinner>(R.id.filter_spinner)
        val filterOptions = mutableListOf("Tous", "Mes tâches", "À faire", "En cours", "Fini")

        if (user?.role == "Parent") {
            familyMembers.forEach { member ->
                if(member.id != user!!.id){
                    filterOptions.add("${member.prenom} ${member.nom}")
                }
            }
        }

        val spinnerAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            filterOptions
        )

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        filterSpinner.adapter = spinnerAdapter

        filterSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedFilter = filterOptions[position]
                applyFilter(selectedFilter, familyMembers)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }


    private fun setupParentView(view: View) {
        view.findViewById<Button>(R.id.add_task_button).setOnClickListener {
            navigateToNewTaskFragment()
        }
    }

    private fun setupChildView(view: View) {
        view.findViewById<Button>(R.id.add_task_button).visibility = View.GONE
    }

    private fun navigateToNewTaskFragment() {
        activity?.supportFragmentManager?.commit {
            replace<NewTaskFragment>(R.id.fragment_container)
            setReorderingAllowed(true)
            addToBackStack("name")
        }
    }

    private fun applyFilter(filter: String, familyMembers: List<User>) {
        taskViewModel.task.observe(viewLifecycleOwner) { tasks ->
            val filteredTasks = when (filter) {
                "Tous" -> tasks
                "Mes tâches" -> tasks.filter { it.user.id == user?.id }
                "À faire" -> tasks.filter { it.status == TASK_STATUS_TODO }
                "En cours" -> tasks.filter { it.status == TASK_STATUS_IN_PROGRESS }
                "Fini" -> tasks.filter { it.status == TASK_STATUS_DONE }
                else -> {
                    val selectedMember = familyMembers.find { "${it.prenom} ${it.nom}" == filter }
                    if (selectedMember != null) {
                        tasks.filter { it.user.id == selectedMember.id }
                    } else {
                        tasks
                    }
                }
            }
            currentTasks = filteredTasks
            updateTaskList(filteredTasks)
            updateProgress(filteredTasks)
        }
    }


    private fun updateTaskList(filteredTasks: List<Task>) {
        (tasksRv.adapter as TasksRvAdapter).updateTasks(filteredTasks)
    }

    override fun onTaskUpdated() {
        updateProgress(taskViewModel.task.value ?: emptyList())
    }

    @SuppressLint("SetTextI18n")
    private fun updateProgress(tasks: List<Task>) {
        val textTacheFini = requireView().findViewById<TextView>(R.id.tachesfini)
        val progressBar = requireView().findViewById<ProgressBar>(R.id.stats_progressbar)
        val pourcentage = requireView().findViewById<TextView>(R.id.pourcentage)

        val completionPercentage = tasks.calculateCompletionPercentage()
        val completedTasks = tasks.count { it.status == TASK_STATUS_DONE }

        when {
            tasks.isEmpty() -> {
                textTacheFini.text = "Aucune tache a faire !"
                progressBar.progress = 0
                pourcentage.text = "0 %"
            }
            tasks.size == 1 -> {
                textTacheFini.text = "$completedTasks tache sur ${tasks.size} fini !"
                progressBar.progress = completionPercentage
                pourcentage.text = "$completionPercentage %"
            }
            else -> {
                textTacheFini.text = "$completedTasks taches sur ${tasks.size} fini !"
                progressBar.progress = completionPercentage
                pourcentage.text = "$completionPercentage %"
            }
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchData(view)
        setUpSwipeToRefreshListeners()
    }

    private fun setUpTasksRv(tasks: List<Task>, fragmentView: View) {
        this.tasksRv = fragmentView.findViewById(R.id.list_tasks_rv)
        this.tasksRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        this.tasksRv.adapter = TasksRvAdapter(tasks, taskViewModel, this)
    }

    private fun fetchData(fragmentView: View) {
        taskViewModel.task.observe(viewLifecycleOwner) { tasks ->
            setUpTasksRv(tasks, fragmentView)
            swipeRefreshLayout.isRefreshing = false
        }

        userViewModel.user.observe(viewLifecycleOwner) { users ->
            if (users.isEmpty()) {
                Log.e("ManageTaskFragment", "La liste des utilisateurs est vide.")
                Toast.makeText(
                    context,
                    "La liste des membres de la famille est vide.",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Log.d("ManageTaskFragment", "Utilisateurs récupérés : ${users.size}")
                familyMembers = users
                setupFilterSpinner(fragmentView, familyMembers)
            }
        }

        val familyId = if (user?.role == "Parent") 1 else user?.id
        familyId?.let { taskViewModel.fetchTask(it) }

        userViewModel.fetchUser(1)
    }


    private fun setUpSwipeToRefreshListeners() {
        this.swipeRefreshLayout.setOnRefreshListener {
            val familyId = if (user?.role == "Parent") 1 else user?.id
            familyId?.let { taskViewModel.fetchTask(it) }
            swipeRefreshLayout.isRefreshing = false
        }
    }
}

fun List<Task>.calculateCompletionPercentage(): Int {
    if (isEmpty()) return 0
    val completedTasks = count { it.status == ManageTaskFragment.TASK_STATUS_DONE }
    return (completedTasks * 100) / size
}
