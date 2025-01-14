package com.example.familyapp.views.fragments.task

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.familyapp.R

class NewTaskFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_create_task, container, false)

        view.findViewById<ImageView>(R.id.back_button_create_task).setOnClickListener {
            parentFragmentManager.popBackStack()
        }
        return view
    }
}
