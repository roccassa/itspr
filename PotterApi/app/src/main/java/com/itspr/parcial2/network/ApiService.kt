package com.itspr.parcial2.network

import retrofit2.http.*
import com.itspr.parcial2.model.Character


interface ApiService {
    @GET("characters")
    suspend fun getCharacters(): List<Character>
}