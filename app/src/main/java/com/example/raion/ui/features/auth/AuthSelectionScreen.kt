package com.example.raion.ui.features.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.raion.R
import com.example.raion.ui.features.auth.components.AuthPrimaryButton
import com.example.raion.ui.features.auth.components.AuthSecondaryButton
import com.example.raion.ui.theme.DesignTokens

@Composable
fun AuthSelectionScreen(
    onLoginClick: () -> Unit = {},
    onRegisterClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DesignTokens.Colors.BrandPrimary) // Top half background
    ) {
        // Inner Box acting as the layout anchor for the bottom card
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .fillMaxSize(fraction = 0.72f) // White card takes up bottom 72%
        ) {
            // Main Content Container (White/Cream bottom half)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(topStart = DesignTokens.Dimensions.CornerRadiusLarge, topEnd = DesignTokens.Dimensions.CornerRadiusLarge))
                    .background(DesignTokens.Colors.CardBackground)
                    .padding(horizontal = DesignTokens.Dimensions.PaddingLarge),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
            
            // Push content down slightly from the top edge
            Spacer(modifier = Modifier.height(48.dp))

            // Typography Section
            Text(
                text = "Selamat Datang",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold,
                color = Color.Black
            )
            
            Text(
                text = "Sahabat Dino!",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold,
                color = DesignTokens.Colors.BrandSecondary
            )

            // Center Illustration (Placeholder for Trash Can)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f), // Takes up available space between text and buttons
                contentAlignment = Alignment.Center
            ) {
                // val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.trash_animate))
                val composition by rememberLottieComposition(LottieCompositionSpec.Asset("trash_animate.lottie"))
                val progress by animateLottieCompositionAsState(
                    composition = composition,
                    iterations = LottieConstants.IterateForever
                )

                LottieAnimation(
                    composition = composition,
                    progress = { progress },
                    modifier = Modifier.size(240.dp)
                )
            }

            // Buttons Section
            AuthPrimaryButton(
                text = "MASUK",
                onClick = onLoginClick
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            AuthSecondaryButton(
                text = "BUAT AKUN",
                onClick = onRegisterClick
            )

            // Footer Version Text
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "v e r s i o n   1 . 0",
                style = MaterialTheme.typography.labelMedium,
                color = Color.Gray,
                letterSpacing = 2.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(24.dp))
        }

        // Peeking Dino Illustration - Placed inside the inner Box so its TopCenter is the card's top edge
            Image(
                painter = painterResource(id = R.drawable.dino_peeking),
                contentDescription = "Peeking Dino",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = (-176.7f).dp) // The offset is now independent of screen height, only relative to the dino's size!
                    .size(240.dp)
            )
        }
    }
}
