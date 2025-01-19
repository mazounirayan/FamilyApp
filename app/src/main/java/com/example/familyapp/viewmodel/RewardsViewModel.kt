package com.example.familyapp.viewmodel

import UserRepository
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.familyapp.data.model.recompense.Recompense
import com.example.familyapp.data.model.task.Task
import com.example.familyapp.data.model.user.User
import com.example.familyapp.repositories.RewardsRepository
import kotlinx.coroutines.launch

class RewardsViewModel (
    private val rewardsRepo: RewardsRepository,
    private val userRepository: UserRepository,
    val context:LifecycleOwner
): ViewModel() {
    private val _users = MutableLiveData<List<User>>()
    private val _rewards = MutableLiveData<List<Recompense>>()
    val recompenses: LiveData<List<Recompense>> = _rewards
    val users = userRepository.users



    fun fetchRecompense(idFamille: Int) {
        _rewards.value
        this.rewardsRepo.rewards.observe(this.context) { data ->
            this@RewardsViewModel._rewards.value = data
        }

        this.rewardsRepo.getRewards(idFamille)
    }

    fun fetchMembers(familyId: Int) {
        userRepository.getMembers(familyId)
    }
    private val _currentUser = MutableLiveData<User>()
    val currentUser: LiveData<User> = _currentUser

    init {
        loadMockData()
    }

    private fun loadMockData() {
        // Simulation des données
        _currentUser.value = User(
            1, "Martin", "Sophie", "sophie@email.com",
            "xxx", "123456789", "PARENT", 1,
            "2024-01-14",  1500, "avatar1.jpg",2000
        )

        _users.value = listOf(
            User(1, "Martin", "Sophie", "sophie@email.com", "xxx", "123456789", "ENFANT", 1, "2024-01-14",  1500,"avatar1.jpg", 2000),
            User(2, "Martin", "Lucas", "lucas@email.com", "xxx", "123456789", "ENFANT", 1, "2024-01-14", 1200, "avatar1.jpg",1800),
            User(3, "Martin", "Emma", "emma@email.com", "xxx", "123456789", "ENFANT", 1, "2024-01-14", 800,"avatar1.jpg", 1200)
        )


    }

    fun echangerRecompense(recompense: Recompense) {
        // Implémenter la logique d'échange ici
    }


    fun ajouterRecompense(nom: String, description: String, cout: Int, stock: Int) {
        viewModelScope.launch {
            RewardsRepository.ajouterRecompense(nom, description, cout, stock)
            // Rafraîchir la liste après l'ajout
            fetchRecompense(1)
        }
    }

    fun updateRecompense(id: Int, nom: String, description: String, cout: Int, stock: Int) {
        viewModelScope.launch {
            RewardsRepository.updateRecompense(id, nom, description, cout, stock)
            // Rafraîchir la liste après la modification
            fetchRecompense(1)
        }
    }
    fun supprimerRecompense(id: Int) {
        viewModelScope.launch {
            RewardsRepository.supprimerRecompense(id)
            // Rafraîchir la liste après la suppression
            fetchRecompense(1)
        }
    }
}