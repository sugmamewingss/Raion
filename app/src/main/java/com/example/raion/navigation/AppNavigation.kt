package com.example.raion.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.raion.ui.features.auth.AuthSelectionScreen
import com.example.raion.ui.features.onboarding.OnboardingScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "onboarding"
    ) {
        composable("onboarding") {
            OnboardingScreen(
                onNavigateNext = {
                    navController.navigate("auth_selection") {
                        popUpTo("onboarding") { inclusive = true }
                    }
                }
            )
        }

        composable("auth_selection") {
            AuthSelectionScreen(
                onLoginClick = {
                    // Navigate to Login Screen
                },
                onRegisterClick = {
                    // Navigate to Register Screen
                }
            )
        }
    }
}
