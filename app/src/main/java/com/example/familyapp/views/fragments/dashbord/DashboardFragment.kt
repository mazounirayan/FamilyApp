package com.example.familyapp.views.fragments
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.familyapp.R
import com.example.familyapp.data.model.task.TaskDashbord
import com.example.familyapp.data.model.user.User
import com.example.familyapp.views.Adapters.TaskDashboardAdapter
import com.example.familyapp.views.Adapters.UserDashboardAdapter

class DashboardFragment : Fragment() {

    private lateinit var userRecyclerView: RecyclerView
    private lateinit var taskRecyclerView: RecyclerView

    private lateinit var userDashboardAdapter: UserDashboardAdapter
    private lateinit var taskDashboardAdapter: TaskDashboardAdapter

     private val userList: List<User> = listOf(
        User(1, "Fils", "Prenom", "email@example.com", "1234",  "0000", "User", 1, "2023-01-01",65,"fils",2),
        User(2, "Fils01", "Prenom", "email@example.com", "5678", "0000", "User", 1, "2023-01-01",70,"fils",2),
         User(2, "Fils02", "Prenom", "email@example.com", "5678", "0000", "User", 1, "2023-01-01",70,"fils",2)

     )

     private val taskList: List<TaskDashbord> = listOf(
        TaskDashbord("Total Task", 65),
        TaskDashbord("En Cours", 45),
        TaskDashbord("Urgent Task", 85)
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dashbord, container, false)

        userRecyclerView = view.findViewById(R.id.user_recycler_view)
        userDashboardAdapter = UserDashboardAdapter(userList)
        userRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        userRecyclerView.adapter = userDashboardAdapter

        taskRecyclerView = view.findViewById(R.id.task_recycler_view)
        taskDashboardAdapter = TaskDashboardAdapter(taskList)
        taskRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        taskRecyclerView.adapter = taskDashboardAdapter

        return view
    }
}
