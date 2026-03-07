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
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.*
import androidx.compose.ui.text.withStyle
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
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
    onNavigateToDailyMission: () -> Unit = {}
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
                0 -> HomeTabContent(uiState = uiState, onNavigateToDailyMission = onNavigateToDailyMission) // Halaman Utama (Scroll panjang)
                1 -> DummyPage("Halaman Journey/Buku")
                2 -> DummyPage("Halaman Poin/Dino Kacamata")
                3 -> DummyPage("Halaman Profil/Dino Avatar")
            }
        }
    }
}

// ====================================================================
// ISI HALAMAN UTAMA (YANG BISA DI SCROLL KE BAWAH)
// ====================================================================
@Composable
fun HomeTabContent(uiState: HomeUiState, onNavigateToDailyMission: () -> Unit = {}) {
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
            progressText = uiState.levelProgressText,
            progressRatio = uiState.levelProgressRatio,
            coins = uiState.currentPoints,
            level = uiState.userLevel
        )
        Spacer(modifier = Modifier.height(DesignTokens.Dimensions.PaddingLarge))
        DailyTaskSection(
            tasks = uiState.incompleteTasks,
            organicCount = uiState.organicCount,
            inorganicCount = uiState.inorganicCount,
            onMulaiMisi = onNavigateToDailyMission
        )
        
        Spacer(modifier = Modifier.height(DesignTokens.Dimensions.PaddingLarge))
        QuickMenuSection()
        
        Spacer(modifier = Modifier.height(DesignTokens.Dimensions.PaddingLarge))
        ArticleSection()
        
        Spacer(modifier = Modifier.height(32.dp)) // Khusus bagian section besar
        LeaderboardSection(leaderboard = uiState.leaderboard)
        
        Spacer(modifier = Modifier.height(32.dp))
        PointShopSection(shopItems = uiState.shopItems)
        
        Spacer(modifier = Modifier.height(100.dp)) // Jarak ekstra agar tidak tertutup nav bar floating
    }
}

// --- KOMPONEN: Header Profil ---
@Composable
fun TopProfileSection(
    userName: String,
    streak: Int,
    progressText: String,
    progressRatio: Float,
    coins: Int,
    level: Int
) {
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp)) {
        // BARIS UTAMA: Profil (Kiri) & Lencana (Kanan)
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Kontainer Kiri: Avatar + Info (Teks & Streak)
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Avatar
                Image(
                    painter = painterResource(id = R.drawable.dinoprofile),
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .border(1.dp, Color.LightGray, CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(12.dp))

                // Info
                Column(verticalArrangement = Arrangement.Center) {
                    Text(
                        text = androidx.compose.ui.text.buildAnnotatedString {
                            append("Selamat Datang ")
                            withStyle(androidx.compose.ui.text.SpanStyle(fontWeight = FontWeight.ExtraBold)) {
                                append("$userName!")
                            }
                        },
                        fontSize = 15.sp,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    
                    // Baris untuk Badge di bawah nama
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // 1. Streak Pill
                        val streakColor = if (streak > 0) DesignTokens.Colors.OrangePrimary else Color.Gray
                        val streakBgColor = if (streak > 0) DesignTokens.Colors.OrangePrimary.copy(alpha = 0.1f) else Color(0xFFF5F5F5)
                        val streakBorderColor = if (streak > 0) DesignTokens.Colors.OrangePrimary.copy(alpha = 0.5f) else Color(0xFFE0E0E0)
                        
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, streakBorderColor),
                            color = streakBgColor
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.streak),
                                    contentDescription = "Streak",
                                    modifier = Modifier.height(18.dp), // Diperbesar dari 14.dp ke 18.dp
                                    contentScale = ContentScale.Fit
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = streak.toString(),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = streakColor
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.width(8.dp))
                        
                        // 2. Status Pill (Online/Active)
                        val activeColor = Color(0xFF4CAF50) // Hijau Online
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, activeColor.copy(alpha = 0.5f)),
                            color = activeColor.copy(alpha = 0.1f)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(activeColor)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Aktif",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = activeColor
                                )
                            }
                        }
                    }
                }
            }

            // Kontainer Kanan: Lencana Buatan Sendiri (Custom Shapes)
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // 1. Lencana Koin (Kotak Kuning dengan Ikon Koin)
                Box(
                    modifier = Modifier
                        .height(34.dp)
                        .background(Color(0xFFFFDF8D), RoundedCornerShape(8.dp))
                        .border(1.dp, Color(0xFFE5C87A), RoundedCornerShape(8.dp))
                        .padding(horizontal = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.goldimage),
                            contentDescription = "Coin",
                            modifier = Modifier.size(16.dp),
                            contentScale = ContentScale.Fit
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            coins.toString(),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF8C6200) // Warna cokelat gelap
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // 2. Lencana Level (Bentuk Perisai Abstrak Oranye)
                Box(
                    modifier = Modifier
                        .width(52.dp)
                        .height(34.dp),
                    contentAlignment = Alignment.Center
                ) {
                    val orangeColor = DesignTokens.Colors.OrangePrimary
                    val shadowColor = Color.LightGray
                    val strokeColor = Color(0xFFC05900) // Garis luar perisai lebih gelap
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val path = Path().apply {
                            moveTo(size.width / 2, 0f)
                            lineTo(size.width, size.height * 0.25f)
                            lineTo(size.width, size.height * 0.75f)
                            lineTo(size.width / 2, size.height)
                            lineTo(0f, size.height * 0.75f)
                            lineTo(0f, size.height * 0.25f)
                            close()
                        }
                        // Draw shadow
                        drawPath(path, color = shadowColor, alpha = 0.5f)
                        // Draw shape
                        drawPath(path, color = orangeColor)
                        // Draw stroke
                        drawPath(path, color = strokeColor, style = androidx.compose.ui.graphics.drawscope.Stroke(width = 1.dp.toPx()))
                    }
                    Text(
                        text = androidx.compose.ui.text.buildAnnotatedString {
                            withStyle(androidx.compose.ui.text.SpanStyle(fontSize = 7.sp)) {
                                append("LEVEL\n")
                            }
                            withStyle(androidx.compose.ui.text.SpanStyle(fontSize = 12.sp)) {
                                append(level.toString())
                            }
                        },
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        lineHeight = 11.sp,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(top = 2.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // BARIS BAWAH: Zona Progress Bar & Teks Inline
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Progress Bar Tipis
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(8.dp)
                    .background(Color(0xFFEEEEEE), CircleShape)
                    .clip(CircleShape),
                contentAlignment = Alignment.CenterStart
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(progressRatio)
                        .background(DesignTokens.Colors.OrangePrimary, CircleShape)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            // Teks Progress di kanan bar
            Text(
                text = progressText,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray
            )
        }
    }
}

// --- KOMPONEN: Daily Task ---
@Composable
fun DailyTaskSection(
    tasks: List<String>,
    organicCount: Int = 0,
    inorganicCount: Int = 0,
    onMulaiMisi: () -> Unit = {}
) {
    Text("Misi Gobi", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
    Spacer(modifier = Modifier.height(12.dp))
    Box(
        modifier = Modifier
            .fillMaxWidth()
            // Shadow tipis warna abu-abu di bawah, dengan lekukan di atas
            .background(Color.Gray, RoundedCornerShape(DesignTokens.Dimensions.CornerRadiusMedium))
            .padding(bottom = 3.dp) // Shadow sedikit ditipiskan
            .clip(RoundedCornerShape(DesignTokens.Dimensions.CornerRadiusMedium))
            .background(Color.White)
            .border(1.dp, Color.Gray, RoundedCornerShape(DesignTokens.Dimensions.CornerRadiusMedium))
    ) {
        Column {
            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Misi yang belum terselesaikan :", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    Spacer(modifier = Modifier.height(12.dp))

                    // Dua misi dinamis
                    val organicLimit = 5
                    val inorganicLimit = 5
                    val displayTasks = listOf(
                        "Buang sampah organik (${organicCount.coerceAtMost(organicLimit)}/$organicLimit)",
                        "Buang sampah anorganik (${inorganicCount.coerceAtMost(inorganicLimit)}/$inorganicLimit)"
                    )
                    displayTasks.forEach { task ->
                        DailyTaskItem(task)
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Buanglah sampah pada tempatnya ya! ✨",
                        fontSize = 10.sp,
                        fontStyle = FontStyle.Italic,
                        color = Color.Gray
                    )
                }
                // Gambar Kopi/Sampah
                Image(
                    painter = painterResource(id = R.drawable.trashprogress), 
                    contentDescription = null,
                    modifier = Modifier.size(80.dp),
                    contentScale = ContentScale.Fit
                )
            }
            // Footer Gelap
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Color(0xFF4A7A64) // Dark Green
                    )
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Kumpulkan sampahmu disini, Sobat Gobi!", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                
                // Button Neo-Brutalism tebal ke atas
                Box(
                    modifier = Modifier
                        .height(34.dp)
                        .background(Color.Gray, RoundedCornerShape(50)) // Bayangannya abu-abu di bawah
                        .padding(bottom = 2.dp) // Shadow bawah tipis
                        .background(Color.White, RoundedCornerShape(50)) // Warna sebenarnya
                        .border(1.dp, Color.Gray, RoundedCornerShape(50)) // Border tepi button
                        .padding(top = 1.dp) // Memberi efek lekukan di atas (mendorong konten turun)
                        .clickable { onMulaiMisi() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Mulai Misi!", 
                        fontSize = 10.sp, 
                        color = Color(0xFF4A7A64), 
                        fontWeight = FontWeight.ExtraBold, 
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun DailyTaskItem(text: String) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Lingkaran dot hijau kecil
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF4A7A64))
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text, fontSize = 12.sp, color = Color.DarkGray)
        }
        Spacer(modifier = Modifier.height(6.dp))
        // 0/5 Pill
        Surface(
            color = Color(0xFFFDECDA), // Light orange background
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.padding(start = 14.dp) // align under text
        ) {
            Row(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.trashicon),
                    contentDescription = "Trash",
                    modifier = Modifier.size(14.dp), // Icon sampah 
                    contentScale = ContentScale.Fit,
                    colorFilter = ColorFilter.tint(DesignTokens.Colors.OrangePrimary) // Mengubah warna ikon trash
                )
                Spacer(modifier = Modifier.width(6.dp))
                // Hardcoding 0/5 for visual match with design
                Text("0/5", color = DesignTokens.Colors.OrangePrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// --- KOMPONEN: Quick Menu ---
@Composable
fun QuickMenuSection() {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        QuickMenuItem(icon = R.drawable.detailmisi, title = "Detail Misi", bgColor = Color(0xFFE2D4F0))
        QuickMenuItem(icon = R.drawable.bukuharian, title = "Buku Harian", bgColor = Color(0xFFAFE0D9))
        QuickMenuItem(icon = R.drawable.tantanganjenius, title = "Tantangan Jenius", bgColor = Color(0xFFFFC0CB))
    }
}

@Composable
fun QuickMenuItem(icon: Int, title: String, bgColor: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        val shadowColor = Color.Gray // Warna abu-abu shadow
        val borderColors = Color.Gray // Warna border disesuaikan

        Box(
            modifier = Modifier
                .size(86.dp) // Dibesarkan dari 70.dp agar tidak terlalu mepet
                // Menggambar shadow manual yang bergeser ke bawah
                .background(shadowColor, RoundedCornerShape(26.dp))
                .padding(bottom = 3.dp) // Shadow ditipiskan
                .background(bgColor, RoundedCornerShape(26.dp)) 
                .border(1.dp, borderColors, RoundedCornerShape(26.dp)) // Border tepi luar
                .padding(top = 2.dp), // Menyisakan efek padding bibir atas sedikit
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = icon),
                contentDescription = title,
                modifier = Modifier
                    .size(56.dp) // Ukuran ikon juga dibesarkan menyesuaikan Box (sebelumnya 46.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Fit
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(title, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.Black)
    }
}

// --- KOMPONEN: Artikel Daur Ulang ---
@Composable
fun ArticleSection() {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(DesignTokens.Dimensions.BorderStrokeStardard, DesignTokens.Colors.LightGrayBorder, RoundedCornerShape(20.dp))
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
            ) {
                // Gambar tumpukan plastik
                Image(
                    painter = painterResource(id = R.drawable.sampah1),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                // Tag "Plastik" di sudut kiri bawah gambar
                Surface(
                    color = DesignTokens.Colors.DarkBackground,
                    shape = RoundedCornerShape(topEnd = DesignTokens.Dimensions.PaddingSmall, bottomEnd = DesignTokens.Dimensions.PaddingSmall),
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(bottom = 12.dp)
                ) {
                    Text(
                        "Plastik",
                        color = Color.White,
                        fontSize = 10.sp,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }
            }
            Column(modifier = Modifier.padding(DesignTokens.Dimensions.PaddingMedium)) {
                Text(
                    text = androidx.compose.ui.text.buildAnnotatedString {
                        withStyle(androidx.compose.ui.text.SpanStyle(fontWeight = FontWeight.ExtraBold, color = Color.Black)) {
                            append("Keajaiban ")
                        }
                        withStyle(androidx.compose.ui.text.SpanStyle(fontWeight = FontWeight.ExtraBold, color = Color(0xFF4A7A64))) {
                            append("Daur ")
                        }
                        withStyle(androidx.compose.ui.text.SpanStyle(fontWeight = FontWeight.ExtraBold, color = Color(0xFF1B2A47))) {
                            append("Ulang!")
                        }
                    },
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "Pernahkah kamu membayangkan punya tongkat ajaib yang bisa menyulap semua tumpukan sampah plastik jadi barang baru seketika? ✨",
                    fontSize = 11.sp,
                    color = Color.DarkGray,
                    lineHeight = 16.sp,
                    maxLines = 3 // Prevents text overflow
                )
                Spacer(modifier = Modifier.height(12.dp))
                // Pagination dots
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.width(20.dp).height(6.dp).clip(RoundedCornerShape(50)).background(DesignTokens.Colors.TealPrimary))
                    Spacer(modifier = Modifier.width(4.dp))
                    Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(Color.Gray.copy(alpha = 0.5f)))
                    Spacer(modifier = Modifier.width(4.dp))
                    Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(Color.Gray.copy(alpha = 0.5f)))
                }
            }
        }
    }
}

// --- KOMPONEN: Leaderboard ---
@Composable
fun LeaderboardSection(leaderboard: List<LeaderboardEntry>) {
    val displayLeaderboard = if (leaderboard.isEmpty()) {
        listOf(
            LeaderboardEntry(name = "Alvaro", points = "1000 xp", title = "Si paling bersih", rank = "1st", isPrimary = true, isSecondary = false),
            LeaderboardEntry(name = "Evan", points = "950 xp", title = "Si paling rajin", rank = "2nd", isPrimary = false, isSecondary = true),
            LeaderboardEntry(name = "Noah", points = "800 xp", title = "Si paling rapi", rank = "3rd", isPrimary = false, isSecondary = false)
        )
    } else leaderboard

    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = Icons.Default.BarChart, 
            contentDescription = null, 
            modifier = Modifier
                .background(Color.White, RoundedCornerShape(8.dp))
                .border(1.dp, Color(0xFF2C4331), RoundedCornerShape(8.dp))
                .padding(8.dp)
                .size(24.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text("Papan Peringkat", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
    }
    Spacer(modifier = Modifier.height(DesignTokens.Dimensions.PaddingMedium))
    
    displayLeaderboard.forEachIndexed { index, entry ->
        val rankColor = when {
            entry.isPrimary -> Color(0xFF4A7A64) // Dark Green
            entry.isSecondary -> DesignTokens.Colors.OrangePrimary
            else -> Color(0xFF8F8F8F) // Gray
        }
        val rankTextColor = when {
            entry.isPrimary -> DesignTokens.Colors.RankGold
            entry.isSecondary -> DesignTokens.Colors.OrangePrimary
            else -> Color(0xFF8F8F8F)
        }
        val avatarRes = when(index) {
            0 -> R.drawable.dinoprofile2
            1 -> R.drawable.dinoprofile
            else -> R.drawable.dinoprofile3
        }

        LeaderboardItem(
            imageRes = avatarRes, 
            name = entry.name, 
            points = entry.points, 
            title = entry.title, 
            rankColor = rankColor, 
            rank = entry.rank, 
            rankTextColor = rankTextColor
        )
        if (index < displayLeaderboard.size - 1) {
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun LeaderboardItem(
    imageRes: Int, 
    name: String, 
    points: String, 
    title: String, 
    rankColor: Color, 
    rank: String,
    rankTextColor: Color
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(84.dp), // Height to fit everything well including the overlapping avatar
        contentAlignment = Alignment.CenterStart
    ) {
        // Main Container
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 30.dp) // Space for overlapping avatar
                .height(76.dp)
                .background(Color.White, RoundedCornerShape(16.dp))
                .border(DesignTokens.Dimensions.BorderStrokeStardard, Color(0xFFE0E0E0), RoundedCornerShape(16.dp))
                .clip(RoundedCornerShape(16.dp)) // Clip the entire container for the right circle effect
        ) {
            // Background Layer: Top colored, Bottom white
            Column(modifier = Modifier.fillMaxSize()) {
                Box(modifier = Modifier.weight(1.1f).fillMaxWidth().background(rankColor))
                Box(modifier = Modifier.weight(0.9f).fillMaxWidth().background(Color.White))
            }
            
            // Text Content Layer
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left Text Area
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        // padding end increased to 68.dp so text doesn't hide behind the right circle cutout
                        .padding(start = 42.dp, end = 68.dp, top = 8.dp, bottom = 8.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    // Top area (Name and Points)
                    Row(
                        modifier = Modifier.fillMaxWidth().weight(1f),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top // Align to top slightly
                    ) {
                        Text(name, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = Color.White)
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(points, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, fontStyle = FontStyle.Italic, color = Color.White, modifier = Modifier.alignByBaseline())
                            Spacer(modifier = Modifier.width(2.dp))
                            Text("poin", fontSize = 12.sp, fontStyle = FontStyle.Italic, color = Color.White, modifier = Modifier.alignByBaseline())
                        }
                    }
                    
                    // Bottom area (Achieve)
                    Row(
                        modifier = Modifier.fillMaxWidth().weight(0.9f),
                        verticalAlignment = Alignment.Bottom // Align to bottom
                    ) {
                        Text("Achieve:", fontSize = 12.sp, color = Color.DarkGray)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(title, fontSize = 12.sp, color = Color.Black, fontWeight = FontWeight.Bold, fontStyle = FontStyle.Italic)
                    }
                }
            }

            // Right Circle Cutout Area
            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .size(76.dp) // Size EXACTLY matches Box height so top & bottom are NOT clipped flat
                    .offset(x = 16.dp) // shift it right to create the semi-circle effect on boundary
                    .background(Color.White, CircleShape),
                contentAlignment = Alignment.CenterStart
            ) {
                // The Rank text inside the circle
                Row(
                    verticalAlignment = Alignment.Bottom, 
                    modifier = Modifier.padding(start = 14.dp, bottom = 12.dp)
                ) {
                    val rankNumber = rank.takeWhile { it.isDigit() }
                    val rankSuffix = rank.dropWhile { it.isDigit() }.lowercase()
                    Text(rankNumber, fontWeight = FontWeight.ExtraBold, fontSize = 34.sp, color = rankTextColor, modifier = Modifier.alignByBaseline())
                    Text(rankSuffix, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, color = rankTextColor, modifier = Modifier.alignByBaseline())
                }
            }
        }

        // Avatar (Overlapping)
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = null,
            modifier = Modifier
                .size(68.dp)
                .clip(CircleShape)
                .background(Color.White)
                .border(2.dp, Color(0xFFE0E0E0), CircleShape)
        )
    }
}

// --- KOMPONEN: Toko Poin ---
data class ShopItemUI(
    val image: Int, 
    val price: String, 
    val shadowWidth: androidx.compose.ui.unit.Dp = 70.dp, 
    val shadowHeight: androidx.compose.ui.unit.Dp = 14.dp, 
    val shadowOffsetX: androidx.compose.ui.unit.Dp = 0.dp, 
    val shadowOffsetY: androidx.compose.ui.unit.Dp = 0.dp,
    val imageOffsetY: androidx.compose.ui.unit.Dp = 0.dp
)

@Composable
fun PointShopSection(shopItems: List<ShopItemData>) {
    val items = listOf(
        ShopItemUI(image = R.drawable.clothes1, price = "20 Poin", shadowWidth = 76.dp, shadowHeight = 12.dp, imageOffsetY = (-8).dp),
        ShopItemUI(image = R.drawable.hoodie1, price = "10 Poin", shadowWidth = 72.dp, shadowHeight = 12.dp, imageOffsetY = (-8).dp),
        ShopItemUI(image = R.drawable.cap1, price = "30 Poin", shadowWidth = 60.dp, shadowHeight = 12.dp, shadowOffsetX = 12.dp, shadowOffsetY = 6.dp, imageOffsetY = (-12).dp),
        ShopItemUI(image = R.drawable.clothes2, price = "40 Poin", shadowWidth = 68.dp, shadowHeight = 12.dp, imageOffsetY = (-8).dp)
    )

    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = Icons.Default.Storefront, 
            contentDescription = null, 
            modifier = Modifier
                .background(Color.White, RoundedCornerShape(8.dp))
                .border(1.dp, Color(0xFF2C4331), RoundedCornerShape(8.dp))
                .padding(8.dp)
                .size(24.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text("Toko Poin", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
    }
    Spacer(modifier = Modifier.height(DesignTokens.Dimensions.PaddingMedium))
    
    // Outer Border Container
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFF2C4331), RoundedCornerShape(16.dp))
            .background(Color.White, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState())) {
            items.forEachIndexed { index, item ->
                ShopItem(
                    image = item.image, 
                    price = item.price,
                    shadowWidth = item.shadowWidth,
                    shadowHeight = item.shadowHeight,
                    shadowOffsetX = item.shadowOffsetX,
                    shadowOffsetY = item.shadowOffsetY,
                    imageOffsetY = item.imageOffsetY
                )
                if (index < items.size - 1) {
                    Spacer(modifier = Modifier.width(12.dp))
                }
            }
        }
    }
}

@Composable
fun ShopItem(
    image: Int, 
    price: String,
    shadowWidth: androidx.compose.ui.unit.Dp = 70.dp, 
    shadowHeight: androidx.compose.ui.unit.Dp = 14.dp, 
    shadowOffsetX: androidx.compose.ui.unit.Dp = 0.dp, 
    shadowOffsetY: androidx.compose.ui.unit.Dp = 0.dp,
    imageOffsetY: androidx.compose.ui.unit.Dp = 0.dp
) {
    Column(
        modifier = Modifier.width(110.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Kotak Baju
        Box(
            modifier = Modifier
                .size(110.dp)
                .background(Color(0xFFEEF5F4), RoundedCornerShape(12.dp)) // Light mint color background
                .border(1.dp, Color(0xFF2C4331), RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            // Heart icon in top start
            Icon(
                imageVector = Icons.Default.FavoriteBorder,
                contentDescription = null,
                tint = Color.LightGray,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(10.dp)
                    .size(18.dp)
            )
            
            // Efek shadow oval sejati (Ellipse murni) di bagian bawah baju
            Canvas(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 12.dp) // Jarak shadow dari border kotak utama
                    .offset(x = shadowOffsetX, y = shadowOffsetY)
                    .size(width = shadowWidth, height = shadowHeight) // Dimensi elips
            ) {
                drawOval(
                    color = Color(0xFF7B9695).copy(alpha = 0.5f) // Opacity yang lebih tipis/transparan
                )
            }

            // Item Image
            Image(
                painter = painterResource(id = image),
                contentDescription = null,
                modifier = Modifier
                    .offset(y = imageOffsetY) // Angkat gambar cukup jauh atau miring agar pas dengan posisi shadow
                    .size(76.dp),
                contentScale = ContentScale.Fit
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        
        // Kotak Harga dengan Shadow Neo-Brutalism
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(38.dp)
                .background(Color(0xFF2C4331), RoundedCornerShape(6.dp)) // Shadow gelap di bawah
                .padding(bottom = 3.dp) // Ketebalan shadow
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF50B498), RoundedCornerShape(6.dp)) // Teal color
                    .border(1.dp, Color(0xFF2C4331), RoundedCornerShape(6.dp)),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Gold Icon
                    Image(
                        painter = painterResource(id = R.drawable.goldimage),
                        contentDescription = null,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = price,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}


// ====================================================================
// KOMPONEN: BOTTOM NAVIGATION BAR CUSTOM
// ====================================================================
@Composable
fun CustomBottomNavBar(
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = DesignTokens.Dimensions.PaddingMedium, vertical = DesignTokens.Dimensions.PaddingLarge) // Lebih melayang
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(DesignTokens.Dimensions.CornerRadiusLarge))
                .border(DesignTokens.Dimensions.BorderStrokeStardard, DesignTokens.Colors.LightGrayBorder.copy(alpha=0.5f), RoundedCornerShape(DesignTokens.Dimensions.CornerRadiusLarge))
                .padding(horizontal = DesignTokens.Dimensions.PaddingLarge, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween, // Distribusi merata
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomNavIcon(icon = R.drawable.navbar1, isSelected = selectedIndex == 0, onClick = { onItemSelected(0) })
            BottomNavIcon(icon = R.drawable.navbar2, isSelected = selectedIndex == 1, onClick = { onItemSelected(1) })
            BottomNavIcon(icon = R.drawable.navbar3, isSelected = selectedIndex == 2, onClick = { onItemSelected(2) })
            BottomNavIcon(icon = R.drawable.navbar4, isSelected = selectedIndex == 3, onClick = { onItemSelected(3) })
        }
    }
}

@Composable
fun BottomNavIcon(icon: Int, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(52.dp)
            .clip(RoundedCornerShape(DesignTokens.Dimensions.CornerRadiusMedium))
            .background(if (isSelected) DesignTokens.Colors.OrangePrimary.copy(alpha = 0.4f) else Color.Transparent)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Image(painter = painterResource(id = icon), contentDescription = null, modifier = Modifier.size(32.dp))
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