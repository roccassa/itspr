package com.example.projecto_peli.ViewModel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projecto_peli.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AuthUiState(
    val isLoading: Boolean = false,
    val error: String = "",
    val isSuccess: Boolean = false
)

class AuthViewModel(private val repository: AuthRepository = AuthRepository()) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun login(email: String, password: String) {
        when {
            email.isBlank() -> {
                _uiState.value = _uiState.value.copy(error = "Por favor ingresa tu email")
                return
            }
            password.isBlank() -> {
                _uiState.value = _uiState.value.copy(error = "Por favor ingresa tu contraseña")
                return
            }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                _uiState.value = _uiState.value.copy(error = "Email inválido")
                return
            }
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = "")

            repository.login(email, password)
                .onSuccess {
                    _uiState.value = AuthUiState(isSuccess = true)
                }
                .onFailure {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Correo o contraseña incorrecta"
                    )
                }
        }
    }

    fun register(name: String, email: String, password: String, confirmPassword: String) {
        when {
            name.isBlank() -> {
                _uiState.value = _uiState.value.copy(error = "Por favor ingresa tu nombre")
                return
            }
            email.isBlank() -> {
                _uiState.value = _uiState.value.copy(error = "Por favor ingresa tu email")
                return
            }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                _uiState.value = _uiState.value.copy(error = "Email inválido")
                return
            }
            password.isBlank() -> {
                _uiState.value = _uiState.value.copy(error = "Por favor ingresa tu contraseña")
                return
            }
            password.length < 6 -> {
                _uiState.value = _uiState.value.copy(error = "La contraseña debe tener al menos 6 caracteres")
                return
            }
            confirmPassword.isBlank() -> {
                _uiState.value = _uiState.value.copy(error = "Por favor confirma tu contraseña")
                return
            }
            password != confirmPassword -> {
                _uiState.value = _uiState.value.copy(error = "Las contraseñas no coinciden")
                return
            }
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = "")

            repository.register(name, email, password)
                .onSuccess {
                    _uiState.value = AuthUiState(isSuccess = true)
                }
                .onFailure { exception ->
                    val errorMessage = when {
                        exception.message?.contains("email address is already in use") == true ->
                            "Este email ya está registrado"
                        else ->
                            "Error al registrar: ${exception.message}"
                    }
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = errorMessage
                    )
                }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = "")
    }

    fun resetState() {
        _uiState.value = AuthUiState()
    }
}