package com.itspr.parcial2.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    const val BASE_URL = "https://hp-api.onrender.com/api/"
    //indicar a que API se va usar
//
// https://hp-api.onrender.com/api/
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