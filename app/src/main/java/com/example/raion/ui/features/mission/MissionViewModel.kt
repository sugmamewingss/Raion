package com.example.raion.ui.features.mission

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.raion.data.model.MissionStep
import com.example.raion.data.model.MissionUiState
import com.example.raion.data.repository.AuthRepository
import com.example.raion.data.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale
import android.util.Log
import javax.inject.Inject

@HiltViewModel
class MissionViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val homeRepository: HomeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MissionUiState())
    val uiState: StateFlow<MissionUiState> = _uiState.asStateFlow()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val profileDeferred = async { homeRepository.getUserProfile() }
            val rankDeferred = async { authRepository.getUserRank() }
            val missionDeferred = async { homeRepository.getActiveMissions() }
            val categoriesDeferred = async { homeRepository.getWasteCategories() }

            val profileResult = profileDeferred.await()
            val rankResult = rankDeferred.await()
            val missionResult = missionDeferred.await()
            val categoriesResult = categoriesDeferred.await()

            val profile = profileResult.getOrNull()
            val missions = missionResult.getOrNull()
            val categories = categoriesResult.getOrNull() ?: emptyList()

            val fullName = profile?.name?.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
            } ?: "Sobat Gobi"

            val activeMission = missions?.firstOrNull()

            val missionComplete = missions?.isEmpty() == true
            val target = activeMission?.targetProgress ?: 5

            _uiState.update {
                it.copy(
                    isLoading = false,
                    userName = fullName,
                    userLevel = profile?.level ?: 1,
                    userXp = profile?.totalXp ?: 0,
                    userCoins = profile?.coins ?: 0,
                    userRank = rankResult.getOrNull() ?: 5,
                    avatarUrl = profile?.currentAvatarUrl ?: "",
                    scannedCount = if (missionComplete) target else (activeMission?.currentProgress ?: 0),
                    targetCount = target,
                    isMissionComplete = missionComplete,
                    categories = categories
                )
            }
        }
    }

    // === Navigation Actions ===

    fun startCollecting() {
        _uiState.update { it.copy(step = MissionStep.INTRO) }
    }

    fun goToSelectType() {
        _uiState.update { it.copy(step = MissionStep.SELECT_TYPE) }
    }

    fun selectType(type: String) {
        _uiState.update {
            it.copy(
                selectedType = type,
                selectedSubtype = null,
                step = MissionStep.SELECT_SUBTYPE
            )
        }
    }

    fun selectSubtype(subtype: String) {
        _uiState.update {
            it.copy(
                selectedSubtype = subtype,
                step = MissionStep.SELECT_LOCATION
            )
        }
    }

    fun selectLocation(location: String) {
        _uiState.update {
            it.copy(
                selectedLocation = location,
                step = MissionStep.SELECT_QUANTITY
            )
        }
    }

    fun selectQuantityAndSubmit(quantity: Int) {
        _uiState.update { it.copy(selectedQuantity = quantity, isLoading = true) }
        submitEntry()
    }

    private fun submitEntry() {
        viewModelScope.launch {
            val state = _uiState.value
            val result = homeRepository.logWasteEntry(
                wasteType = state.selectedType ?: return@launch,
                wasteSubtype = state.selectedSubtype ?: return@launch,
                location = state.selectedLocation ?: return@launch,
                quantity = state.selectedQuantity
            )

            result.onSuccess { response ->
                Log.d("MissionVM", "logWasteEntry SUCCESS: status=${response.status}, scanned=${response.scannedCount}, target=${response.targetCount}, isCompleted=${response.isCompleted}, xp=${response.gainedXp}, coins=${response.gainedCoins}")

                if (response.status == "already_completed") {
                    // Misi hari ini sudah selesai sebelumnya
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            step = MissionStep.RESULT,
                            scannedCount = response.scannedCount,
                            isMissionComplete = true,
                            errorMessage = null
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            step = MissionStep.RESULT,
                            scannedCount = response.scannedCount,
                            isMissionComplete = response.isCompleted,
                            lastGainedXp = response.gainedXp,
                            lastGainedCoins = response.gainedCoins,
                            totalGainedXp = it.totalGainedXp + response.gainedXp,
                            totalGainedCoins = it.totalGainedCoins + response.gainedCoins,
                            errorMessage = null
                        )
                    }
                }
            }.onFailure { e ->
            Log.e("MissionVM", "logWasteEntry FAILED: ${e.message}", e)
            _uiState.update {
                    it.copy(
                        isLoading = false,
                        step = MissionStep.RESULT,
                        errorMessage = e.message ?: "Gagal menyimpan data"
                    )
                }
            }
        }
    }

    fun continueMission() {
        // Reset wizard selections, go back to INTRO for next entry
        _uiState.update {
            it.copy(
                step = MissionStep.INTRO,
                selectedType = null,
                selectedSubtype = null,
                selectedLocation = null,
                selectedQuantity = 1,
                lastGainedXp = 0,
                lastGainedCoins = 0
            )
        }
    }

    fun goBack() {
        _uiState.update {
            val prevStep = when (it.step) {
                MissionStep.INTRO -> MissionStep.JOURNEY
                MissionStep.SELECT_TYPE -> MissionStep.INTRO
                MissionStep.SELECT_SUBTYPE -> MissionStep.SELECT_TYPE
                MissionStep.SELECT_LOCATION -> MissionStep.SELECT_SUBTYPE
                MissionStep.SELECT_QUANTITY -> MissionStep.SELECT_LOCATION
                MissionStep.RESULT -> MissionStep.JOURNEY
                MissionStep.JOURNEY -> MissionStep.JOURNEY
            }
            it.copy(step = prevStep)
        }
    }
}
