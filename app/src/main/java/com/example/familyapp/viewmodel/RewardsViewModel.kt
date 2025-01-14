package com.example.familyapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.familyapp.data.model.recompense.Recompense
import com.example.familyapp.data.model.user.User

class RecompenseViewModel : ViewModel() {
    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> = _users

    private val _recompenses = MutableLiveData<List<Recompense>>()
    val recompenses: LiveData<List<Recompense>> = _recompenses

    private val _currentUser = MutableLiveData<User>()
    val currentUser: LiveData<User> = _currentUser

    init {
        loadMockData()
    }

    private fun loadMockData() {
        // Simulation des données
        _currentUser.value = User(
            1, "Martin", "Sophie", "sophie@email.com",
            "xxx", "123456789", "ENFANT", 1,
            "2024-01-14",  1500, "avatar1.jpg",2000
        )

        _users.value = listOf(
            User(1, "Martin", "Sophie", "sophie@email.com", "xxx", "123456789", "ENFANT", 1, "2024-01-14",  1500,"avatar1.jpg", 2000),
            User(2, "Martin", "Lucas", "lucas@email.com", "xxx", "123456789", "ENFANT", 1, "2024-01-14",  1200,"avatar2.jpg", 1800),
            User(3, "Martin", "Emma", "emma@email.com", "xxx", "123456789", "ENFANT", 1, "2024-01-14",  800, "avatar3.jpg",1200)
        )

        _recompenses.value = listOf(
            Recompense(1, "Nintendo Switch", "1 heure de jeu", 100, 5, true),
            Recompense(2, "Sortie cinéma", "Une séance au choix", 200, 3, true),
            Recompense(3, "Restaurant", "Menu enfant", 150, 2, true),
            Recompense(4, "Parc d'attractions", "Une journée", 500, 1, true)
        )
    }

    fun echangerRecompense(recompense: Recompense) {
        // Implémenter la logique d'échange ici
    }
}