package com.example.familyapp.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.familyapp.R

class ProfilFragment : Fragment() {

    private lateinit var viewModel: ProfileViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_recompense, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialisation du ViewModel

        // Charger les données du profil
        val userId = "123" // Remplacez par l'ID de l'utilisateur connecté
        viewModel.loadUserProfile(userId)

        // Observer les données du profil
        viewModel.userProfile.observe(viewLifecycleOwner) { profile ->
            binding.tvFullName.text = "${profile.firstName} ${profile.lastName}"
            binding.tvEmail.text = profile.email
            binding.tvRole.text = "Rôle: ${profile.role}"
        }

        // Observer les récompenses
        viewModel.rewards.observe(viewLifecycleOwner) { rewards ->
            binding.tvRewards.text = "Récompenses: $rewards points"
        }

        // Observer les tâches accomplies
        viewModel.completedTasks.observe(viewLifecycleOwner) { tasks ->
            binding.tvCompletedTasks.text = "Tâches accomplies: $tasks"
        }

        // Gestion du bouton Modifier le profil
        binding.btnEditProfile.setOnClickListener {
            // Naviguer vers l'écran de modification du profil
            findNavController().navigate(R.id.action_profileFragment_to_editProfileFragment)
        }

        // Gestion du bouton Déconnexion
        binding.btnLogout.setOnClickListener {
            // Déconnecter l'utilisateur
            viewModel.logout()
            findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
        }
    }
}