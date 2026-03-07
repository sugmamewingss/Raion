package com.example.raion.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.Lifecycle
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.raion.ui.features.auth.AuthSelectionScreen
import com.example.raion.ui.features.auth.LoginScreen
import com.example.raion.ui.features.auth.LoginViewModel
import com.example.raion.ui.features.auth.register.RegisterScreen
import com.example.raion.ui.features.auth.register.RegisterViewModel
import com.example.raion.ui.features.onboarding.OnboardingScreen
import com.example.raion.ui.features.splash.SplashScreen
import com.example.raion.ui.features.home.HomeScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable("splash") { backStackEntry ->
            val splashViewModel: com.example.raion.ui.features.splash.SplashViewModel = hiltViewModel()
            SplashScreen(
                viewModel = splashViewModel,
                onNavigate = { route ->
                    if (backStackEntry.lifecycle.currentState == Lifecycle.State.RESUMED) {
                        navController.navigate(route) {
                            popUpTo("splash") { inclusive = true }
                        }
                    }
                }
            )
        }
        composable("onboarding") { backStackEntry ->
            val onboardingViewModel: com.example.raion.ui.features.onboarding.OnboardingViewModel = hiltViewModel()
            OnboardingScreen(
                viewModel = onboardingViewModel,
                onNavigateNext = {
                    if (backStackEntry.lifecycle.currentState == Lifecycle.State.RESUMED) {
                        navController.navigate("auth_selection") {
                            popUpTo("onboarding") { inclusive = true }
                        }
                    }
                }
            )
        }

        composable("auth_selection") { backStackEntry ->
            AuthSelectionScreen(
                onLoginClick = {
                    if (backStackEntry.lifecycle.currentState == Lifecycle.State.RESUMED) {
                        navController.navigate("login")
                    }
                },
                onRegisterClick = {
                    if (backStackEntry.lifecycle.currentState == Lifecycle.State.RESUMED) {
                        navController.navigate("register")
                    }
                }
            )
        }

        composable("login") { backStackEntry ->
            val loginViewModel: LoginViewModel = hiltViewModel()
            LoginScreen(
                viewModel = loginViewModel,
                onBackClick = {
                    if (backStackEntry.lifecycle.currentState == Lifecycle.State.RESUMED) {
                        navController.popBackStack()
                    }
                },
                onLoginSubmit = {
                    if (backStackEntry.lifecycle.currentState == Lifecycle.State.RESUMED) {
                        navController.navigate("home") {
                            popUpTo("auth_selection") { inclusive = true }
                        }
                    }
                }
            )
        }

        composable("register") { backStackEntry ->
            val registerViewModel: RegisterViewModel = hiltViewModel()
            RegisterScreen(
                viewModel = registerViewModel,
                onBackToAuthSelection = {
                    if (backStackEntry.lifecycle.currentState == Lifecycle.State.RESUMED) {
                        navController.popBackStack()
                    }
                },
                onFinishRegister = {
                    if (backStackEntry.lifecycle.currentState == Lifecycle.State.RESUMED) {
                        navController.navigate("home") {
                            popUpTo("auth_selection") { inclusive = true }
                        }
                    }
                }
            )
        }

        composable("home") { backStackEntry ->
            val homeViewModel: com.example.raion.ui.features.home.HomeViewModel = hiltViewModel()

            // Optimistic UI: observe mission result data (not a boolean flag)
            val gainedXpFlow = backStackEntry.savedStateHandle.getStateFlow("mission_gained_xp", -1)
            androidx.compose.runtime.LaunchedEffect(Unit) {
                gainedXpFlow.collect { xp ->
                    if (xp >= 0) {
                        val coins = backStackEntry.savedStateHandle.get<Int>("mission_gained_coins") ?: 0
                        val progress = backStackEntry.savedStateHandle.get<Int>("mission_new_progress") ?: 0
                        val isComplete = backStackEntry.savedStateHandle.get<Boolean>("mission_is_complete") ?: false

                        homeViewModel.applyMissionResult(xp, coins, progress, isComplete)

                        // Clear the result so it doesn't re-apply
                        backStackEntry.savedStateHandle["mission_gained_xp"] = -1
                    }
                }
            }

            HomeScreen(
                viewModel = homeViewModel,
                onNavigateOut = {
                    if (backStackEntry.lifecycle.currentState == Lifecycle.State.RESUMED) {
                        navController.navigate("auth_selection") {
                            popUpTo("home") { inclusive = true }
                        }
                    }
                },
                onStartMission = {
                    if (backStackEntry.lifecycle.currentState == Lifecycle.State.RESUMED) {
                        navController.navigate("mission")
                    }
                }
            )
        }

        composable("mission") { backStackEntry ->
            com.example.raion.ui.features.mission.MissionScreen(
                onBackWithResult = { gainedXp, gainedCoins, newProgress, isComplete ->
                    if (backStackEntry.lifecycle.currentState == Lifecycle.State.RESUMED) {
                        // Pass result data to home for optimistic update
                        navController.previousBackStackEntry?.savedStateHandle?.apply {
                            set("mission_gained_xp", gainedXp)
                            set("mission_gained_coins", gainedCoins)
                            set("mission_new_progress", newProgress)
                            set("mission_is_complete", isComplete)
                        }
                        navController.popBackStack()
                    }
                }
            )
        }
    }
}
