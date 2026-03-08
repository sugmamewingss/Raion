package com.example.raion.ui.features.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.raion.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class EditProfileUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditProfileUiState())
    val uiState: StateFlow<EditProfileUiState> = _uiState.asStateFlow()

    fun saveProfile(newName: String, newBirthDate: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            // Update user profile ke Supabase
            val result = authRepository.updateUserProfile(newName, newBirthDate)
            
            if (result.isSuccess) {
                _uiState.update { it.copy(isLoading = false, isSuccess = true) }
            } else {
                _uiState.update { 
                    it.copy(
                        isLoading = false, 
                        errorMessage = "Gagal menyimpan: ${result.exceptionOrNull()?.message ?: "Terjadi kesalahan"}"
                    ) 
                }
            }
        }
    }
    
    fun resetState() {
        _uiState.update { it.copy(isSuccess = false, errorMessage = null) }
    }
}
