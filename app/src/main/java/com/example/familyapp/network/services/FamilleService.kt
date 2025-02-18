package com.example.familyapp.network.services

import com.example.familyapp.network.dto.familleDto.FamilleDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface FamilleService {
    @GET("familles/{id}")
    fun getFamilles(@Path("id") id:Int): Call<List<FamilleDTO>>
}