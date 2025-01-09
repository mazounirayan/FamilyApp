package com.example.familyapp.views.fragments.task

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.familyapp.MainActivity
import com.example.familyapp.R
import com.example.familyapp.activity.TaskActivity

class ManageTaskFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_manage_task, container, false)

        view.findViewById<Button>(R.id.add_task_button).setOnClickListener {
            (activity as? TaskActivity)?.navigateToNewTaskFragment()
        }

        return view
    }

}


