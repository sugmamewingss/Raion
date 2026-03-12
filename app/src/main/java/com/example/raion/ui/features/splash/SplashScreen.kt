package com.example.raion.ui.features.splash

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.raion.R
import com.example.raion.ui.theme.DesignTokens
import kotlinx.coroutines.delay
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun SplashScreen(
    viewModel: SplashViewModel = hiltViewModel(),
    onNavigate: (String) -> Unit
) {
    var currentState by remember { mutableIntStateOf(0) }
    val routeState by viewModel.routeState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.determineNextRoute()
        delay(500)
        currentState = 1
        delay(1000)
        
        when (routeState) {
            is SplashRoute.Onboarding -> onNavigate("onboarding")
            is SplashRoute.AuthSelection -> onNavigate("auth_selection")
            is SplashRoute.Home -> onNavigate("home")
            else -> onNavigate("auth_selection") // fallback
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DesignTokens.Colors.BrandPrimary),
        contentAlignment = Alignment.Center
    ) {
        Crossfade(
            targetState = currentState,
            animationSpec = tween(durationMillis = 800),
            label = "SplashTransition",
            modifier = Modifier.fillMaxSize()
        ) { state ->
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                when (state) {
                    0 -> {
                        Image(
                            painter = painterResource(id = R.drawable.img_dino_face),
                            contentDescription = "App Logo",
                            modifier = Modifier.size(200.dp)
                        )
                    }
                    1 -> {
                        Text(
                            text = "BinGo",
                            style = MaterialTheme.typography.headlineLarge,
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 42.sp,
                            letterSpacing = (-1).sp
                        )
                    }
                }
            }
        }
    }
}
