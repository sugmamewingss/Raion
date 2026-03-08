package com.example.raion.ui.features.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.raion.data.model.UserProfile
import com.example.raion.ui.features.home.HomeUiState
import com.example.raion.ui.theme.DesignTokens

@Composable
fun ProfileScreen(
    uiState: HomeUiState,
    onLogoutClick: () -> Unit,
    onEditProfileClick: () -> Unit
) {
    // Scaffold or Main Container for Profile Tab
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DesignTokens.Colors.CreamBackground)
            .verticalScroll(rememberScrollState())
    ) {
        // Space for system status bar if needed, since it's a pager page we just add some top padding
        Spacer(modifier = Modifier.height(32.dp))

        // 1. Header (Profil + Nama)
        ProfileHeader(userName = uiState.userName)

        // 2. Avatar Hero Section
        ProfileAvatarSection(coinBalance = uiState.totalCoins)

        // Wrapper for content below Avatar to give them side padding
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = DesignTokens.Dimensions.PaddingLarge)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // 3. User Info (Name, Date, Level, XP)
            ProfileInfoSection(
                fullName = uiState.fullName,
                joinDate = uiState.birthDate,
                level = uiState.userLevel,
                xpText = uiState.levelProgressText,
                xpRatio = uiState.levelProgressRatio
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 4. Edit Profile Button
            EditProfileButton(onClick = onEditProfileClick)

            Spacer(modifier = Modifier.height(32.dp))

            // 5. Streak & Tasks Retention Card
            StreakRetentionCard(
                streak = uiState.streak,
                onTaskClick = { /* TODO: Action when clicking Kerjakan Tugas */ }
            )

            Spacer(modifier = Modifier.height(32.dp))

            // 6. Monthly Badges Section
            MonthlyBadgesSection(onSeeAllClick = { /* TODO: Navigate to All Badges */ })

            Spacer(modifier = Modifier.height(48.dp))

            // 7. Logout Button
            LogoutButton(onClick = onLogoutClick)

            // Extra padding at bottom to prevent being covered by Bottom Navigation Bar
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}
