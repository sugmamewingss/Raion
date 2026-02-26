package com.example.raion.ui.features.onboarding.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.BorderStroke
import com.example.raion.ui.theme.DesignTokens

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .height(DesignTokens.Dimensions.ButtonHeight),
        shape = RoundedCornerShape(DesignTokens.Dimensions.CornerRadiusLarge),
        border = BorderStroke(
            width = DesignTokens.Dimensions.BorderStrokeStardard,
            color = DesignTokens.Colors.StrokeDark
        ),
        colors = ButtonDefaults.buttonColors(
            containerColor = DesignTokens.Colors.BrandPrimary,
            contentColor = Color.White
        ),
        contentPadding = PaddingValues(horizontal = DesignTokens.Dimensions.PaddingLarge)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge
        )
    }
}
