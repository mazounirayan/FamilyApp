package com.example.familyapp.views.fragments

import UserRepository
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.familyapp.AuthenticationActivity
import com.example.familyapp.R
import com.example.familyapp.viewmodel.UserViewModel
import com.example.familyapp.viewmodel.factories.UserViewModelFactory





class SignupFragment : Fragment() {

    private lateinit var viewModel: UserViewModel
    private lateinit var roleSpinner: Spinner
    private lateinit var familyCodeEditText: EditText // Déclarez familyCodeEditText
    private lateinit var nomFamilleEditText: EditText // Déclarez nomFamilleEditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_signup, container, false)

        // Initialisez les vues
        roleSpinner = view.findViewById(R.id.roleSpinner)
        familyCodeEditText = view.findViewById(R.id.familyCodeEditText)
        nomFamilleEditText = view.findViewById(R.id.nomFamilleEditText)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()
        setupSpinner()
        setupListeners()
        setupObservers()
    }

    private fun setupViewModel() {
        val repository = UserRepository(requireContext())
        val factory = UserViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[UserViewModel::class.java]
    }

    private fun setupSpinner() {
        val roles = listOf("Parent", "Enfant")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, roles)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        roleSpinner.adapter = adapter

        roleSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val isChild = roles[position] == "Enfant"
                familyCodeEditText.visibility = if (isChild) View.VISIBLE else View.GONE
                nomFamilleEditText.visibility = if (!isChild) View.VISIBLE else View.GONE
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun setupListeners() {
        view?.findViewById<View>(R.id.signupButton)?.setOnClickListener {
            val nom = view?.findViewById<EditText>(R.id.nomEditText)?.text.toString().trim()
            val prenom = view?.findViewById<EditText>(R.id.prenomEditText)?.text.toString().trim()
            val email = view?.findViewById<EditText>(R.id.emailEditText)?.text.toString().trim()
            val motDePasse = view?.findViewById<EditText>(R.id.passwordEditText)?.text.toString().trim()
            val role = roleSpinner.selectedItem.toString()
            val codeFamille = familyCodeEditText.text.toString().trim()
            val nomFamille = nomFamilleEditText.text.toString().trim()

            viewModel.nom.value = nom
            viewModel.prenom.value = prenom
            viewModel.email.value = email
            viewModel.motDePasse.value = motDePasse
            viewModel.role.value = role
            viewModel.codeFamille.value = codeFamille
            viewModel.nomFamille.value = nomFamille

            viewModel.signUp()
        }
    }

    private fun setupObservers() {
        viewModel.signUpResult.observe(viewLifecycleOwner) { result ->
            result.fold(
                onSuccess = {
                    // L'inscription a réussi
                    Toast.makeText(requireContext(), "Inscription réussie", Toast.LENGTH_SHORT).show()
                    // Rediriger l'utilisateur vers l'écran de connexion
                    (activity as? AuthenticationActivity)?.navigateToLoginFragment()
                },
                onFailure = { exception ->
                    // Gérer les erreurs
                    Toast.makeText(requireContext(), exception.message, Toast.LENGTH_SHORT).show()
                }
            )
        }
    }

}