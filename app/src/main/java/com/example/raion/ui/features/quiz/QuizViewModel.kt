package com.example.raion.ui.features.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.raion.data.local.UserPreferences
import com.example.raion.data.model.UserProfile
import com.example.raion.data.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class QuizUiState(
    val isLoading: Boolean = true,
    val userProfile: UserProfile? = null,
    val userRank: Int = 0,
    val quizProgress: Int = 0, // Soal Benar
    val errorMessage: String? = null
)

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _uiState = MutableStateFlow(QuizUiState())
    val uiState: StateFlow<QuizUiState> = _uiState.asStateFlow()

    init {
        loadQuizData()
    }

    private fun loadQuizData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            
            val profileResult = homeRepository.getUserProfile()
            // Fetch top 100 to determine user's rank
            val leaderboardResult = homeRepository.getTopPlayerProfiles(limit = 100)
            
            // Get quiz progress (Soal Benar) from datastore
            val savedProgress = userPreferences.quizProgress.first()

            if (profileResult.isSuccess && leaderboardResult.isSuccess) {
                val profile = profileResult.getOrNull()
                val leaderboard = leaderboardResult.getOrDefault(emptyList())
                val rank = leaderboard.indexOfFirst { it.id == profile?.id } + 1
                
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        userProfile = profile,
                        userRank = if (rank > 0) rank else 0,
                        quizProgress = savedProgress
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Gagal memuat data",
                        quizProgress = savedProgress
                    )
                }
            }
        }
    }

    // Use this function if we need to update "Soal Benar" progress later
    fun updateQuizProgress(newProgress: Int) {
        viewModelScope.launch {
            userPreferences.saveQuizProgress(newProgress)
            _uiState.update { it.copy(quizProgress = newProgress) }
        }
    }
}
