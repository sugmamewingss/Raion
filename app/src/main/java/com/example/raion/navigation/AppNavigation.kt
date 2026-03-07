package com.example.raion.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import kotlinx.coroutines.launch
import com.example.raion.ui.features.auth.register.RegisterViewModel
import com.example.raion.ui.features.onboarding.OnboardingScreen
import com.example.raion.ui.features.splash.SplashScreen
import com.example.raion.ui.features.home.HomeScreen
import com.example.raion.ui.features.mission.DailyMissionScreen
import com.example.raion.ui.features.mission.MissionOnboardingScreen

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

        // ===== HOME: Fade Transition (masuk dari login/register) =====
        composable(
            "home",
            enterTransition = { fadeIn(animationSpec = tween(400)) },
            exitTransition = { fadeOut(animationSpec = tween(300)) },
            popEnterTransition = { fadeIn(animationSpec = tween(400)) },
            popExitTransition = { fadeOut(animationSpec = tween(300)) }
        ) { backStackEntry ->
            val homeViewModel: com.example.raion.ui.features.home.HomeViewModel = hiltViewModel()
            HomeScreen(
                viewModel = homeViewModel,
                onNavigateOut = {
                    if (backStackEntry.lifecycle.currentState == Lifecycle.State.RESUMED) {
                        navController.navigate("auth_selection") {
                            popUpTo("home") { inclusive = true }
                        }
                    }
                },
                onNavigateToDailyMission = {
                    if (backStackEntry.lifecycle.currentState == Lifecycle.State.RESUMED) {
                        navController.navigate("daily_mission")
                    }
                }
            )
        }
        
        // ===== DAILY MISSION: Scale + Fade Transition =====
        composable(
            "daily_mission",
            enterTransition = {
                scaleIn(initialScale = 0.85f, animationSpec = tween(400)) +
                fadeIn(animationSpec = tween(400))
            },
            exitTransition = {
                scaleOut(targetScale = 1.1f, animationSpec = tween(300)) +
                fadeOut(animationSpec = tween(300))
            },
            popEnterTransition = {
                scaleIn(initialScale = 1.1f, animationSpec = tween(400)) +
                fadeIn(animationSpec = tween(400))
            },
            popExitTransition = {
                scaleOut(targetScale = 0.85f, animationSpec = tween(300)) +
                fadeOut(animationSpec = tween(300))
            }
        ) { backStackEntry ->
            DailyMissionScreen(
                onBackClick = {
                    if (backStackEntry.lifecycle.currentState == Lifecycle.State.RESUMED) {
                        navController.popBackStack()
                    }
                },
                onNavigateNext = {
                    if (backStackEntry.lifecycle.currentState == Lifecycle.State.RESUMED) {
                        navController.navigate("mission_onboarding")
                    }
                }
            )
        }
        
        // ===== MISSION ONBOARDING: Slide Horizontal =====
        composable(
            "mission_onboarding",
            enterTransition = { slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(350)) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }, animationSpec = tween(350)) },
            popEnterTransition = { slideInHorizontally(initialOffsetX = { -it }, animationSpec = tween(350)) },
            popExitTransition = { slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(350)) }
        ) { backStackEntry ->
            MissionOnboardingScreen(
                onBackClick = {
                    if (backStackEntry.lifecycle.currentState == Lifecycle.State.RESUMED) {
                        navController.popBackStack()
                    }
                },
                onNextClick = {
                    if (backStackEntry.lifecycle.currentState == Lifecycle.State.RESUMED) {
                        navController.navigate("trash_selection")
                    }
                }
            )
        }
        
        // ===== TRASH SELECTION: Slide Horizontal =====
        composable(
            "trash_selection",
            enterTransition = { slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(350)) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }, animationSpec = tween(350)) },
            popEnterTransition = { slideInHorizontally(initialOffsetX = { -it }, animationSpec = tween(350)) },
            popExitTransition = { slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(350)) }
        ) { backStackEntry ->
            com.example.raion.ui.features.mission.TrashSelectionScreen(
                onBackClick = {
                    if (backStackEntry.lifecycle.currentState == Lifecycle.State.RESUMED) {
                        navController.popBackStack()
                    }
                },
                onNavigateToOrganic = {
                    if (backStackEntry.lifecycle.currentState == Lifecycle.State.RESUMED) {
                        navController.navigate("trash_category/organic")
                    }
                },
                onNavigateToRecycle = {
                    if (backStackEntry.lifecycle.currentState == Lifecycle.State.RESUMED) {
                        navController.navigate("trash_category/recycle")
                    }
                },
                onNavigateToOthers = {
                    if (backStackEntry.lifecycle.currentState == Lifecycle.State.RESUMED) {
                        navController.navigate("under_development")
                    }
                }
            )
        }
        
        // ===== TRASH CATEGORY: Slide Horizontal =====
        composable(
            route = "trash_category/{type}",
            arguments = listOf(androidx.navigation.navArgument("type") { type = androidx.navigation.NavType.StringType }),
            enterTransition = { slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(350)) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }, animationSpec = tween(350)) },
            popEnterTransition = { slideInHorizontally(initialOffsetX = { -it }, animationSpec = tween(350)) },
            popExitTransition = { slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(350)) }
        ) { backStackEntry ->
            val categoryType = backStackEntry.arguments?.getString("type") ?: "organic"
            com.example.raion.ui.features.mission.TrashCategoryScreen(
                categoryType = categoryType,
                onBackClick = {
                    if (backStackEntry.lifecycle.currentState == Lifecycle.State.RESUMED) {
                        navController.popBackStack()
                    }
                },
                onItemClick = { itemName ->
                    if (backStackEntry.lifecycle.currentState == Lifecycle.State.RESUMED) {
                        // Pass category type (organic or inorganic) to location_selection
                        val wasteCategory = if (categoryType == "organic") "organic" else "inorganic"
                        navController.navigate("location_selection/$wasteCategory")
                    }
                }
            )
        }
        
        // ===== LOCATION SELECTION: Slide Horizontal =====
        composable(
            route = "location_selection/{category}",
            arguments = listOf(androidx.navigation.navArgument("category") { type = androidx.navigation.NavType.StringType }),
            enterTransition = { slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(350)) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }, animationSpec = tween(350)) },
            popEnterTransition = { slideInHorizontally(initialOffsetX = { -it }, animationSpec = tween(350)) },
            popExitTransition = { slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(350)) }
        ) { backStackEntry ->
            val wasteCategory = backStackEntry.arguments?.getString("category") ?: "organic"
            com.example.raion.ui.features.mission.LocationSelectionScreen(
                onBackClick = {
                    if (backStackEntry.lifecycle.currentState == Lifecycle.State.RESUMED) {
                        navController.popBackStack()
                    }
                },
                onLocationClick = { locationName ->
                    if (backStackEntry.lifecycle.currentState == Lifecycle.State.RESUMED) {
                        navController.navigate("trash_quantity/$wasteCategory")
                    }
                }
            )
        }
        
        // ===== TRASH QUANTITY: Slide Horizontal =====
        composable(
            route = "trash_quantity/{category}",
            arguments = listOf(androidx.navigation.navArgument("category") { type = androidx.navigation.NavType.StringType }),
            enterTransition = { slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(350)) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }, animationSpec = tween(350)) },
            popEnterTransition = { slideInHorizontally(initialOffsetX = { -it }, animationSpec = tween(350)) },
            popExitTransition = { slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(350)) }
        ) { backStackEntry ->
            val wasteCategory = backStackEntry.arguments?.getString("category") ?: "organic"
            val coroutineScope = rememberCoroutineScope()
            val vm: com.example.raion.ui.features.mission.DailyMissionViewModel = hiltViewModel()
            com.example.raion.ui.features.mission.TrashQuantityScreen(
                onBackClick = {
                    if (backStackEntry.lifecycle.currentState == Lifecycle.State.RESUMED) {
                        navController.popBackStack()
                    }
                },
                onQuantitySelected = { quantity ->
                    if (backStackEntry.lifecycle.currentState == Lifecycle.State.RESUMED) {
                        coroutineScope.launch {
                            val repo = vm.authRepository
                            // Record with category (organic/inorganic)
                            val result = repo.recordTrashDisposal(quantity, wasteCategory)
                            val newCount = result.getOrNull() ?: 0
                            // Get level-based max target
                            val progressResult = repo.getDailyMissionProgress()
                            val maxTarget = progressResult.getOrNull()?.second ?: 10
                            if (newCount >= maxTarget) {
                                navController.navigate("mission_complete")
                            } else {
                                navController.navigate("mission_incomplete")
                            }
                        }
                    }
                }
            )
        }
        
        // ===== MISSION COMPLETE: Slide Horizontal =====
        composable(
            "mission_complete",
            enterTransition = { slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(350)) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }, animationSpec = tween(350)) },
            popEnterTransition = { slideInHorizontally(initialOffsetX = { -it }, animationSpec = tween(350)) },
            popExitTransition = { slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(350)) }
        ) { backStackEntry ->
            val coroutineScope = rememberCoroutineScope()
            val vm: com.example.raion.ui.features.mission.DailyMissionViewModel = hiltViewModel()
            com.example.raion.ui.features.mission.MissionCompleteScreen(
                onBackClick = {
                    if (backStackEntry.lifecycle.currentState == Lifecycle.State.RESUMED) {
                        navController.popBackStack()
                    }
                },
                onSaveProgress = {
                    if (backStackEntry.lifecycle.currentState == Lifecycle.State.RESUMED) {
                        coroutineScope.launch {
                            // Award +85 XP and +100 coins
                            vm.authRepository.completeMissionRewards()
                            // Lanjut ke animasi truk sampah sebelum mystery box
                            navController.navigate("daily_mission_truck") {
                                popUpTo("home") { inclusive = false }
                            }
                        }
                    }
                }
            )
        }
        
        // ===== MISSION INCOMPLETE: Slide Horizontal =====
        composable(
            "mission_incomplete",
            enterTransition = { slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(350)) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }, animationSpec = tween(350)) },
            popEnterTransition = { slideInHorizontally(initialOffsetX = { -it }, animationSpec = tween(350)) },
            popExitTransition = { slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(350)) }
        ) { backStackEntry ->
            com.example.raion.ui.features.mission.MissionIncompleteScreen(
                onBackClick = {
                    if (backStackEntry.lifecycle.currentState == Lifecycle.State.RESUMED) {
                        navController.popBackStack()
                    }
                },
                onContinue = {
                    if (backStackEntry.lifecycle.currentState == Lifecycle.State.RESUMED) {
                        // Kembali ke DailyMissionScreen untuk lanjut misi
                        navController.navigate("daily_mission") {
                            popUpTo("daily_mission") { inclusive = true }
                        }
                    }
                },
                onExit = {
                    if (backStackEntry.lifecycle.currentState == Lifecycle.State.RESUMED) {
                        // Kembali ke home
                        navController.navigate("home") {
                            popUpTo("home") { inclusive = true }
                        }
                    }
                }
            )
        }
        
        // ===== DAILY MISSION TRUCK (State 2) =====
        composable(
            "daily_mission_truck",
            enterTransition = { slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(350)) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }, animationSpec = tween(350)) },
            popEnterTransition = { slideInHorizontally(initialOffsetX = { -it }, animationSpec = tween(350)) },
            popExitTransition = { slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(350)) }
        ) { backStackEntry ->
            com.example.raion.ui.features.mission.DailyMissionScreen(
                missionState = com.example.raion.ui.features.mission.DailyMissionState.TRUCK_READY,
                onBackClick = {
                    if (backStackEntry.lifecycle.currentState == Lifecycle.State.RESUMED) {
                        navController.popBackStack()
                    }
                },
                onTruckFinishClick = {
                    if (backStackEntry.lifecycle.currentState == Lifecycle.State.RESUMED) {
                        navController.navigate("daily_mission_reward") {
                            popUpTo("home") { inclusive = false }
                        }
                    }
                }
            )
        }
        
        // ===== DAILY MISSION MYSTERY BOX REWARD (State 3) =====
        composable(
            "daily_mission_reward",
            enterTransition = { slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(350)) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }, animationSpec = tween(350)) },
            popEnterTransition = { slideInHorizontally(initialOffsetX = { -it }, animationSpec = tween(350)) },
            popExitTransition = { slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(350)) }
        ) { backStackEntry ->
            val coroutineScope = rememberCoroutineScope()
            val vm: com.example.raion.ui.features.mission.DailyMissionViewModel = hiltViewModel()
            com.example.raion.ui.features.mission.DailyMissionScreen(
                missionState = com.example.raion.ui.features.mission.DailyMissionState.MYSTERY_BOX_READY,
                onBackClick = {
                    if (backStackEntry.lifecycle.currentState == Lifecycle.State.RESUMED) {
                        navController.navigate("home") {
                            popUpTo("home") { inclusive = true }
                        }
                    }
                },
                onClaimRewardClick = {
                    if (backStackEntry.lifecycle.currentState == Lifecycle.State.RESUMED) {
                        coroutineScope.launch {
                            // Reset the progress to 0/10 so user can do it again
                            vm.authRepository.resetDailyMissionProgress()
                            navController.navigate("home") {
                                popUpTo("home") { inclusive = true }
                            }
                        }
                    }
                }
            )
        }
        
        // ===== UNDER DEVELOPMENT: Slide Horizontal =====
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
    }
}
