package com.example.raion.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

object DesignTokens {
    object Colors {
        val BrandPrimary = Color(0xFF68C2A7)
        val BrandSecondary = Color(0xFFF09D51)
        val BrandDark = Color(0xFF11533F)
        val BackgroundGradientStart = Color(0xFF68C2A7)
        val BackgroundGradientEnd = Color(0xFFF8F8EB)
        val TextSecondary = Color(0xFF474444)
        val IndicatorInactive = Color(0xFFE0E0E0)
        val CardBackground = Color(0xFFFFFFFF)
        val StrokeDark = Color.Black.copy(alpha = 0.26f)
    }

    object Dimensions {
        val PaddingSmall = 8.dp
        val PaddingMedium = 16.dp
        val PaddingLarge = 24.dp
        val CornerRadiusLarge = 32.dp
        val ButtonHeight = 48.dp
        val BorderStrokeStardard = 1.dp
    }
}
