package com.example.familyapp.activity

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.familyapp.NavigationBar
import com.example.familyapp.R
import com.example.familyapp.views.fragments.task.ManageTaskFragment
import com.example.familyapp.views.fragments.task.NewTaskFragment

class TaskActivity : NavigationBar() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.task_fragment_container, ManageTaskFragment())
                .commit()
        }
    }
    

    fun navigateToNewTaskFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.task_fragment_container, NewTaskFragment())
            .addToBackStack(null)
            .commit()
    }
}



