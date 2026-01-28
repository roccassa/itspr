package com.itspr.micro.network


import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // CAMBIO: URL BASE para la nueva API de códigos postales
    const val ZIP_CODE_BASE_URL = "https://api.zippopotam.us/"

    // Cliente específico para la API de CP
    val zipCodeService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(ZIP_CODE_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

}