package com.example.raion.ui.features.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.raion.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.example.raion.data.local.UserPreferences
import javax.inject.Inject

data class RegisterState(
    val name: String = "",
    val nameError: String? = null,
    val birthDate: String = "",
    val birthDateError: String? = null,
    val username: String = "",
    val usernameError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
    val confirmPassword: String = "",
    val confirmPasswordError: String? = null,
    val isCheckingUsername: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isRegistrationSuccess: Boolean = false
)

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterState())
    val uiState: StateFlow<RegisterState> = _uiState.asStateFlow()

    fun updateName(name: String) {
        _uiState.update { it.copy(name = name, nameError = null) }
    }

    fun updateBirthDate(birthDate: String) {
        _uiState.update { it.copy(birthDate = birthDate, birthDateError = null) }
    }

    fun updateUsername(username: String) {
        val formatted = username.lowercase().replace(" ", "")
        _uiState.update { it.copy(username = formatted, usernameError = null) }
    }

    fun updatePassword(password: String) {
        _uiState.update { it.copy(password = password, passwordError = null) }
    }

    fun updateConfirmPassword(confirmPassword: String) {
        _uiState.update { it.copy(confirmPassword = confirmPassword, confirmPasswordError = null) }
    }

    private val _currentStep = MutableStateFlow(1)
    val currentStep: StateFlow<Int> = _currentStep.asStateFlow()

    fun nextStep() {
        val currentState = _uiState.value
        
        when (_currentStep.value) {
            3 -> {
                if (currentState.name.isBlank() || currentState.name.length < 3) {
                    _uiState.update { it.copy(nameError = "Nama harus terdiri dari minimal 3 karakter") }
                    return
                }
            }
            4 -> {
                if (currentState.birthDate.isBlank()) {
                    _uiState.update { it.copy(birthDateError = "Silakan pilih tanggal lahirmu") }
                    return
                }
            }
            5 -> {
                if (currentState.username.isBlank()) {
                    _uiState.update { it.copy(usernameError = "Nama panggilan tidak boleh kosong") }
                    return
                } else if (currentState.username.length < 3) {
                    _uiState.update { it.copy(usernameError = "Nama panggilan minimal 3 huruf") }
                    return
                }
            }
            6 -> {
                if (currentState.password.length < 8) {
                    _uiState.update { it.copy(passwordError = "Kata sandi minimal 8 karakter") }
                    return
                }
            }
        }

        if (_currentStep.value == 5) {
            _uiState.update { it.copy(isCheckingUsername = true) }
            viewModelScope.launch {
                val result = authRepository.checkUsernameAvailable(currentState.username)
                _uiState.update { it.copy(isCheckingUsername = false) }
                
                result.onSuccess { isAvailable ->
                    if (isAvailable) {
                        _currentStep.value += 1
                    } else {
                        _uiState.update { it.copy(usernameError = "Sayang sekali, username ini sudah terpakai.") }
                    }
                }.onFailure {
                    _uiState.update { it.copy(usernameError = "Gagal memeriksa username. Coba lagi.") }
                }
            }
        } else {
            if (_currentStep.value < 8) {
                _currentStep.value += 1
            }
        }
    }

    fun previousStep() {
        if (_currentStep.value > 1) {
            _currentStep.value -= 1
        }
    }

    fun resetStep() {
        _currentStep.value = 1
        _uiState.value = RegisterState()
    }

    fun submitRegistration() {
        val currentState = _uiState.value
        
        if (currentState.confirmPassword.isEmpty()) {
            _uiState.update { it.copy(confirmPasswordError = "Konfirmasi kata sandi tidak boleh kosong") }
            return
        }
        
        if (currentState.password != currentState.confirmPassword) {
            _uiState.update { it.copy(confirmPasswordError = "Kata sandi tidak cocok!") }
            return
        }

        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            val result = authRepository.registerCustomUser(
                name = currentState.name,
                birthDate = currentState.birthDate,
                username = currentState.username,
                password = currentState.password
            )

            result.fold(
                onSuccess = {
                    userPreferences.saveRememberMe(true)
                    _uiState.update { it.copy(isLoading = false, isRegistrationSuccess = true) }
                    _currentStep.value = 8
                },
                onFailure = { exception ->
                    _uiState.update { 
                        it.copy(
                            isLoading = false, 
                            errorMessage = exception.message ?: "Terjadi kesalahan saat mendaftar"
                        ) 
                    }
                }
            )
        }
    }
}
