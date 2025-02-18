package com.example.familyapp.viewmodel.factories

import UserRepository
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.familyapp.repositories.RewardsRepository
import com.example.familyapp.viewmodel.RewardsViewModel
import com.example.familyapp.viewmodel.TaskViewModel

class RewardsViewModelFactory (
    private val repository: RewardsRepository,
    private val userRepository: UserRepository,
    private val fragment: Fragment
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RewardsViewModel::class.java)) {
            return RewardsViewModel(repository,userRepository, fragment) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


