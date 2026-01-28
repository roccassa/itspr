package com.itspr.parcial2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itspr.parcial2.repository.CharacterRepository
import com.itspr.parcial2.model.Character
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CharacterViewModel : ViewModel() {
    private val repository = CharacterRepository()

    private val _characters = MutableStateFlow<List<Character>>(emptyList())

    val character: StateFlow<List<Character>> = _characters

    init {
        fetchCharacter()
    }

    private fun fetchCharacter() {
        viewModelScope.launch {
            try {
                _characters.value = repository.getCharacters()
            } catch (e: Exception) {
                _characters.value = emptyList()
            }
        }
    }
}


