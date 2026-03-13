package com.example.raion.ui.features.auth

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
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
    var isTransitioning by rememberSaveable { mutableStateOf(false) }
    var targetAction by remember { mutableStateOf<(() -> Unit)?>(null) }
    
    // Auto-reverse when returning from backstack
    LaunchedEffect(Unit) {
        if (isTransitioning) {
            isTransitioning = false
            targetAction = null
        }
    }
    
    val cardHeightFraction by animateFloatAsState(
        targetValue = if (isTransitioning) 1.0f else 0.42f,
        animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing),
        finishedListener = {
            if (isTransitioning) {
                targetAction?.invoke()
            }
        },
        label = "cardHeightFraction"
    )
    
    val contentAlpha by animateFloatAsState(
        targetValue = if (isTransitioning) 0f else 1f,
        animationSpec = tween(durationMillis = 300),
        label = "contentAlpha"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DesignTokens.Colors.BrandPrimary) // Top half background
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 96.dp)
                .alpha(contentAlpha)
                .align(Alignment.TopCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Petualangan Dimulai!",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    letterSpacing = 1.sp
                ),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Masuk dan lanjutkan perjalananmu jadi\npahlawan lingkungan!",
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Medium,
                    color = Color.White,
                    lineHeight = 20.sp
                ),
                textAlign = TextAlign.Center
            )
        }

        // Inner Box acting as the layout anchor for the bottom card
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .fillMaxSize(fraction = cardHeightFraction) // Animates to full screen
        ) {
            // Background Container (White/Cream) - Does NOT fade!
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(topStart = DesignTokens.Dimensions.CornerRadiusLarge, topEnd = DesignTokens.Dimensions.CornerRadiusLarge))
                    .background(DesignTokens.Colors.CardBackground)
            )

            // Main Content Container (Fades Out)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(contentAlpha)
                    .padding(horizontal = DesignTokens.Dimensions.PaddingLarge),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
            
            // Push content down slightly from the top edge
            Spacer(modifier = Modifier.height(64.dp))

            // Buttons Section
            AuthPrimaryButton(
                text = "MASUK",
                onClick = { 
                    if (!isTransitioning) {
                        isTransitioning = true
                        targetAction = onLoginClick
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            AuthSecondaryButton(
                text = "BUAT AKUN",
                onClick = {
                    if (!isTransitioning) {
                        isTransitioning = true
                        targetAction = onRegisterClick
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.weight(1f))

            val annotatedString = buildAnnotatedString {
                append("Dengan melanjutkan, kamu menyetujui\n")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = Color.Black)) {
                    append("Syarat & Ketentuan")
                }
                append(" serta ")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = Color.Black)) {
                    append("Kebijakan Privasi")
                }
                append(" kami.")
            }
            Text(
                text = annotatedString,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Medium,
                    color = DesignTokens.Colors.TextSecondary
                ),
                textAlign = TextAlign.Center
            )

            // Bottom padding for the card before Dino peeks
            Spacer(modifier = Modifier.height(32.dp))
        }

        // Peeking Dino Illustration - Placed inside the inner Box so its TopCenter is the card's top edge
            Image(
                painter = painterResource(id = R.drawable.img_dino_peeking),
                contentDescription = "Peeking Dino",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = (-176.7f).dp) // Exact mathematical offset to make claws rest on the edge
                    .size(240.dp)
            )
        }
    }
}
