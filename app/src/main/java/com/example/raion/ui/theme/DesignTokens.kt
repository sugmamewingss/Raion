package com.example.raion.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

object DesignTokens {
    object Colors {
        // Existing tokens
        val BrandPrimary = Color(0xFF68C2A7)
        val BrandSecondary = Color(0xFFF09D51)
        val BrandDark = Color(0xFF11533F)
        val BackgroundGradientStart = Color(0xFF68C2A7)
        val BackgroundGradientEnd = Color(0xFFF8F8EB)
        val TextSecondary = Color(0xFF474444)
        val IndicatorInactive = Color(0xFFE0E0E0)
        val CardBackground = Color(0xFFFEFEEF)
        val StrokeDark = Color.Black.copy(alpha = 0.26f)
        
        // Home Screen UI Tokens
        val CreamBackground = Color(0xFFFFFBE6)
        val OrangePrimary = Color(0xFFF4A261)
        val TealPrimary = Color(0xFF6AC9AB)
        val DarkBackground = Color(0xFF3D3D4E)
        val LightGrayBorder = Color(0xFFEFEFEF)
        val RankGold = Color(0xFFFBB03B)
    }

    object Dimensions {
        val PaddingSmall = 8.dp
        val PaddingMedium = 16.dp
        val PaddingLarge = 24.dp
        val CornerRadiusMedium = 16.dp
        val CornerRadiusLarge = 32.dp
        val ButtonHeight = 48.dp
        val BorderStrokeStardard = 1.dp
    }
}
