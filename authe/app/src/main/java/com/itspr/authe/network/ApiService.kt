package com.itspr.authe.network

import retrofit2.http.GET
import com.itspr.authe.model.Character
import com.itspr.authe.model.CharacterResponse
import com.itspr.authe.model.Product

//HP
/*
interface ApiService {
    @GET("characters")
    suspend fun getCharacter(): List<Character>
}
*/
//RICKY MORTY
/*
interface ApiService {
    @GET("character")//parametro de referencia para saber que jalara
    suspend fun getCharacter(): CharacterResponse //funcion suspendida que en algun momento se manda a llamar (activa)

}*/

//FAKE STORE
interface ApiService {
    @GET("products")
    suspend fun getProducts(): List<Product>
}

