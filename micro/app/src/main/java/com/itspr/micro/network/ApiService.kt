package com.itspr.micro.network

import retrofit2.http.*
import com.itspr.micro.model.ZipCodeResponse

// Importar el nuevo modelo

interface ApiService {

    // 1. Endpoint para buscar información geográfica por Código Postal
    // La estructura de la URL es: /mx/{codigo_postal}
    @GET("mx/{cp}")
    suspend fun getZipCodeInfo(
        @Path("cp") cp: String // El CP se inyecta en el path
    ): ZipCodeResponse

}