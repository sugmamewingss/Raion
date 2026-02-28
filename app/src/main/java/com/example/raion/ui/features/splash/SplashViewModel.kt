package com.example.raion.ui.features.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.raion.data.local.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
import io.github.jan.supabase.exceptions.RestException

sealed class SplashRoute {
    object Onboarding : SplashRoute()
    object AuthSelection : SplashRoute()
    object Home : SplashRoute()
    object Idle : SplashRoute()
}

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val userPreferences: UserPreferences,
    private val supabase: SupabaseClient
) : ViewModel() {

    private val _routeState = MutableStateFlow<SplashRoute>(SplashRoute.Idle)
    val routeState: StateFlow<SplashRoute> = _routeState.asStateFlow()

    fun determineNextRoute() {
        viewModelScope.launch {
            val isOnboardingCompleted = userPreferences.isOnboardingCompleted.first()
            val isRememberMeEnabled = userPreferences.isRememberMeEnabled.first()
            val hasSession = supabase.auth.currentSessionOrNull() != null

            if (!isOnboardingCompleted) {
                // Skenario A: Pengguna Baru
                _routeState.value = SplashRoute.Onboarding
            } else if (!hasSession) {
                // Skenario B: Aplikasi Pernah Dibuka, Belum Login LOKAL
                _routeState.value = SplashRoute.AuthSelection
            } else if (hasSession && !isRememberMeEnabled) {
                // Skenario C: Sudah Login TAPI "Ingat Aku" tidak dicentang -> Bersihkan sesi
                try {
                    supabase.auth.signOut()
                } catch (e: Exception) {
                    // Abaikan jika gagal logout background
                }
                _routeState.value = SplashRoute.AuthSelection
            } else {
                // Skenario D: Token lokal ada, saatnya VERIFIKASI KE SERVER (Database)
                try {
                    // Blokir sesaat untuk bertanya ke server "Apakah token ini masih sah dan user belum dihapus?"
                    supabase.auth.retrieveUserForCurrentSession(updateSession = true)
                    
                    // Jika sukses ke titik ini, berarti user BENAR-BENAR ada di database
                    _routeState.value = SplashRoute.Home
                } catch (e: Exception) {
                    // Skenario E: Token lokal kedaluwarsa, atau User dihapus admin dari database!
                    try {
                        supabase.auth.signOut() // Bersihkan sisa sampah token lokal
                    } catch (ex: Exception) { }
                    
                    _routeState.value = SplashRoute.AuthSelection
                }
            }
        }
    }
}
