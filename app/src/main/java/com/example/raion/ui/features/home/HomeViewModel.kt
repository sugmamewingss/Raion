package com.example.raion.ui.features.home

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
import java.util.Locale
import javax.inject.Inject
import android.util.Log

data class LeaderboardEntry(
    val name: String,
    val points: String,
    val title: String,
    val rank: String,
    val isPrimary: Boolean = false,
    val isSecondary: Boolean = false
)

data class ShopItemData(
    val price: String
)

data class HomeUiState(
    val userName: String = "",
    val userLevel: Int = 0,
    val currentPoints: Int = 0,
    val totalXp: Int = 0, // Lifetime XP aktual di database
    val xpForCurrentLevel: Int = 0, // Syarat batas bawah level saat ini
    val xpForNextLevel: Int = 100, // Syarat batas atas (Next Level)
    val streak: Int = 0,
    val incompleteTasks: List<String> = emptyList(),
    val organicCount: Int = 0,
    val inorganicCount: Int = 0,
    val leaderboard: List<LeaderboardEntry> = emptyList(),
    val shopItems: List<ShopItemData> = listOf(
        ShopItemData("20 Poin"),
        ShopItemData("10 Poin"),
        ShopItemData("30 Poin"),
        ShopItemData("40 Poin")
    )
) {
    // BEST PRACTICE: Progress Ratio dihitung secara relatif
    val levelProgressRatio: Float
        get() = if (xpForNextLevel > xpForCurrentLevel) {
            (totalXp - xpForCurrentLevel).toFloat() / (xpForNextLevel - xpForCurrentLevel)
        } else 0f

    // BEST PRACTICE: Teks progress menampilkan sisa relatif ke level berikutnya
    val levelProgressText: String
        get() = "${totalXp - xpForCurrentLevel} / ${xpForNextLevel - xpForCurrentLevel} XP"
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _isLoggedOut = MutableStateFlow(false)
    val isLoggedOut: StateFlow<Boolean> = _isLoggedOut.asStateFlow()
    
    // UI State for HomeScreen content
    private val _uiState = MutableStateFlow(HomeUiState(userName = "Memuat..."))
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        fetchUserData()
    }

    private fun fetchUserData() {
        viewModelScope.launch {
            // Panggil trigger Streak harian setiap kali Home dibuka
            authRepository.updateDailyStreak()

            val profileResult = authRepository.getUserProfile()
            val leaderboardResult = authRepository.getTopPlayerProfiles(3)
            val tasksResult = authRepository.getIncompleteDailyTasks()
            val wasteCountsResult = authRepository.getOrganicInorganicCounts()
            
            val fetchedPlayers = leaderboardResult.getOrNull()?.map { user ->
                val name = user.name.split(" ").firstOrNull()?.replaceFirstChar { 
                    if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() 
                } ?: user.username
                
                val title = "Level ${user.level} Player"
                
                LeaderboardEntry(
                    name = name,
                    points = "${user.totalXp} XP",
                    title = title,
                    rank = "", // will be calculated below
                    isPrimary = false,
                    isSecondary = false
                )
            } ?: emptyList()
            
            // Hitung ulang ranking (1st, 2nd, 3rd) dan warna medali (Primary/Secondary)
            val newLeaderboard = fetchedPlayers.take(3).mapIndexed { index, entry ->
                val rankStr = when (index) {
                    0 -> "1st"
                    1 -> "2nd"
                    2 -> "3rd"
                    else -> "${index + 1}th"
                }
                entry.copy(
                    rank = rankStr,
                    isPrimary = index == 0,
                    isSecondary = index == 1
                )
            }
            
            if (profileResult.isSuccess) {
                val profile = profileResult.getOrNull()
                if (profile != null) {
                    val firstName = profile.name.split(" ").firstOrNull()?.replaceFirstChar { 
                        if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() 
                    } ?: profile.username

                    // Asumsi 1 level = 100 XP
                    val currentBaseXp = (profile.level - 1) * 100
                    val nextBaseXp = profile.level * 100

                    val incompleteTasks = tasksResult.getOrNull()?.map { it.title } ?: emptyList()
                    val (organic, inorganic) = wasteCountsResult.getOrNull() ?: Pair(0, 0)

                    _uiState.update { 
                        it.copy(
                            userName = firstName,
                            userLevel = profile.level,
                            currentPoints = profile.coins,
                            totalXp = profile.totalXp,
                            xpForCurrentLevel = currentBaseXp,
                            xpForNextLevel = nextBaseXp,
                            streak = profile.currentStreak,
                            incompleteTasks = incompleteTasks,
                            leaderboard = newLeaderboard,
                            organicCount = organic,
                            inorganicCount = inorganic
                        ) 
                    }
                }
            } else {
                // Fallback kalau error
                val name = authRepository.getLoggedInUserName()
                _uiState.update { 
                    it.copy(
                        userName = name, 
                        leaderboard = newLeaderboard,
                        incompleteTasks = tasksResult.getOrNull()?.map { it.title } ?: emptyList()
                    ) 
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            userPreferences.saveRememberMe(false)
            _isLoggedOut.value = true
        }
    }
}
