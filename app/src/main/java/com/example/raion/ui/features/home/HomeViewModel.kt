package com.example.raion.ui.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.raion.data.model.ActiveMission
import com.example.raion.data.model.EduArticle
import com.example.raion.data.model.PointShopItem
import com.example.raion.data.model.ShopCategory
import com.example.raion.data.model.UserProfile
import com.example.raion.data.model.UserInventoryItem
import com.example.raion.data.repository.AuthRepository
import com.example.raion.data.repository.HomeRepository
import com.example.raion.data.local.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

data class HomeUiState(
    val isLoading: Boolean = true,
    val userName: String = "Sobat", // Used for "Halo, {First Name}!"
    val username: String = "sobatgobi", // Used for "Nama Panggilan" in Edit Profile
    val fullName: String = "Sobat Gobi",
    val birthDate: String = "1 Januari 2010",
    val currentAvatarUrl: String = "", 
    val userLevel: Int = 1,
    val totalCoins: Int = 0,
    val totalXp: Int = 0,
    val xpForCurrentLevel: Int = 0,
    val xpForNextLevel: Int = 100,
    val streak: Int = 0,
    val isActive: Boolean = true,
    val isMissionCompletedToday: Boolean = false,

    // Core Game Data
    val activeMissions: List<ActiveMission> = emptyList(),
    val eduArticles: List<EduArticle> = emptyList(),
    val leaderboard: List<UserProfile> = emptyList(),
    val pointShopItems: List<PointShopItem> = emptyList(),
    val shopCategories: List<ShopCategory> = emptyList(),
    val userInventory: List<UserInventoryItem> = emptyList(),
    
    val errorMessage: String? = null
) {
    val levelProgressRatio: Float
        get() = if (xpForNextLevel > xpForCurrentLevel) {
            val progress = (totalXp - xpForCurrentLevel).toFloat() / (xpForNextLevel - xpForCurrentLevel)
            progress.coerceIn(0f, 1f)
        } else 0f

    val levelProgressText: String
        get() = "${(totalXp - xpForCurrentLevel).coerceAtLeast(0)}/${xpForNextLevel - xpForCurrentLevel} XP"
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val homeRepository: HomeRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _isLoggedOut = MutableStateFlow(false)
    val isLoggedOut: StateFlow<Boolean> = _isLoggedOut.asStateFlow()
    
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadHomeData()
    }

    /** Optimistic UI: apply mission results locally, then background sync */
    fun applyMissionResult(gainedXp: Int, gainedCoins: Int, newProgress: Int, isComplete: Boolean) {
        _uiState.update { state ->
            // Per-entry rewards from RPC
            var bonusXp = gainedXp
            var bonusCoins = gainedCoins

            // Add mission completion bonus (same as DB trigger: +50 XP, +10 coins)
            if (isComplete) {
                bonusXp += 50
                bonusCoins += 10
            }

            val newTotalXp = state.totalXp + bonusXp
            val newCoins = state.totalCoins + bonusCoins
            // Recalculate level locally (same formula as DB: level = total_xp / 100 + 1)
            val newLevel = (newTotalXp / 100) + 1

            val updatedMissions = if (isComplete) {
                emptyList()
            } else {
                state.activeMissions.map { mission ->
                    mission.copy(currentProgress = newProgress)
                }.ifEmpty {
                    listOf(ActiveMission(title = "Membuang sampah", currentProgress = newProgress, targetProgress = 5))
                }
            }

            state.copy(
                totalXp = newTotalXp,
                totalCoins = newCoins,
                userLevel = newLevel,
                xpForCurrentLevel = (newLevel - 1) * 100,
                xpForNextLevel = newLevel * 100,
                streak = if (isComplete) state.streak + 1 else state.streak,
                isMissionCompletedToday = isComplete,
                activeMissions = updatedMissions
            )
        }

        // Background sync: silently correct any discrepancies with server
        viewModelScope.launch {
            kotlinx.coroutines.delay(500) // Small delay to let DB triggers complete
            val profileResult = homeRepository.getUserProfile()
            profileResult.onSuccess { profile ->
                _uiState.update { state ->
                    state.copy(
                        totalXp = profile.totalXp,
                        totalCoins = profile.coins,
                        userLevel = profile.level,
                        xpForCurrentLevel = (profile.level - 1) * 100,
                        xpForNextLevel = profile.level * 100,
                        streak = profile.currentStreak
                    )
                }
            }
        }
    }

    /** Full refresh from server (used on login, pull-to-refresh, etc.) */
    fun refreshData() {
        loadHomeData()
    }

    private fun loadHomeData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            // Fetch all dashboard data concurrently
            val profileDeferred = async { homeRepository.getUserProfile() }
            val missionsDeferred = async { homeRepository.getActiveMissions() }
            val articlesDeferred = async { homeRepository.getEducationalArticles() }
            val leaderboardDeferred = async { homeRepository.getTopPlayerProfiles() }
            val shopDeferred = async { homeRepository.getPointShopItems() }
            val categoriesDeferred = async { homeRepository.getShopCategories() }
            val inventoryDeferred = async { homeRepository.getUserInventory() }

            val profileResult = profileDeferred.await()
            val missionsResult = missionsDeferred.await()
            val articlesResult = articlesDeferred.await()
            val leaderboardResult = leaderboardDeferred.await()
            val shopResult = shopDeferred.await()
            val categoriesResult = categoriesDeferred.await()
            val inventoryResult = inventoryDeferred.await()

            if (profileResult.isSuccess) {
                val profile = profileResult.getOrThrow()
                val firstName = profile.name.split(" ").firstOrNull()?.replaceFirstChar { 
                    if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() 
                } ?: profile.username

                val formattedBirthDate = try {
                    if (profile.birthDate != null) {
                        val date = java.time.LocalDate.parse(profile.birthDate)
                        val formatter = java.time.format.DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.Builder().setLanguage("id").setRegion("ID").build())
                        date.format(formatter)
                    } else "Belum diatur"
                } catch (e: Exception) {
                    profile.birthDate ?: "Belum diatur"
                }

                val todayStr = java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ISO_LOCAL_DATE)
                val completedTodayMissions = profile.lastMissionCompletedDate == todayStr 
                    || (missionsResult.isSuccess && missionsResult.getOrNull()?.isEmpty() == true)

                _uiState.update { state ->
                    state.copy(
                        isLoading = false,
                        userName = firstName,
                        username = profile.username,
                        fullName = profile.name,
                        birthDate = formattedBirthDate,
                        currentAvatarUrl = if (profile.currentAvatarUrl.isNullOrEmpty()) "https://nnloirkwladlazxgpgrm.supabase.co/storage/v1/object/public/avatars/dino_default.png" else profile.currentAvatarUrl,
                        userLevel = profile.level,
                        totalCoins = profile.coins,
                        totalXp = profile.totalXp,
                        xpForCurrentLevel = (profile.level - 1) * 100,
                        xpForNextLevel = profile.level * 100,
                        streak = profile.currentStreak,
                        isActive = true,
                        isMissionCompletedToday = completedTodayMissions,

                        activeMissions = missionsResult.getOrNull() ?: emptyList(),
                        eduArticles = articlesResult.getOrNull() ?: emptyList(),
                        leaderboard = leaderboardResult.getOrNull() ?: emptyList(),
                        pointShopItems = shopResult.getOrNull() ?: emptyList(),
                        shopCategories = categoriesResult.getOrNull() ?: emptyList(),
                        userInventory = inventoryResult.getOrNull() ?: emptyList()
                    )
                }
            } else {
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        errorMessage = "Gagal memuat profil pahlawanmu. Periksa koneksi internet."
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

    // =========================================================================
    // Shop Actions
    // =========================================================================

    fun purchaseShopItem(itemId: String) {
        viewModelScope.launch {
            val result = homeRepository.buyShopItem(itemId)
            if (result.isSuccess) {
                // Auto-equip directly after purchase
                val equipResult = homeRepository.equipShopItem(itemId)
                if (equipResult.isFailure) {
                    val errorMsg = equipResult.exceptionOrNull()?.message ?: "Berhasil dibeli tetapi gagal dipakai"
                    val cleanMsg = if (errorMsg.contains("message")) {
                        errorMsg.substringAfter("\"message\":\"").substringBefore("\"").ifEmpty { errorMsg }
                    } else errorMsg
                    _uiState.update { it.copy(errorMessage = cleanMsg) }
                }
                
                // Refresh data to update coins, user inventory, and the newly equipped avatar
                loadHomeData()
            } else {
                _uiState.update { 
                    it.copy(errorMessage = result.exceptionOrNull()?.message ?: "Gagal membeli barang") 
                }
            }
        }
    }

    fun equipShopItem(itemId: String) {
        viewModelScope.launch {
            val result = homeRepository.equipShopItem(itemId)
            if (result.isSuccess) {
                // Refresh data to update currentAvatarUrl
                loadHomeData()
            } else {
                val errorMsg = result.exceptionOrNull()?.message ?: "Gagal mengganti avatar"
                // Extract clean message if it's a Supabase error wrapping the DB exception
                val cleanMsg = if (errorMsg.contains("message")) {
                    errorMsg.substringAfter("\"message\":\"").substringBefore("\"").ifEmpty { errorMsg }
                } else errorMsg
                
                _uiState.update { it.copy(errorMessage = cleanMsg) }
            }
        }
    }
}
