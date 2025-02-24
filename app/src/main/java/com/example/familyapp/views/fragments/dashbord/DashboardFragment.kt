package com.example.familyapp.views.fragments
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
import com.example.familyapp.views.fragments.dashbord.PiechartFragment
import com.example.familyapp.views.fragments.dashbord.TaskStatusAdapter
import com.example.familyapp.views.fragments.dashbord.TasksFragment
import com.example.familyapp.views.fragments.dashbord.UsersFragment


class DashboardFragment : Fragment() {
    private val currentUserId = SessionManager.currentUser!!.id
    private val taskViewModel: TaskViewModel by viewModels {
        TaskViewModelFactory(TaskRepository(requireContext()), this)
    }

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



        // Initialisation du RecyclerView pour afficher les priorités
        taskStatusRecyclerView = view.findViewById(R.id.item_task_status)
        taskStatusRecyclerView.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )

        // Observer les tâches et mettre à jour les priorités
        taskViewModel.task.observe(viewLifecycleOwner) { tasks ->
            if (tasks.isNotEmpty()) {
                updateTaskStatus(tasks)
            }
        }

        // Charger les tâches de l'API
        taskViewModel.fetchAllTasks(currentUserId) //
        return view
    }
    private fun updateTaskStatus(tasks: List<Task>) {
        val totalTasks = tasks.size.toFloat()

        // Compter les tâches par priorité
        val hauteCount = tasks.count { it.priorite == Priorite.HAUTE }.toFloat()
        val moyenneCount = tasks.count { it.priorite == Priorite.MOYENNE }.toFloat()
        val basseCount = tasks.count { it.priorite == Priorite.BASSE}.toFloat()

        // Calculer les pourcentages
        val hautePercentage = if (totalTasks > 0) (hauteCount / totalTasks) * 100 else 0f
        val moyennePercentage = if (totalTasks > 0) (moyenneCount / totalTasks) * 100 else 0f
        val bassePercentage = if (totalTasks > 0) (basseCount / totalTasks) * 100 else 0f

        // Créer une nouvelle liste avec les vraies données
        val taskStatuses = listOf(
            TaskStatus("Haute", hautePercentage.toInt()),
            TaskStatus("Moyenne", moyennePercentage.toInt()),
            TaskStatus("Basse", bassePercentage.toInt())
        )

        // Mettre à jour le RecyclerView
        taskStatusAdapter = TaskStatusAdapter(taskStatuses)
        taskStatusRecyclerView.adapter = taskStatusAdapter
    }
}