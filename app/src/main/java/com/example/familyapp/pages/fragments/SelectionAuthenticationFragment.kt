package com.example.familyapp.pages.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.familyapp.R
import com.example.familyapp.pages.interfaces.PagerHandler

class SelectionFragment(private val pagerHandler: PagerHandler) : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_selection_authentification, container, false)

        val loginButton = view.findViewById<Button>(R.id.login_button)
        val signInButton = view.findViewById<Button>(R.id.signin_button)

        loginButton.setOnClickListener {
            pagerHandler.displayLoginPage()
        }

        signInButton.setOnClickListener {
            pagerHandler.displaySignInPage()
        }

        return view
    }
}
