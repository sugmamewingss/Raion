package com.example.raion.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.raion.ui.features.auth.AuthSelectionScreen
import com.example.raion.ui.features.auth.LoginScreen
// [TAMBAHAN BARU] Import halaman RegisterStep1Screen
import com.example.raion.ui.features.auth.register.RegisterStep1Screen
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
                    navController.navigate("auth_selection")
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

        // [TAMBAHAN BARU] Rute untuk Register Langkah 1
        composable("register_step_1") {
            RegisterStep1Screen(
                onBackClick = {
                    // Kembali ke halaman pemilihan Auth
                    navController.popBackStack()
                },
                onNextClick = {
                    // Nanti kita isi untuk lanjut ke langkah 2 (Input Nama)
                    // navController.navigate("register_step_2")
                }
            )
        }
    }
}