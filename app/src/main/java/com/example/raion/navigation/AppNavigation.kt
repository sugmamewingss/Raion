package com.example.raion.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.lifecycle.Lifecycle
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavType
import androidx.navigation.navArgument
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
                },
                onNavigateToStoryDetail = { episodeId ->
                    if (backStackEntry.lifecycle.currentState == Lifecycle.State.RESUMED) {
                        navController.navigate("story_detail/$episodeId")
                    }
                },
                onNavigateToQuiz = {
                    if (backStackEntry.lifecycle.currentState == Lifecycle.State.RESUMED) {
                        navController.navigate("quiz")
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
                initialAvatarUrl = uiState.currentAvatarUrl,
                onBackClick = {
                    navController.popBackStack()
                },
                onSaveSuccess = {
                    homeViewModel.refreshData()
                }
            )
        }

        // ===== STORY DETAIL: Slide Horizontal =====
        composable(
            "story_detail/{episodeId}",
            arguments = listOf(navArgument("episodeId") { type = NavType.StringType }),
            enterTransition = { slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(350)) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }, animationSpec = tween(350)) },
            popEnterTransition = { slideInHorizontally(initialOffsetX = { -it }, animationSpec = tween(350)) },
            popExitTransition = { slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(350)) }
        ) { backStackEntry ->
            val episodeId = backStackEntry.arguments?.getString("episodeId") ?: ""
            
            // Note: StoryDetailScreen will instantiate its own StoryViewModel scoped to this backstack entry
            // Or it can be passed from AppNavigation if we scoped it higher up, but this is fine.
            com.example.raion.ui.features.story.StoryDetailScreen(
                episodeId = episodeId,
                onNavigateBack = {
                    if (backStackEntry.lifecycle.currentState == Lifecycle.State.RESUMED) {
                        navController.popBackStack()
                    }
                },
                onNextLevel = { nextEpId ->
                    if (backStackEntry.lifecycle.currentState == Lifecycle.State.RESUMED) {
                        // Navigate to next episode, replacing current one
                        navController.navigate("story_detail/$nextEpId") {
                            popUpTo("story_detail/{episodeId}") { inclusive = true }
                        }
                    }
                },
                onPreviousLevel = { prevEpId ->
                    if (backStackEntry.lifecycle.currentState == Lifecycle.State.RESUMED) {
                        navController.navigate("story_detail/$prevEpId") {
                            popUpTo("story_detail/{episodeId}") { inclusive = true }
                        }
                    }
                },
                onFinish = {
                    if (backStackEntry.lifecycle.currentState == Lifecycle.State.RESUMED) {
                        navController.popBackStack()
                    }
                }
            )
        }
        // ===== QUIZ: Slide Horizontal =====
        composable(
            "quiz",
            enterTransition = { slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(350)) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }, animationSpec = tween(350)) },
            popEnterTransition = { slideInHorizontally(initialOffsetX = { -it }, animationSpec = tween(350)) },
            popExitTransition = { slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(350)) }
        ) { backStackEntry ->
            com.example.raion.ui.features.quiz.QuizScreen(
                onNavigateBack = {
                    if (backStackEntry.lifecycle.currentState == Lifecycle.State.RESUMED) {
                        navController.popBackStack()
                    }
                },
                onNavigateToEpisodes = {
                    if (backStackEntry.lifecycle.currentState == Lifecycle.State.RESUMED) {
                        navController.navigate("quiz_episode")
                    }
                }
            )
        }

        // ===== QUIZ EPISODE: Slide Horizontal =====
        composable(
            "quiz_episode",
            enterTransition = { slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(350)) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }, animationSpec = tween(350)) },
            popEnterTransition = { slideInHorizontally(initialOffsetX = { -it }, animationSpec = tween(350)) },
            popExitTransition = { slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(350)) }
        ) { backStackEntry ->
            com.example.raion.ui.features.quiz.QuizEpisodeScreen(
                onNavigateBack = {
                    if (backStackEntry.lifecycle.currentState == Lifecycle.State.RESUMED) {
                        navController.popBackStack()
                    }
                },
                onNavigateToPrep = {
                    if (backStackEntry.lifecycle.currentState == Lifecycle.State.RESUMED) {
                        navController.navigate("quiz_prep")
                    }
                }
            )
        }

        // ===== QUIZ PREP: Slide Horizontal =====
        composable(
            "quiz_prep",
            enterTransition = { slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(350)) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }, animationSpec = tween(350)) },
            popEnterTransition = { slideInHorizontally(initialOffsetX = { -it }, animationSpec = tween(350)) },
            popExitTransition = { slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(350)) }
        ) { backStackEntry ->
            com.example.raion.ui.features.quiz.QuizPrepScreen(
                onNavigateBack = {
                    if (backStackEntry.lifecycle.currentState == Lifecycle.State.RESUMED) {
                        navController.popBackStack()
                    }
                },
                onStartQuiz = {
                    if (backStackEntry.lifecycle.currentState == Lifecycle.State.RESUMED) {
                        navController.navigate("quiz_question")
                    }
                }
            )
        }

        // ===== QUIZ QUESTION: Slide Horizontal =====
        composable(
            "quiz_question",
            enterTransition = { slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(350)) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }, animationSpec = tween(350)) },
            popEnterTransition = { slideInHorizontally(initialOffsetX = { -it }, animationSpec = tween(350)) },
            popExitTransition = { slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(350)) }
        ) { backStackEntry ->
            com.example.raion.ui.features.quiz.QuizQuestionScreen(
                onNavigateBack = {
                    if (backStackEntry.lifecycle.currentState == Lifecycle.State.RESUMED) {
                        navController.popBackStack()
                    }
                },
                onHalfwayBreak = {
                    if (backStackEntry.lifecycle.currentState == Lifecycle.State.RESUMED) {
                        navController.navigate("quiz_break")
                    }
                },
                onQuizFinished = { correctCount ->
                    if (backStackEntry.lifecycle.currentState == Lifecycle.State.RESUMED) {
                        navController.navigate("quiz_complete/$correctCount") {
                            popUpTo("quiz_question") { inclusive = true }
                        }
                    }
                }
            )
        }

        // ===== QUIZ BREAK: Fade in/out for breather =====
        composable(
            "quiz_break",
            enterTransition = { fadeIn(animationSpec = tween(400)) },
            exitTransition = { fadeOut(animationSpec = tween(400)) },
            popEnterTransition = { fadeIn(animationSpec = tween(400)) },
            popExitTransition = { fadeOut(animationSpec = tween(400)) }
        ) { backStackEntry ->
            com.example.raion.ui.features.quiz.QuizBreakScreen(
                onNavigateBack = {
                    if (backStackEntry.lifecycle.currentState == Lifecycle.State.RESUMED) {
                        navController.popBackStack()
                    }
                },
                onContinue = {
                    if (backStackEntry.lifecycle.currentState == Lifecycle.State.RESUMED) {
                        navController.popBackStack() // Pop back to question screen to continue
                    }
                }
            )
        }

        // ===== QUIZ COMPLETE: Slide Horizontal =====
        composable(
            "quiz_complete/{correctCount}",
            arguments = listOf(navArgument("correctCount") { type = NavType.IntType }),
            enterTransition = { slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(350)) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }, animationSpec = tween(350)) },
            popEnterTransition = { slideInHorizontally(initialOffsetX = { -it }, animationSpec = tween(350)) },
            popExitTransition = { slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(350)) }
        ) { backStackEntry ->
            val correctCount = backStackEntry.arguments?.getInt("correctCount") ?: 0

            // Optionally, we could save the XP and Coins to the backend here like we did for Mission.
            // For now, we focus on the UI flow.
            
            com.example.raion.ui.features.quiz.QuizCompleteScreen(
                correctAnswersCount = correctCount,
                onNavigateToExplanation = {
                    if (backStackEntry.lifecycle.currentState == Lifecycle.State.RESUMED) {
                        navController.navigate("quiz_explanation")
                    }
                },
                onRetryQuiz = {
                    if (backStackEntry.lifecycle.currentState == Lifecycle.State.RESUMED) {
                        // Pop back to the initial QuizScreen to retry
                        navController.navigate("quiz") {
                            popUpTo("quiz") { inclusive = true }
                        }
                    }
                }
            )
        }

        // ===== QUIZ EXPLANATION: Slide Horizontal =====
        composable(
            "quiz_explanation",
            enterTransition = { slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(350)) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }, animationSpec = tween(350)) },
            popEnterTransition = { slideInHorizontally(initialOffsetX = { -it }, animationSpec = tween(350)) },
            popExitTransition = { slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(350)) }
        ) { backStackEntry ->
            com.example.raion.ui.features.quiz.QuizExplanationScreen(
                onNavigateHome = {
                    if (backStackEntry.lifecycle.currentState == Lifecycle.State.RESUMED) {
                        navController.navigate("home") {
                            popUpTo("home") { inclusive = true } // Clear quiz stack, go fresh to home
                        }
                    }
                },
                onRetryQuiz = {
                    if (backStackEntry.lifecycle.currentState == Lifecycle.State.RESUMED) {
                        navController.navigate("quiz") {
                            popUpTo("quiz") { inclusive = true } // Clear explanation stack, fresh quiz
                        }
                    }
                }
            )
        }
    }
}
