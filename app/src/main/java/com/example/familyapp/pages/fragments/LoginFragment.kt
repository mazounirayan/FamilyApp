package com.example.familyapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.familyapp.databinding.FragmentLoginBinding
import com.example.familyapp.repositories.UserRepository
import com.example.familyapp.viewmodel.LoginViewModel
import com.example.familyapp.viewmodel.factories.LoginViewModelFactory

class LoginFragment : Fragment() {

    // Utilisation du pattern Backing Property pour le binding
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    // Instance du ViewModel pour la logique de connexion
    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Initialisation du binding
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        // Configuration du ViewModel
        val repository = UserRepository(requireContext())
        val factory = LoginViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]

        // Configuration du data binding
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        // Observation du résultat de connexion
        viewModel.loginResult.observe(viewLifecycleOwner) { result ->
            result.fold(
                onSuccess = {
                    // En cas de succès de connexion
                    Toast.makeText(requireContext(), "Connexion réussie", Toast.LENGTH_SHORT).show()
                },
                onFailure = { exception ->
                    // En cas d'échec de connexion
                    Toast.makeText(requireContext(), exception.message, Toast.LENGTH_SHORT).show()
                }
            )
        }

        return binding.root
    }

    // Nettoyage du binding pour éviter les fuites de mémoire
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}