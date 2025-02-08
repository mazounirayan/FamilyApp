package com.example.familyapp.repositories

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

import com.example.familyapp.data.model.famille.Famille
import com.example.familyapp.network.RetrofitClient
import com.example.familyapp.network.dto.familleDto.FamilleDto
import com.example.familyapp.network.mapper.mapFamilleDtoToFamille
import com.example.familyapp.network.services.FamilleService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FamilleRepository(context: Context) {
    private val familleService = RetrofitClient.instance.create(FamilleService::class.java)

    private val _familles = MutableLiveData<List<Famille>>()
    val familles: LiveData<List<Famille>> get() = _familles

    fun getFamilles(idFamille: Int) {
        val call = familleService.getFamilles(idFamille)

        call.enqueue(object : Callback<List<FamilleDto>> {
            override fun onResponse(
                call: Call<List<FamilleDto>>,
                response: Response<List<FamilleDto>>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    _familles.value = responseBody?.map { familleDto ->
                        mapFamilleDtoToFamille(familleDto)
                    }
                } else {
                    Log.e("FamilleRepository", "Erreur HTTP : ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<FamilleDto>>, t: Throwable) {
                Log.e("FamilleRepository", "Erreur réseau : ${t.message}")
            }
        })
    }
}