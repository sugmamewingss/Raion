package com.example.raion.ui.features.onboarding

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class OnboardingViewModel : ViewModel() {

    private val _navigateToAuth = MutableStateFlow(false)
    val navigateToAuth: StateFlow<Boolean> = _navigateToAuth.asStateFlow()

    fun onEvent(event: OnboardingEvent) {
        when (event) {
            is OnboardingEvent.SkipClicked -> {
                _navigateToAuth.value = true
            }
            is OnboardingEvent.NextClicked -> {
                // UI logic to swipe pager handled in Compose locally.
                // If it's the last page, we navigate.
            }
            is OnboardingEvent.GetStartedClicked -> {
                _navigateToAuth.value = true
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
