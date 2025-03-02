package com.example.familyapp.views.fragments.dashboard

import UserRepository
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.familyapp.R
import com.example.familyapp.repositories.TaskRepository
import com.example.familyapp.utils.SessionManager
import com.example.familyapp.viewmodel.TaskViewModel
import com.example.familyapp.viewmodel.UserViewModel
import com.example.familyapp.viewmodel.factories.TaskViewModelFactory
import com.example.familyapp.viewmodel.factories.UserViewModelFactory

class TopContributorFragment : Fragment() {
    private var idFamille = SessionManager.currentUser!!.idFamille

    private val userViewModel: UserViewModel by viewModels {
        UserViewModelFactory(
            UserRepository(this.requireContext()),
            fragment = this
        )
    }

    private val taskViewModel: TaskViewModel by viewModels {
        TaskViewModelFactory(TaskRepository(this.requireContext()), this)
    }

    private lateinit var winnerNameTextView: TextView
    private lateinit var progressEnCours: ProgressBar
    private lateinit var progressFini: ProgressBar
    private lateinit var progressNonCommence: ProgressBar
    private lateinit var percentEnCours: TextView
    private lateinit var percentFini: TextView
    private lateinit var percentNonCommence: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.item_dashboard_top_contributor, container, false)

        winnerNameTextView = view.findViewById(R.id.winner_name)
        progressEnCours = view.findViewById(R.id.progress_en_cours)
        progressFini = view.findViewById(R.id.progress_fini)
        progressNonCommence = view.findViewById(R.id.progress_non_commence)
        percentEnCours = view.findViewById(R.id.percent_en_cours)
        percentFini = view.findViewById(R.id.percent_fini)
        percentNonCommence = view.findViewById(R.id.percent_non_commence)

        userViewModel.users.observe(viewLifecycleOwner) { users ->
            val topUser = users.maxByOrNull { it.totalPoints }
            topUser?.let { user ->
                winnerNameTextView.text = "${user.prenom} ${user.nom}"
                fetchUserTasks(user.id)
            }
        }

        idFamille?.let { userViewModel.fetchUsers(it) }

        return view
    }

    private fun fetchUserTasks(userId: Int) {
        taskViewModel.task.observe(viewLifecycleOwner) { tasks ->
            val totalTasks = tasks.size
            if (totalTasks > 0) {
                val notStartedTasks = tasks.count { it.status == "A_FAIRE" }
                val inProgressTasks = tasks.count { it.status == "EN_COURS" }
                val finishedTasks = tasks.count { it.status == "FINI" }

                val notStartedPercentage = (notStartedTasks * 100) / totalTasks
                val inProgressPercentage = (inProgressTasks * 100) / totalTasks
                val finishedPercentage = (finishedTasks * 100) / totalTasks

                progressNonCommence.progress = notStartedPercentage
                progressEnCours.progress = inProgressPercentage
                progressFini.progress = finishedPercentage

                percentNonCommence.text = "$notStartedPercentage%"
                percentEnCours.text = "$inProgressPercentage%"
                percentFini.text = "$finishedPercentage%"
            }
        }

        taskViewModel.fetchTask(userId)
    }
}