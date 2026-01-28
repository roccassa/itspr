package com.itspr.authe.repository

import com.itspr.authe.network.RetrofitClient
import com.itspr.authe.model.Character
import com.itspr.authe.model.Product

//HP
/*
class ApiRepository {

    suspend fun getCharacters(): List<Character> {
        return RetrofitClient.apiService.getCharacter()
    }
}
*/



//RCIKY MORTY
/*
class ApiRepository {

    suspend fun getCharacters(): List<Character> {
        return RetrofitClient.apiService.getCharacter().results
    }
}
*/

//FAKE STORE
class ApiRepository {
    suspend fun getProducts(): List<Product> {
        return RetrofitClient.apiService.getProducts()
    }
}
