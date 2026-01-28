package com.itspr.parcial2.repository

import com.itspr.parcial2.network.RetrofitClient;
import com.itspr.parcial2.model.Character

class CharacterRepository {
    suspend fun getCharacters(): List<Character> {
        return RetrofitClient.apiService.getCharacters()
    }
}

