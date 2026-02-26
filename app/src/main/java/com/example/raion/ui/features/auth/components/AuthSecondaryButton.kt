package com.example.raion.ui.features.auth.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.raion.ui.theme.DesignTokens

@Composable
fun AuthSecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(DesignTokens.Dimensions.CornerRadiusMedium),
        colors = ButtonDefaults.buttonColors(
            containerColor = DesignTokens.Colors.CardBackground, 
            contentColor = Color.Black
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp, pressedElevation = 0.dp),
        border = BorderStroke(
            width = 1.dp,
            color = Color.Black.copy(alpha = 0.26f) // 26% Black Outline
        ),
        contentPadding = PaddingValues(0.dp) // Let custom Box fill the entire button
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Dark Inner Shadow (Bevel) at the bottom
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(5.dp) // The thickness of the inner shadow
                    .background(Color.Black.copy(alpha = 0.1f)) // Creates a subtle grey inner shadow
            )

            // Text perfectly centered
            Text(
                text = text,
                modifier = Modifier.align(Alignment.Center),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold, 
                color = Color.Black,
                letterSpacing = 1.sp
            )
        }
    }
}
