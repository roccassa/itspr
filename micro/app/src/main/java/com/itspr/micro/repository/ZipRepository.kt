package com.itspr.micro.repository

import com.itspr.micro.model.ZipCodeResponse
import com.itspr.micro.network.RetrofitClient

class ZipCodeRepository {
    // Llama a la función de la interfaz de Retrofit
    suspend fun getZipCodeInfo(cp: String): ZipCodeResponse {
        return RetrofitClient.zipCodeService.getZipCodeInfo(cp)
    }
}