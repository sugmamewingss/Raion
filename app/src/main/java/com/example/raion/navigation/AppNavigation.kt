package com.example.raion.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.raion.ui.features.auth.AuthSelectionScreen
import com.example.raion.ui.features.auth.LoginScreen
import com.example.raion.ui.features.auth.register.RegisterStep1Screen
import com.example.raion.ui.features.auth.register.RegisterStep2Screen
import com.example.raion.ui.features.auth.register.RegisterStep3Screen
import com.example.raion.ui.features.auth.register.RegisterStep4Screen
import com.example.raion.ui.features.auth.register.RegisterStep5Screen
import com.example.raion.ui.features.auth.register.RegisterStep6Screen
import com.example.raion.ui.features.auth.register.RegisterStepFinalScreen
import com.example.raion.ui.features.onboarding.OnboardingScreen
import com.example.raion.ui.features.splash.SplashScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable("splash") {
            SplashScreen(
                onSplashFinished = {
                    navController.navigate("onboarding") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }
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
                    navController.navigate("login")
                },
                onRegisterClick = {
                    navController.navigate("register_step_1")
                }
            )
        }

        composable("login") {
            LoginScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onLoginSubmit = {
                    // Nanti diisi setelah ada halaman Home
                }
            )
        }

        // Rute untuk Register Langkah 1
        composable("register_step_1") {
            RegisterStep1Screen(
                onBackClick = {
                    navController.popBackStack()
                },
                onNextClick = {
                    navController.navigate("register_step_2")
                }
            )
        }

        // Rute untuk Register Langkah 2
        composable("register_step_2") {
            RegisterStep2Screen(
                onBackClick = {
                    navController.popBackStack()
                },
                onNextClick = {
                    navController.navigate("register_step_3")
                }
            )
        }

        composable("register_step_3") {
            RegisterStep3Screen(
                onBackClick = { navController.popBackStack() },
                onNextClick = {
                     navController.navigate("register_step_4")
                }
            )
        }

        composable("register_step_4") {
            RegisterStep4Screen(
                onBackClick = { navController.popBackStack() },
                onNextClick = {
                     navController.navigate("register_step_5")
                }
            )
        }

        composable("register_step_5") {
            RegisterStep5Screen(
                onBackClick = { navController.popBackStack() },
                onNextClick = {
                    navController.navigate("register_step_6")
                }
            )
        }

        composable("register_step_6") {
            RegisterStep6Screen(
                onBackClick = { navController.popBackStack() },
                onNextClick = {
                    navController.navigate("register_step_final") }
            )
        }
        composable("register_step_final") {
            RegisterStepFinalScreen(
                onBackClick = { navController.popBackStack() },
                onFinishClick = {
                    navController.navigate("login") {
                        popUpTo("auth_selection")
                    }
                }
            )
        }
    }
}
