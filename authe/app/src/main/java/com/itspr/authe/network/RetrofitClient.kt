package com.itspr.authe.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    //const val BASE_URL = "https://rickandmortyapi.com/api/"
    //simpson https://thesimpsonsapi.com/api/characters/
    //const val BASE_URL = "https://hp-api.onrender.com/api/"
    const val BASE_URL = "https://fakestoreapi.com/"

    //implementacion del servicio
    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL) //url de la api
            .addConverterFactory(GsonConverterFactory.create())
            //castear json a texto
            .build()
            .create(ApiService::class.java)
    }
}