package com.example.raion.ui.features.onboarding

import androidx.annotation.DrawableRes

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class OnboardingPageInfo(
    val title: String,
    val description: String,
    @DrawableRes val imageRes: Int,
    val imageScale: Float = 1.0f,
    val imageOffsetY: Dp = 0.dp
)
