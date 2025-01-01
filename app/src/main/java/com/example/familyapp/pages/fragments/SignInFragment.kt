package com.example.familyapp.pages.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.familyapp.R
import com.example.familyapp.network.dto.userDto.UserDTO
import com.example.familyapp.viewmodel.ConnexionViewModel

class InscriptionFragment : Fragment() {

    private lateinit var viewModel: ConnexionViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_inscription, container, false)

        val nameInput: EditText = view.findViewById(R.id.nameInput)
        val emailInput: EditText = view.findViewById(R.id.emailInput)
        val passwordInput: EditText = view.findViewById(R.id.passwordInput)
        val registerButton: Button = view.findViewById(R.id.registerButton)

        viewModel = ViewModelProvider(requireActivity()).get(ConnexionViewModel::class.java)

        registerButton.setOnClickListener {
            val user = UserDTO(
                id = 0, // L'ID est généré par le serveur
                nom = nameInput.text.toString(),
                prenom = "",
                email = emailInput.text.toString(),
                motDePasse = passwordInput.text.toString(),
                profession = "N/A",
                numTel = "0000000000",
                role = "Parent",
                idFamille = 0
            )
            viewModel.registerUser(user)
            (activity as? ConnexionActivity)?.navigateToConnexion()
        }

        return view
    }
}
