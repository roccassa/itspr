package com.itspr.micro.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itspr.micro.model.ZipCodeResponse
import com.itspr.micro.repository.ZipCodeRepository
import kotlinx.coroutines.launch

class ZipCodeViewModel(
    private val repository: ZipCodeRepository = ZipCodeRepository()
) : ViewModel() {

    // Estado para almacenar la respuesta del CP
    var zipCodeData by mutableStateOf<ZipCodeResponse?>(null)
        private set

    // Estado para el mensaje de error
    var errorMessage by mutableStateOf<String?>(null)
        private set

    // Estado de carga
    var isLoading by mutableStateOf(false)
        private set

    fun searchZipCode(cp: String) {
        // Validación básica del CP (debe ser un número de 5 dígitos)
        if (cp.length != 5 || cp.toIntOrNull() == null) {
            errorMessage = "Ingresa un código postal válido de 5 dígitos."
            return
        }

        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            zipCodeData = null // Limpiar resultados anteriores

            try {
                // Llamada a la API
                val result = repository.getZipCodeInfo(cp)
                zipCodeData = result
            } catch (e: Exception) {
                errorMessage = "Error al buscar el CP: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }
}