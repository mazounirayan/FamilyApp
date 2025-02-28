package com.example.familyapp.viewmodel

import UserRepository
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.familyapp.data.model.chat.Chat
import com.example.familyapp.data.model.recompense.Recompense
import com.example.familyapp.data.model.task.Task
import com.example.familyapp.data.model.user.User
import com.example.familyapp.network.dto.rewardsDto.rewardsDto
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

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error
    fun fetchRecompense(idFamille: Int) {
        viewModelScope.launch {
            try {
                _loading.value = true
                rewardsRepo.getRewards(idFamille)
                rewardsRepo.rewards.observe(context) { data ->
                    _rewards.value = data
                    _loading.value = false
                }
            } catch (e: Exception) {
                _error.value = "Erreur lors de la récupération des récompenses"
                _loading.value = false
            }
        }
    }



    fun fetchMembers(familyId: Int) {
        viewModelScope.launch {
            try {
                userRepository.getMembers(familyId)
            } catch (e: Exception) {
                _error.value = "Erreur lors de la récupération des membres"
            }
        }
    }
    private val _currentUser = MutableLiveData<User>()
    val currentUser: LiveData<User> = _currentUser
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading
    init {
       // loadMockData()
    }




    fun echangerRecompense(idRecompense: Int) {
        viewModelScope.launch {
            try {
                _loading.value = true
                rewardsRepo.buyRecompense(idRecompense, mapOf("idUser" to currentUser.value!!.id))
                currentUser.value!!.idFamille?.let { fetchRecompense(it) }
                _loading.value = false
            } catch (e: Exception) {
                _error.value = "Erreur lors de l'échange de la récompense"
                _loading.value = false
            }
        }
    }



    fun ajouterRecompense(nom: String, description: String, cout: Int, stock: Int, estDisponible: Boolean) {
        viewModelScope.launch {
            try {
                _loading.value = true
                val newReward = rewardsDto(
                    idRecompense = null,
                    nom = nom,
                    description = description,
                    cout = cout,
                    stock = stock,
                    estDisponible = estDisponible
                )

                // Observer le LiveData retourné par le Repository
                rewardsRepo.ajouterRecompense(1, newReward).observeForever { addedReward ->
                    if (addedReward != null) {
                        val currentRewards = _rewards.value.orEmpty().toMutableList()
                        currentRewards.add(addedReward)
                        _rewards.value = currentRewards
                        Log.d("RewardsViewModel", "Récompense ajoutée avec succès")
                    } else {
                        Log.e("RewardsViewModel", "Erreur lors de l'ajout de la récompense")
                    }
                    _loading.value = false
                }
            } catch (e: Exception) {
                _error.value = "Erreur lors de l'ajout de la récompense"
                _loading.value = false
            }
        }
    }

    fun updateRecompense(idRecompense: Int, nom: String, description: String, cout: Int, stock: Int, estDisponible: Boolean) {
        viewModelScope.launch {
            try {
                _loading.value = true
                val updatedReward = rewardsDto(
                    idRecompense = null, // Vous pouvez peut-être laisser l'ID vide si vous en avez besoin
                    nom = nom,
                    description = description,
                    cout = cout,
                    stock = stock,
                    estDisponible = estDisponible
                )

                // Observer le LiveData renvoyé par le Repository
                rewardsRepo.updateRecompense(idRecompense, updatedReward).observeForever { modifiedReward ->
                    if (modifiedReward != null) {
                        // Modifier la liste des récompenses en remplaçant l'ancienne récompense par la nouvelle
                        val currentRewards = _rewards.value.orEmpty().map { reward ->
                            if (reward.idRecompense == idRecompense) {
                                modifiedReward // Remplacer la récompense modifiée
                            } else {
                                reward // Garder les autres éléments inchangés
                            }
                        }

                        // Assurez-vous que la liste est bien de type List<Recompense> avant de l'assigner
                        _rewards.value = currentRewards
                    }
                    _loading.value = false
                }
            } catch (e: Exception) {
                _error.value = "Erreur lors de la modification de la récompense"
                _loading.value = false
            }
        }
    }



    fun supprimerRecompense(idRecompense: Int) {
        viewModelScope.launch {
            try {
                _loading.value = true
                rewardsRepo.supprimerRecompense(idRecompense)


                val currentRewards = _rewards.value.orEmpty().filterNot {
                    it.idRecompense == idRecompense
                }
                _rewards.value = currentRewards

                _loading.value = false
            } catch (e: Exception) {
                _error.value = "Erreur lors de la suppression de la récompense"
                _loading.value = false
            }
        }
    }
}