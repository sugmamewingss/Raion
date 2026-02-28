package com.example.raion.ui.features.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import com.example.raion.R
import com.example.raion.ui.features.auth.components.AuthPrimaryButton
import com.example.raion.ui.theme.DesignTokens

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateOut: () -> Unit
) {
    val isLoggedOut by viewModel.isLoggedOut.collectAsState()

    LaunchedEffect(isLoggedOut) {
        if (isLoggedOut) {
            onNavigateOut()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DesignTokens.Colors.CardBackground)
            .padding(DesignTokens.Dimensions.PaddingLarge),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.dinoyeay),
            contentDescription = "Dino Happy",
            modifier = Modifier
                .size(200.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "SELAMAT DATANG",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        )
        Text(
            text = "DI RAION APP",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold,
                color = DesignTokens.Colors.BrandPrimary
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Halaman ini hanya contoh (mock) untuk mengetes flow Otentikasi.",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = DesignTokens.Colors.TextSecondary
            ),
            modifier = Modifier.padding(horizontal = 24.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        Spacer(modifier = Modifier.height(48.dp))

        AuthPrimaryButton(
            text = "KELUAR (LOGOUT)",
            onClick = { viewModel.logout() },
            modifier = Modifier.fillMaxWidth(0.8f)
        )
    }
}
