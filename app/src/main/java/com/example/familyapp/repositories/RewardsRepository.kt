package com.example.familyapp.repositories

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.familyapp.data.model.recompense.Recompense
import com.example.familyapp.network.RetrofitClient
import com.example.familyapp.network.dto.rewardsDto.rewardsDto

import com.example.familyapp.network.mapper.mapRewardDtoToReward
import com.example.familyapp.network.services.RewardService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RewardsRepository(context: Context) {

    private val rewardService = RetrofitClient.instance.create(RewardService::class.java)

    private val _rewards = MutableLiveData<List<Recompense>>()

    val rewards: LiveData<List<Recompense>> get() = _rewards

    fun getRewards(idFamille: Int) {
        val call = rewardService.getRecompense(idFamille)

        call.enqueue(object : Callback<List<rewardsDto>> {
            override fun onResponse(
                call: Call<List<rewardsDto>>,
                response: Response<List<rewardsDto>>
            ) {

                if (response.isSuccessful) {
                    val response = response.body()
                    _rewards.value = response?.let {
                        it.map{ value ->
                            mapRewardDtoToReward(value)
                        }
                    }
                } else {
                    Log.e("TaskRepository", "Erreur HTTP : ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<rewardsDto>>, t: Throwable) {
                Log.e("TaskRepository", "Erreur réseau : ${t.message}")
            }
        })
    }

    companion object {
        fun ajouterRecompense(nom: String, description: String, cout: Int, stock: Int) {

        }

        fun updateRecompense(id: Int, nom: String, description: String, cout: Int, stock: Int) {

        }

        fun supprimerRecompense(id: Int) {

        }
    }
}