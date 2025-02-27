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



    fun ajouterRecompense(idFamille: Int, newReward: rewardsDto): LiveData<Recompense?> {
        val result = MutableLiveData<Recompense?>()

        if (NetworkUtils.isOnline(context)) {
            // L'appareil est en ligne, envoyer la récompense au serveur
            val call = rewardService.addRecompense(idFamille, newReward)

            call.enqueue(object : Callback<rewardsDto> {
                override fun onResponse(call: Call<rewardsDto>, response: Response<rewardsDto>) {
                    if (response.isSuccessful) {
                        val rewardResponse = response.body()
                        if (rewardResponse != null) {
                            val addedReward = mapRewardDtoToReward(rewardResponse)

                            // Mettre à jour les données avec la nouvelle récompense
                            val currentRewards = _rewards.value?.toMutableList() ?: mutableListOf()
                            currentRewards.add(addedReward)
                            _rewards.value = currentRewards

                            scope.launch {
                                recompenseDao.insertRecompenses(listOf(mapRewardToEntity(addedReward)))
                            }

                            // Retourner la récompense ajoutée
                            result.value = addedReward
                            Log.d("RewardsRepository", "Récompense ajoutée avec succès")
                        } else {
                            Log.e("RewardsRepository", "Réponse vide lors de l'ajout de la récompense")
                            result.value = null
                        }
                    } else {
                        Log.e("RewardsRepository", "Erreur lors de l'ajout de la récompense: ${response.code()}")
                        result.value = null
                    }
                }

                override fun onFailure(call: Call<rewardsDto>, t: Throwable) {
                    Log.e("RewardsRepository", "Erreur réseau lors de l'ajout de la récompense: ${t.message}")
                    result.value = null
                }
            })
        } else {
            val newRewardMapped = mapRewardDtoToReward(newReward)
            scope.launch {
                recompenseDao.insertRecompenses(listOf(mapRewardToEntity(newRewardMapped)))
            }
            val currentRewards = _rewards.value?.toMutableList() ?: mutableListOf()
            currentRewards.add(newRewardMapped)
            _rewards.value = currentRewards

            // Retourner la récompense ajoutée
            result.value = newRewardMapped
            Log.d("RewardsRepository", "Récompense ajoutée localement (hors ligne)")
        }

        return result
    }

    fun buyRecompense(idRecompense: Int, requestBody: Map<String, Int>) {
        val result = MutableLiveData<Boolean>()
        if (NetworkUtils.isOnline(context)) {
            val call = rewardService.buyRecompense(idRecompense, requestBody)
            call.enqueue(object : Callback<rewardsDto> {
                override fun onResponse(call: Call<rewardsDto>, response: Response<rewardsDto>) {
                    if (response.isSuccessful) {
                        Log.d("RewardsRepository", "Récompense achetée avec succès")
                        result.value = true
                    } else {
                        Log.e("RewardsRepository", "Erreur lors de l'achat de la récompense: ${response.code()}")
                        result.value = false
                    }
                }

                override fun onFailure(call: Call<rewardsDto>, t: Throwable) {
                    Log.e("RewardsRepository", "Erreur réseau lors de l'achat de la récompense: ${t.message}")
                    result.value = false
                }
            })
        } else {
            Log.e("RewardsRepository", "Pas de connexion réseau disponible")
            result.value = false
        }
    }

    fun updateRecompense(id: Int, updatedReward: rewardsDto): LiveData<Recompense?> {
        val result = MutableLiveData<Recompense?>()
        if (NetworkUtils.isOnline(context)) {
            val call = rewardService.updateRecompense(id, updatedReward)

            call.enqueue(object : Callback<rewardsDto> {
                override fun onResponse(call: Call<rewardsDto>, response: Response<rewardsDto>) {
                    if (response.isSuccessful) {
                        val rewardResponse = response.body()
                        if (rewardResponse != null) {
                            val modifiedReward = mapRewardDtoToReward(rewardResponse)
                            val currentRewards = _rewards.value?.toMutableList() ?: mutableListOf()
                            val index = currentRewards.indexOfFirst { it.idRecompense == id }
                            if (index != -1) {
                                currentRewards[index] = modifiedReward
                                _rewards.value = currentRewards
                                result.value = modifiedReward
                                Log.d("RewardsRepository", "Récompense mise à jour avec succès")
                            }
                        } else {
                            Log.e("RewardsRepository", "Réponse vide lors de la mise à jour de la récompense")
                            result.value = null
                        }
                    } else {
                        Log.e("RewardsRepository", "Erreur lors de la mise à jour de la récompense: ${response.code()}")
                        result.value = null
                    }
                }

                override fun onFailure(call: Call<rewardsDto>, t: Throwable) {
                    Log.e("RewardsRepository", "Erreur réseau lors de la mise à jour de la récompense: ${t.message}")
                    result.value = null
                }
            })
        } else {
            Log.e("RewardsRepository", "Pas de connexion réseau disponible")
            result.value = null
        }
        return result
    }
    fun supprimerRecompense(id: Int): LiveData<Boolean> {
        val result = MutableLiveData<Boolean>()
        if (NetworkUtils.isOnline(context)) {
            val call = rewardService.deleteRecompense(id)

            call.enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Log.d("RewardsRepository", "Récompense supprimée avec succès")
                        result.value = true
                    } else {
                        Log.e("RewardsRepository", "Erreur lors de la suppression de la récompense: ${response.code()}")
                        result.value = false
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.e("RewardsRepository", "Erreur réseau lors de la suppression de la récompense: ${t.message}")
                    result.value = false
                }
            })
        } else {
            Log.e("RewardsRepository", "Pas de connexion réseau disponible")
            result.value = false
        }
        return result
    }



    private suspend fun saveRecompensesLocally(recompenses: List<Recompense>) {
        val recompenseEntities = recompenses.map {
            RecompenseEntity(
                idRecompense = it.idRecompense,
                nom = it.nom,
                cout = it.cout,
                description = it.description,
                stock = it.stock,
                estDisponible = it.estDisponible
            )
        }

        recompenseDao.insertRecompenses(recompenseEntities)
    }

    private fun loadRecompensesFromLocalDb() {
        val localRecompenses = recompenseDao.getAllRecompenses()
        localRecompenses.observeForever {
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

    private fun mapRewardToEntity(reward: Recompense): RecompenseEntity {
        return RecompenseEntity(
            idRecompense = reward.idRecompense,
            nom = reward.nom,
            cout = reward.cout,
            description = reward.description,
            stock = reward.stock,
            estDisponible = reward.estDisponible
        )
    }
}