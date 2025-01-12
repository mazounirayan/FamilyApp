package com.example.familyapp.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.familyapp.R

class RewardsFragment : Fragment() {

    private var role: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        role = arguments?.getString("role") // Récupère le rôle passé en argument
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_recompense, container, false)

        val adminLayout = view.findViewById<View>(R.id.admin_layout)
        val userLayout = view.findViewById<View>(R.id.user_layout)

        if (role == "admin") {
            adminLayout.visibility = View.VISIBLE
            userLayout.visibility = View.GONE
        } else {
            adminLayout.visibility = View.GONE
            userLayout.visibility = View.VISIBLE
        }

        return view
    }
}
