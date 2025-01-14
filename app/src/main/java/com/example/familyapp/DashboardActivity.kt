package com.example.familyapp
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.familyapp.data.model.user.TaskDashbord
import com.example.familyapp.data.model.user.User
import com.example.familyapp.views.Adapters.TaskDashboardAdapter
import com.example.familyapp.views.Adapters.UserDashboardAdapter

class DashboardActivity : AppCompatActivity() {

    private lateinit var userRecyclerView: RecyclerView
    private lateinit var taskRecyclerView: RecyclerView

    private lateinit var userDashboardAdapter: UserDashboardAdapter
    private lateinit var taskDashboardAdapter: TaskDashboardAdapter

     private val userList: List<User> = listOf(
        User(1, "Fils", "Prenom", "email@example.com", "1234", "Profession", "0000", "User", 1, "2023-01-01",65),
        User(2, "Fils01", "Prenom", "email@example.com", "5678", "Profession", "0000", "User", 1, "2023-01-01",70),
         User(2, "Fils02", "Prenom", "email@example.com", "5678", "Profession", "0000", "User", 1, "2023-01-01",70)

     )

     private val taskList: List<TaskDashbord> = listOf(
        TaskDashbord("Total Task", 65),
        TaskDashbord("En Cours", 45),
        TaskDashbord("Urgent Task", 85)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashbord)

         userRecyclerView = findViewById(R.id.user_recycler_view)
        userDashboardAdapter = UserDashboardAdapter(userList)
        userRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        userRecyclerView.adapter = userDashboardAdapter

         taskRecyclerView = findViewById(R.id.task_recycler_view)
        taskDashboardAdapter = TaskDashboardAdapter(taskList)
        taskRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        taskRecyclerView.adapter = taskDashboardAdapter
    }
}
