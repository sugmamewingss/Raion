package com.example.raion.ui.features.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.*
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.text.withStyle
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.foundation.Canvas
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.raion.R
import com.example.raion.ui.theme.DesignTokens
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateOut: () -> Unit,
    onStartMission: () -> Unit = {}
) {
    val isLoggedOut by viewModel.isLoggedOut.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(isLoggedOut) {
        if (isLoggedOut) onNavigateOut()
    }

    // PagerState untuk mengontrol slide ke samping (Ada 4 halaman sesuai icon navbar)
    val pagerState = rememberPagerState(pageCount = { 4 })
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        bottomBar = {
            CustomBottomNavBar(
                selectedIndex = pagerState.currentPage,
                onItemSelected = { index ->
                    // Animasi pindah halaman saat icon navbar diklik
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                }
            )
        },
        // Container color di override di Theme.kt namun diset ulang di sini untuk safety margin jika diperlukan
        containerColor = DesignTokens.Colors.CreamBackground 
    ) { paddingValues ->
        // HorizontalPager memungkinkan user menswipe ke kanan/kiri
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) { page ->
            when (page) {
                0 -> HomeTabContent(uiState = uiState, onStartMission = onStartMission)
                1 -> DummyPage("Halaman Journey/Buku")
                2 -> DummyPage("Halaman Poin/Dino Kacamata")
                3 -> DummyPage("Halaman Profil/Dino Avatar")
            }
        }
    }
}

@Composable
fun HomeTabContent(uiState: HomeUiState, onStartMission: () -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = DesignTokens.Dimensions.PaddingLarge), // Padding samping layar standar
    ) {
        Spacer(modifier = Modifier.height(DesignTokens.Dimensions.PaddingMedium))
        TopProfileSection(
            userName = uiState.userName,
            streak = uiState.streak,
            isActive = uiState.isActive,
            isMissionCompletedToday = uiState.isMissionCompletedToday,
            progressText = uiState.levelProgressText,
            progressRatio = uiState.levelProgressRatio,
            coins = uiState.totalCoins,
            level = uiState.userLevel
        )
        Spacer(modifier = Modifier.height(DesignTokens.Dimensions.PaddingLarge))
        ActiveMissionCard(mission = uiState.activeMissions.firstOrNull(), onStartMission = onStartMission)
        
        Spacer(modifier = Modifier.height(DesignTokens.Dimensions.PaddingLarge))
        QuickNavMenu(onItemClick = {})
        
        Spacer(modifier = Modifier.height(DesignTokens.Dimensions.PaddingLarge))
        EducationalCarousel(articles = uiState.eduArticles)
        
        Spacer(modifier = Modifier.height(32.dp)) // Khusus bagian section besar
        LeaderboardSection(leaderboard = uiState.leaderboard)
        
        Spacer(modifier = Modifier.height(32.dp))
        PointShopSection(shopItems = uiState.pointShopItems)
        
        Spacer(modifier = Modifier.height(100.dp)) // Jarak ekstra agar tidak tertutup nav bar floating
    }
}

@Composable
fun CustomBottomNavBar(
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit
) {
    val navItems = listOf(
        Pair(R.drawable.navbar1, "Beranda"),
        Pair(R.drawable.navbar2, "Misi"),
        Pair(R.drawable.navbar3, "Toko"),
        Pair(R.drawable.navbar4, "Profil")
    )

    Column {
        // Thin horizontal divider at top
        HorizontalDivider(
            color = DesignTokens.Colors.LightGrayBorder.copy(alpha = 0.5f),
            thickness = 1.dp
        )

        @OptIn(ExperimentalMaterial3Api::class)
        CompositionLocalProvider(LocalRippleConfiguration provides null) {
            NavigationBar(
                containerColor = DesignTokens.Colors.CreamBackground,
                tonalElevation = 0.dp
            ) {
                navItems.forEachIndexed { index, (icon, label) ->
                    NavigationBarItem(
                        selected = selectedIndex == index,
                        onClick = { onItemSelected(index) },
                        icon = {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(RoundedCornerShape(DesignTokens.Dimensions.CornerRadiusMedium))
                                    .then(
                                        if (selectedIndex == index) Modifier
                                            .background(Color(0xFFF09D51).copy(alpha = 0.59f))
                                            .border(1.5.dp, Color(0xFFF09D51), RoundedCornerShape(DesignTokens.Dimensions.CornerRadiusMedium))
                                        else Modifier
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = icon),
                                    contentDescription = label,
                                    modifier = Modifier.size(26.dp)
                                )
                            }
                        },
                        label = {
                            Text(
                                text = label,
                                fontSize = 10.sp,
                                fontWeight = if (selectedIndex == index) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.Unspecified,
                            unselectedIconColor = Color.Unspecified,
                            selectedTextColor = Color(0xFFF09D51),
                            unselectedTextColor = Color.Gray,
                            indicatorColor = Color.Transparent
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun DummyPage(title: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(title, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color.Gray)
    }
}

@Preview(showBackground = true, heightDp = 1200)
@Composable
fun HomeScreenPreview() {
    com.example.raion.ui.theme.RaionTheme {
        Scaffold(
            bottomBar = {
                CustomBottomNavBar(
                    selectedIndex = 0,
                    onItemSelected = {}
                )
            },
            containerColor = DesignTokens.Colors.CreamBackground
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // Berikan dummy UI State untuk preview rendering
                HomeTabContent(uiState = HomeUiState())
            }
        }
    }
}