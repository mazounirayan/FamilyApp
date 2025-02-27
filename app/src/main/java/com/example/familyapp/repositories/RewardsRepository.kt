package com.example.familyapp.repositories

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.familyapp.MainApplication
import com.example.familyapp.app_utils.NetworkUtils
import com.example.familyapp.data.model.recompense.Recompense
import com.example.familyapp.db.entities.RecompenseEntity
import com.example.familyapp.network.RetrofitClient
import com.example.familyapp.network.dto.rewardsDto.rewardsDto

import com.example.familyapp.network.mapper.mapRewardDtoToReward
import com.example.familyapp.network.services.RewardService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RewardsRepository(context: Context) {

    private val rewardService = RetrofitClient.instance.create(RewardService::class.java)

    private val _rewards = MutableLiveData<List<Recompense>>()

    val rewards: LiveData<List<Recompense>> get() = _rewards
    private val db = MainApplication.database
    private val recompenseDao = db.recompenseDao()
    private val context = context
    private val scope = CoroutineScope(   SupervisorJob())
    fun getRewards(idFamille: Int) {
        val call = rewardService.getRecompense(idFamille)
        if (NetworkUtils.isOnline(context)){
            call.enqueue(object : Callback<List<rewardsDto>> {
                override fun onResponse(
                    call: Call<List<rewardsDto>>,
                    response: Response<List<rewardsDto>>
                ) {

                    if (response.isSuccessful) {


                        val responseBody = response.body()
                        responseBody?.let {
                            // Convertir les DTO en modèles
                            val recompenses = it.map { value ->
                                mapRewardDtoToReward(value)
                            }
                            _rewards.value = recompenses

                            // Sauvegarder les récompenses dans la base de données locale
                            scope.launch {
                                saveRecompensesLocally(recompenses)
                            }
                        }

                    } else {
                        Log.e("TaskRepository", "Erreur HTTP : ${response.code()}")
                        loadRecompensesFromLocalDb()
                    }
                }

                override fun onFailure(call: Call<List<rewardsDto>>, t: Throwable) {
                    Log.e("TaskRepository", "Erreur réseau : ${t.message}")
                    loadRecompensesFromLocalDb()
                }
            })
        }else{
            loadRecompensesFromLocalDb()
        }

    }



    fun ajouterRecompense(idFamille: Int, newReward: rewardsDto): Recompense? {
        val call = rewardService.addRecompense(idFamille, newReward)
        var addedReward: Recompense? = null
        call.enqueue(object : Callback<rewardsDto> {
            override fun onResponse(call: Call<rewardsDto>, response: Response<rewardsDto>) {
                if (response.isSuccessful) {
                    val rewardResponse = response.body()
                    if (rewardResponse != null) {
                        addedReward = mapRewardDtoToReward(rewardResponse)
                        val currentRewards = _rewards.value?.toMutableList() ?: mutableListOf()
                        currentRewards.add(addedReward!!)
                        _rewards.value = currentRewards
                        Log.d("RewardsRepository", "Récompense ajoutée avec succès")
                    } else {
                        Log.e("RewardsRepository", "Réponse vide lors de l'ajout de la récompense")
                    }
                } else {
                    Log.e("RewardsRepository", "Erreur lors de l'ajout de la récompense: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<rewardsDto>, t: Throwable) {
                Log.e("RewardsRepository", "Erreur réseau lors de l'ajout de la récompense: ${t.message}")
            }
        })
        return addedReward
    }

    fun buyRecompense(idRecompense: Int, requestBody: Map<String, Int>) {
        val call = rewardService.buyRecompense(idRecompense, requestBody)
        call.enqueue(object : Callback<rewardsDto> {
            override fun onResponse(call: Call<rewardsDto>, response: Response<rewardsDto>) {
                if (response.isSuccessful) {
                    Log.d("RewardsRepository", "Récompense achetée avec succès")
                } else {
                    Log.e("RewardsRepository", "Erreur lors de l'achat de la récompense: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<rewardsDto>, t: Throwable) {
                Log.e("RewardsRepository", "Erreur réseau lors de l'achat de la récompense: ${t.message}")
            }
        })
    }

    fun updateRecompense(id: Int, updatedReward: rewardsDto): Recompense? {
        val call = rewardService.updateRecompense(id, updatedReward)
        var modifiedReward: Recompense? = null
        call.enqueue(object : Callback<rewardsDto> {
            override fun onResponse(call: Call<rewardsDto>, response: Response<rewardsDto>) {
                if (response.isSuccessful) {
                    val rewardResponse = response.body()
                    if (rewardResponse != null) {
                        modifiedReward = mapRewardDtoToReward(rewardResponse)
                        val currentRewards = _rewards.value?.toMutableList() ?: mutableListOf()
                        val index = currentRewards.indexOfFirst { it.idRecompense == id }
                        if (index != -1) {
                            currentRewards[index] = modifiedReward!!
                            _rewards.value = currentRewards
                            Log.d("RewardsRepository", "Récompense mise à jour avec succès")
                        }
                    } else {
                        Log.e("RewardsRepository", "Réponse vide lors de la mise à jour de la récompense")
                    }
                } else {
                    Log.e("RewardsRepository", "Erreur lors de la mise à jour de la récompense: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<rewardsDto>, t: Throwable) {
                Log.e("RewardsRepository", "Erreur réseau lors de la mise à jour de la récompense: ${t.message}")
            }
        })
        return modifiedReward
    }
    fun supprimerRecompense(id: Int) {
        val call = rewardService.deleteRecompense(id)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.d("RewardsRepository", "Récompense supprimée avec succès")
                } else {
                    Log.e("RewardsRepository", "Erreur lors de la suppression de la récompense: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("RewardsRepository", "Erreur réseau lors de la suppression de la récompense: ${t.message}")
            }
        })
    }



    private suspend fun saveRecompensesLocally(recompenses: List<Recompense>) {
        // Convertir les récompenses en entités
        val recompenseEntities = recompenses.map {
            RecompenseEntity(
                idRecompense = it.idRecompense,
                nom = it.nom,
                cout = it.cout,
                description = it.description,
                stock = it.stock,
                estDisponible = it.estDisponible,
            )
        }

        // Sauvegarder les entités dans la base de données locale
        recompenseDao.insertRecompenses(recompenseEntities)
    }
    private fun loadRecompensesFromLocalDb() {
        // Récupérer les récompenses locales depuis la base de données
        val localRecompenses = recompenseDao.getAllRecompenses()
        localRecompenses.observeForever {
            // Convertir les entités en modèles
            val recompenses = it.map { entity ->
                Recompense(
                    idRecompense = entity.idRecompense,
                    nom = entity.nom,
                    cout = entity.cout,
                    description = entity.description,
                    stock = entity.stock,
                    estDisponible = entity.estDisponible
                )
            }
            // Mettre à jour les données avec les récompenses locales
            _rewards.postValue(recompenses)
        }
    }


}