package com.example.familyapp.ui

import UserRepository
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.familyapp.AuthenticationActivity
import com.example.familyapp.databinding.FragmentLoginBinding
import com.example.familyapp.viewmodel.UserViewModel
import com.example.familyapp.viewmodel.factories.UserViewModelFactory

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        val repository = UserRepository(requireContext())
        val factory = UserViewModelFactory(repository,this)
        viewModel = ViewModelProvider(this, factory)[UserViewModel::class.java]

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.loginResult.observe(viewLifecycleOwner) { result ->
            result.fold(
                onSuccess = {
                    Toast.makeText(requireContext(), "Connexion réussie", Toast.LENGTH_SHORT).show()
                    (activity as? AuthenticationActivity)?.navigateToMainActivity()
                },
                onFailure = { exception ->
                    Toast.makeText(requireContext(), exception.message, Toast.LENGTH_SHORT).show()
                }
            )
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
