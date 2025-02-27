package com.example.familyapp.repositories

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.familyapp.MainApplication
import com.example.familyapp.app_utils.NetworkUtils
import com.example.familyapp.data.model.Famille.Famille
import com.example.familyapp.db.entities.FamilleEntity

import com.example.familyapp.network.RetrofitClient
import com.example.familyapp.network.dto.familleDto.FamilleDto
import com.example.familyapp.network.mapper.mapFamilleDtoToFamille
import com.example.familyapp.network.services.FamilleService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FamilleRepository(context: Context) {
    private val familleService = RetrofitClient.instance.create(FamilleService::class.java)
    private val db = MainApplication.database
    private val familleDao = db.familleDao()
    private val context = context
    private val scope = CoroutineScope(SupervisorJob())
    private val _familles = MutableLiveData<List<Famille>>()
    val familles: LiveData<List<Famille>> get() = _familles

    fun getFamilles(idFamille: Int) {
        val call = familleService.getFamilles(idFamille)
        if (NetworkUtils.isOnline(context)) {
            call.enqueue(object : Callback<List<FamilleDto>> {
                override fun onResponse(
                    call: Call<List<FamilleDto>>,
                    response: Response<List<FamilleDto>>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        responseBody?.let {
                            // Convertir les DTO en modèles
                            val familles = it.map { familleDto ->
                                mapFamilleDtoToFamille(familleDto)
                            }
                            _familles.value = familles

                            scope.launch {
                                saveFamillesLocally(familles)
                            }
                        }
                    } else {
                        Log.e("FamilleRepository", "Erreur HTTP : ${response.code()}")
                        loadFamillesFromLocalDb(idFamille)
                    }
                }

                override fun onFailure(call: Call<List<FamilleDto>>, t: Throwable) {
                    Log.e("FamilleRepository", "Erreur réseau : ${t.message}")
                }
            })

        } else {

            loadFamillesFromLocalDb(idFamille)
        }
    }
    private suspend fun saveFamillesLocally(familles: List<Famille>) {
        val familleEntities = familles.map {
            FamilleEntity(
                nom = it.nom,
                dateCreation = it.dateCreation,
                idFamille = it.idFamille,
                codeInvitation = it.codeInvitation,
            )
        }

        familleDao.insertFamilles(familleEntities)
    }

    private fun loadFamillesFromLocalDb(idFamille: Int) {
        val localFamille = familleDao.getFamilleById(idFamille)
        localFamille.observeForever { familleEntity ->
            if (familleEntity != null) {
                val famille = Famille(
                    nom = familleEntity.nom,
                    dateCreation = familleEntity.dateCreation,
                    idFamille = familleEntity.idFamille,
                    codeInvitation = familleEntity.codeInvitation,
                )
                _familles.value = listOf(famille)
            }
        }
    }

}