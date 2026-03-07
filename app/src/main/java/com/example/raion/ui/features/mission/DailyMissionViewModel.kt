package com.example.raion.ui.features.mission

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.raion.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

data class DailyMissionUiState(
    val userName: String = "Memuat...",
    val schoolInfo: String = "",
    val level: Int = 0,
    val totalXp: Int = 0,
    val xpInCurrentLevel: Int = 0, // XP progress within current 100 XP bracket
    val xpProgressRatio: Float = 0f,
    val xpProgressText: String = "0/100 XP",
    val coins: Int = 0,
    val rank: Int = 0,
    val title: String = "Si Paling Tertib",
    val totalMissionTarget: Int = 10, // 5 organik + 5 anorganik
    val completedMissionCount: Int = 0
)

@HiltViewModel
class DailyMissionViewModel @Inject constructor(
    internal val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DailyMissionUiState())
    val uiState: StateFlow<DailyMissionUiState> = _uiState.asStateFlow()

    init {
        fetchData()
    }

    private fun fetchData() {
        viewModelScope.launch {
            val profileResult = authRepository.getUserProfile()
            val rankResult = authRepository.getUserRank()
            val missionProgressResult = authRepository.getDailyMissionProgress()

            if (profileResult.isSuccess) {
                val profile = profileResult.getOrNull() ?: return@launch

                val fullName = profile.name
                val school = calculateSchool(profile.birthDate)
                val level = profile.level
                val totalXp = profile.totalXp
                val coins = profile.coins
                val rank = rankResult.getOrNull() ?: 0

                // XP within current level bracket (each level = 100 XP)
                val currentBaseXp = (level - 1).coerceAtLeast(0) * 100
                val xpInLevel = totalXp - currentBaseXp
                val xpRatio = (xpInLevel.toFloat() / 100f).coerceIn(0f, 1f)

                // Mission progress
                val (completed, target) = missionProgressResult.getOrNull() ?: Pair(0, 10)

                _uiState.update {
                    it.copy(
                        userName = fullName,
                        schoolInfo = school,
                        level = level,
                        totalXp = totalXp,
                        xpInCurrentLevel = xpInLevel,
                        xpProgressRatio = xpRatio,
                        xpProgressText = "$xpInLevel/100 XP",
                        coins = coins,
                        rank = rank,
                        title = "Si Paling Tertib",
                        totalMissionTarget = target,
                        completedMissionCount = completed
                    )
                }
            }
        }
    }

    private fun calculateSchool(birthDateStr: String?): String {
        if (birthDateStr.isNullOrBlank()) return ""

        return try {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            val birthDate = sdf.parse(birthDateStr) ?: return ""
            val today = Calendar.getInstance()
            val birth = Calendar.getInstance().apply { time = birthDate }

            var age = today.get(Calendar.YEAR) - birth.get(Calendar.YEAR)
            if (today.get(Calendar.DAY_OF_YEAR) < birth.get(Calendar.DAY_OF_YEAR)) {
                age--
            }

            when {
                age >= 19 -> "Berkuliah"
                age >= 16 -> {
                    val grade = age - 15
                    "$grade - Sekolah Menengah Atas"
                }
                age >= 13 -> {
                    val grade = age - 12
                    "$grade - Sekolah Menengah Pertama"
                }
                age >= 7 -> {
                    val grade = age - 6
                    "$grade - Sekolah Dasar"
                }
                else -> "Pra-Sekolah"
            }
        } catch (e: Exception) {
            ""
        }
    }
}
