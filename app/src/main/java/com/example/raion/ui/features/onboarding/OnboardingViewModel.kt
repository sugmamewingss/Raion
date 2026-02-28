package com.example.raion.ui.features.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.raion.data.local.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _navigateToAuth = MutableStateFlow(false)
    val navigateToAuth: StateFlow<Boolean> = _navigateToAuth.asStateFlow()

    fun onEvent(event: OnboardingEvent) {
        when (event) {
            is OnboardingEvent.SkipClicked -> {
                viewModelScope.launch {
                    userPreferences.saveOnboardingCompleted(true)
                    _navigateToAuth.value = true
                }
            }
            is OnboardingEvent.NextClicked -> {
                // UI logic to swipe pager handled in Compose locally.
                // If it's the last page, we navigate.
            }
            is OnboardingEvent.GetStartedClicked -> {
                viewModelScope.launch {
                    userPreferences.saveOnboardingCompleted(true)
                    _navigateToAuth.value = true
                }
            }
            is OnboardingEvent.NavigationHandled -> {
                _navigateToAuth.value = false
            }
        }
    }
}

sealed class OnboardingEvent {
    object SkipClicked : OnboardingEvent()
    object NextClicked : OnboardingEvent()
    object GetStartedClicked : OnboardingEvent()
    object NavigationHandled : OnboardingEvent()
}
