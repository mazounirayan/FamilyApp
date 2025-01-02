package com.example.familyapp.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient   {
    private val BASE_URL = "https://androidfamilyapi.onrender.com/"

    private val okHttpClient = OkHttpClient.Builder().build()

    val instance: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build();

}