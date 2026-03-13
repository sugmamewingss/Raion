package com.example.raion.ui.features.diary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.raion.data.model.DailyHistory
import com.example.raion.data.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DiaryUiState(
    val selectedDateHistory: DailyHistory? = null,
    val isLoadingHistory: Boolean = false,
    val historyError: String? = null
)

@HiltViewModel
class DiaryViewModel @Inject constructor(
    private val homeRepository: HomeRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(DiaryUiState())
    val uiState: StateFlow<DiaryUiState> = _uiState.asStateFlow()

    fun loadHistoryForDate(dateStr: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoadingHistory = true, 
                historyError = null,
                selectedDateHistory = null
            )
            val result = homeRepository.getDailyHistory(dateStr)
            result.onSuccess { history ->
                _uiState.value = _uiState.value.copy(
                    selectedDateHistory = history,
                    isLoadingHistory = false
                )
            }.onFailure { e ->
                _uiState.value = _uiState.value.copy(
                    isLoadingHistory = false,
                    historyError = e.message
                )
            }
        }
    }
    
    fun clearSelectedHistory() {
        _uiState.value = _uiState.value.copy(selectedDateHistory = null)
    }
}
