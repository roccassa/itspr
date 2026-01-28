package com.itspr.authe.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itspr.authe.repository.ApiRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.itspr.authe.model.Character
import com.itspr.authe.model.Product

/*
class CharacterViewModel : ViewModel() {
    private val repository = ApiRepository()

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
*/
class ProductViewModel : ViewModel() {
    private val repository = ApiRepository()

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products

    init {
        fetchProducts()
    }

    private fun fetchProducts() {
        viewModelScope.launch {
            try {
                _products.value = repository.getProducts()
            } catch (e: Exception) {
                _products.value = emptyList()
            }
        }
    }
}
