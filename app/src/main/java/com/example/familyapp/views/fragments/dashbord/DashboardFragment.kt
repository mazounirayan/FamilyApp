package com.example.familyapp.views.fragments.dashbord
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.familyapp.R
import com.example.familyapp.data.model.task.Priorite
import com.example.familyapp.data.model.task.Task
import com.example.familyapp.data.model.task.TaskStatus
import com.example.familyapp.repositories.TaskRepository
import com.example.familyapp.utils.SessionManager
import com.example.familyapp.viewmodel.TaskViewModel
import com.example.familyapp.viewmodel.factories.TaskViewModelFactory
import com.example.familyapp.views.fragments.dashboard.TopContributorFragment


class DashboardFragment : Fragment() {
    private val taskViewModel: TaskViewModel by viewModels {
        TaskViewModelFactory(TaskRepository(requireContext()), this)
    }
    private val currentUserIdFamille= SessionManager.currentUser!!.idFamille ?:-1
    private lateinit var taskStatusRecyclerView: RecyclerView
    private lateinit var taskStatusAdapter: TaskStatusAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dashbord, container, false)



        val tasksFragment = TasksFragment()
        childFragmentManager.beginTransaction()
            .replace(R.id.tasks_fragment_container, tasksFragment)
            .commit()

        val piechartFragment = PiechartFragment()
        childFragmentManager.beginTransaction()
            .replace(R.id.tsk_progress_fragment_container, piechartFragment)
            .commit()

        val userFragment = UsersFragment()
        childFragmentManager.beginTransaction()
            .replace(R.id.users_fragment_container, userFragment)
            .commit()

        val bestContributer = TopContributorFragment()
        childFragmentManager.beginTransaction()
            .replace(R.id.top_contributor_container, bestContributer)
            .commit()



        taskStatusRecyclerView = view.findViewById(R.id.item_task_status)
        taskStatusRecyclerView.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )

        taskViewModel.task.observe(viewLifecycleOwner) { tasks ->
            if (tasks.isNotEmpty()) {
                updateTaskStatus(tasks)
            }
        }


        taskViewModel.fetchAllTasks(currentUserIdFamille)
        return view
    }
    private fun updateTaskStatus(tasks: List<Task>) {
        val totalTasks = tasks.size.toFloat()


        val hauteCount = tasks.count { it.priorite == Priorite.Haute}.toFloat()
        val moyenneCount = tasks.count { it.priorite == Priorite.Moyenne }.toFloat()
        val basseCount = tasks.count { it.priorite == Priorite.Basse }.toFloat()


        val hautePercentage = if (totalTasks > 0) (hauteCount / totalTasks) * 100 else 0f
        val moyennePercentage = if (totalTasks > 0) (moyenneCount / totalTasks) * 100 else 0f
        val bassePercentage = if (totalTasks > 0) (basseCount / totalTasks) * 100 else 0f


        val taskStatuses = listOf(
            TaskStatus("Haute", hautePercentage.toInt()),
            TaskStatus("Moyenne", moyennePercentage.toInt()),
            TaskStatus("Basse", bassePercentage.toInt())
        )

        // Mettre Ã  jour le RecyclerView
        taskStatusAdapter = TaskStatusAdapter(taskStatuses)
        taskStatusRecyclerView.adapter = taskStatusAdapter
    }
}