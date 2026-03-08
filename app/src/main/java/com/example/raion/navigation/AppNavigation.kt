package com.example.raion.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.lifecycle.Lifecycle
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.raion.data.repository.AuthRepository
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
                },
                // Alfi: "Belum punya akun?" goes directly to register
                onRegisterClick = {
                    if (backStackEntry.lifecycle.currentState == Lifecycle.State.RESUMED) {
                        navController.navigate("register")
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

        // ===== HOME: Fade Transition (from Alfi) + Optimistic UI (from HEAD) =====
        composable(
            "home",
            enterTransition = { fadeIn(animationSpec = tween(400)) },
            exitTransition = { fadeOut(animationSpec = tween(300)) },
            popEnterTransition = { fadeIn(animationSpec = tween(400)) },
            popExitTransition = { fadeOut(animationSpec = tween(300)) }
        ) { backStackEntry ->
            val homeViewModel: com.example.raion.ui.features.home.HomeViewModel = hiltViewModel()

            // HEAD: Optimistic UI — observe mission result data
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
                },
                onNavigateToEditProfile = {
                    if (backStackEntry.lifecycle.currentState == Lifecycle.State.RESUMED) {
                        navController.navigate("edit_profile")
                    }
                }
            )
        }

        // ===== MISSION: HEAD's single-screen wizard with backend integration =====
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
                },
                onMysteryBoxClick = {
                    if (backStackEntry.lifecycle.currentState == Lifecycle.State.RESUMED) {
                        navController.navigate("under_development")
                    }
                }
            )
        }

        // ===== UNDER DEVELOPMENT: Slide Horizontal (from Alfi) =====
        composable(
            "under_development",
            enterTransition = { slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(350)) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }, animationSpec = tween(350)) },
            popEnterTransition = { slideInHorizontally(initialOffsetX = { -it }, animationSpec = tween(350)) },
            popExitTransition = { slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(350)) }
        ) { backStackEntry ->
            com.example.raion.ui.features.mission.UnderDevelopmentScreen(
                onBackClick = {
                    if (backStackEntry.lifecycle.currentState == Lifecycle.State.RESUMED) {
                        navController.popBackStack()
                    }
                }
            )
        }

        // ===== EDIT PROFILE: Slide Horizontal =====
        composable(
            "edit_profile",
            enterTransition = { slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(350)) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }, animationSpec = tween(350)) },
            popEnterTransition = { slideInHorizontally(initialOffsetX = { -it }, animationSpec = tween(350)) },
            popExitTransition = { slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(350)) }
        ) { backStackEntry ->
            val homeEntry = remember(backStackEntry) {
                navController.getBackStackEntry("home")
            }
            val homeViewModel: com.example.raion.ui.features.home.HomeViewModel = hiltViewModel(homeEntry)
            val uiState by homeViewModel.uiState.collectAsState()

            com.example.raion.ui.features.profile.EditProfileScreen(
                initialNickname = uiState.username,
                initialFullName = uiState.fullName,
                initialBirthDate = uiState.birthDate,
                onBackClick = {
                    navController.popBackStack()
                },
                onSaveSuccess = {
                    homeViewModel.refreshData()
                }
            )
        }
    }
}
