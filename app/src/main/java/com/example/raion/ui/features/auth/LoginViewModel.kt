package com.example.raion.ui.features.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.raion.data.repository.AuthRepository
import com.example.raion.data.local.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginState(
    val username: String = "",
    val usernameError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isLoginSuccess: Boolean = false
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginState())
    val uiState: StateFlow<LoginState> = _uiState.asStateFlow()

    fun updateUsername(username: String) {
        val formattedUsername = username.lowercase().replace(" ", "")
        _uiState.update { it.copy(username = formattedUsername, usernameError = null) }
    }

    fun updatePassword(password: String) {
        _uiState.update { it.copy(password = password, passwordError = null) }
    }

    fun submitLogin(rememberMe: Boolean) {
        val currentState = _uiState.value

        var hasError = false

        if (currentState.username.isBlank()) {
            _uiState.update { it.copy(usernameError = "Nama panggilan tidak boleh kosong") }
            hasError = true
        } else if (currentState.username.length < 3) {
            _uiState.update { it.copy(usernameError = "Nama panggilan minimal 3 huruf") }
            hasError = true
        }
        
        if (currentState.password.isBlank()) {
            _uiState.update { it.copy(passwordError = "Kata sandi tidak boleh kosong") }
            hasError = true
        }

        if (hasError) return

        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            val result = authRepository.loginCustomUser(
                username = currentState.username,
                password = currentState.password
            )

            result.fold(
                onSuccess = {
                    userPreferences.saveRememberMe(rememberMe)
                    _uiState.update { it.copy(isLoading = false, isLoginSuccess = true) }
                },
                onFailure = { exception ->
                    _uiState.update { 
                        it.copy(
                            isLoading = false, 
                            errorMessage = exception.message ?: "Kata sandi atau identitas salah."
                        ) 
                    }
                }
            )
        }
    }
}
