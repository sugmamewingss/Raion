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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.raion.R
import kotlinx.coroutines.launch

// --- WARNA DESAIN ---
val CreamBackground = Color(0xFFFFFBE6)
val OrangePrimary = Color(0xFFF4A261)
val TealPrimary = Color(0xFF6AC9AB)
val DarkBackground = Color(0xFF3D3D4E)
val LightGray = Color(0xFFEFEFEF)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateOut: () -> Unit
) {
    val isLoggedOut by viewModel.isLoggedOut.collectAsState()
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
        containerColor = CreamBackground
    ) { paddingValues ->
        // HorizontalPager memungkinkan user menswipe ke kanan/kiri
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) { page ->
            when (page) {
                0 -> HomeTabContent() // Halaman Utama (Scroll panjang)
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
fun HomeTabContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp),
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        TopProfileSection()
        Spacer(modifier = Modifier.height(24.dp))
        DailyTaskSection()
        Spacer(modifier = Modifier.height(24.dp))
        QuickMenuSection()
        Spacer(modifier = Modifier.height(24.dp))
        ArticleSection()
        Spacer(modifier = Modifier.height(32.dp))
        LeaderboardSection()
        Spacer(modifier = Modifier.height(32.dp))
        PointShopSection()
        Spacer(modifier = Modifier.height(100.dp))
    }
}

// --- KOMPONEN: Header Profil ---
@Composable
fun TopProfileSection() {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        // Avatar
        Image(
            painter = painterResource(id = R.drawable.dinoprofile),
            contentDescription = "Avatar",
            modifier = Modifier.size(56.dp).clip(CircleShape).background(Color.White).border(1.dp, Color.Gray, CircleShape)
        )
        Spacer(modifier = Modifier.width(12.dp))

        // Nama & Progress
        Column(modifier = Modifier.weight(1f)) {
            Text("Selamat Datang !", fontSize = 15.sp, color = Color.Black, fontWeight = FontWeight.ExtraBold, style = MaterialTheme.typography.headlineMedium)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Kevin", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Surface(shape = RoundedCornerShape(4.dp), border = BorderStroke(1.dp, Color.Gray)) {
                    Image(
                        painter = painterResource(id = R.drawable.streak),
                        contentDescription = "Streak",
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 0.3.dp).size(20.dp),
                        contentScale = ContentScale.Fit
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            // Progress Bar Level
            Box(modifier = Modifier.fillMaxWidth(0.8f).height(10.dp).background(Color.White, RoundedCornerShape(5.dp)).border(1.dp, Color.Gray, RoundedCornerShape(5.dp))) {
                Box(modifier = Modifier.fillMaxWidth(0.45f).fillMaxHeight().background(OrangePrimary, RoundedCornerShape(5.dp)))
            }
        }

        // Koin & Level
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.goldimg),
                contentDescription = "Gold Amount",
                modifier = Modifier.height(32.dp),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.height(4.dp))
            // [PERUBAHAN] Teks LEVEL 3 dihapus, diganti menggunakan gambar level.png
            Image(
                painter = painterResource(id = R.drawable.level),
                contentDescription = "Level Badge",
                modifier = Modifier.height(28.dp), // Sesuaikan angkanya jika gambarnya kurang besar/kecil
                contentScale = ContentScale.Fit
            )
        }
    }
}

// --- KOMPONEN: Daily Task ---
@Composable
fun DailyTaskSection() {
    Text("Daily Task", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
    Spacer(modifier = Modifier.height(8.dp))
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth().border(1.dp, Color.LightGray, RoundedCornerShape(16.dp))
    ) {
        Column {
            Row(modifier = Modifier.padding(16.dp)) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Misi yang belum selesai :", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("• Buang Sampah Botol", fontSize = 12.sp, color = Color.DarkGray)
                    Text("• Matikan Lampu Kamar", fontSize = 12.sp, color = Color.DarkGray)
                    Text("• Buang Sampah Botol", fontSize = 12.sp, color = Color.DarkGray)
                }
                // Gambar Kopi/Sampah
                Image(painter = painterResource(id = R.drawable.trashprogress), contentDescription = null, modifier = Modifier.size(60.dp))
            }
            // Footer Gelap
            Row(
                modifier = Modifier.fillMaxWidth().background(DarkBackground).padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Selesaikan Tugas Untuk mendapatkan poin", color = Color.White, fontSize = 11.sp)
                Button(
                    onClick = { /*TODO*/ },
                    colors = ButtonDefaults.buttonColors(containerColor = TealPrimary),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp),
                    modifier = Modifier.height(28.dp)
                ) { Text("Kerjakan", fontSize = 10.sp, color = Color.White) }
            }
        }
    }
}

// --- KOMPONEN: Quick Menu ---
@Composable
fun QuickMenuSection() {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        QuickMenuItem(icon = R.drawable.detailtugas, title = "Detail Tugas")
        QuickMenuItem(icon = R.drawable.journeyimg, title = "Journey")
        QuickMenuItem(icon = R.drawable.miniquiz, title = "Mini Quiz")
    }
}

@Composable
fun QuickMenuItem(icon: Int, title: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier.size(70.dp).background(Color.White, RoundedCornerShape(16.dp)).border(1.dp, Color.Gray, RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Image(painter = painterResource(id = icon), contentDescription = title, modifier = Modifier.size(40.dp))
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(title, fontSize = 10.sp, fontWeight = FontWeight.Medium)
    }
}

// --- KOMPONEN: Artikel Daur Ulang ---
@Composable
fun ArticleSection() {
    Card(
        shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp), modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            Box(modifier = Modifier.fillMaxWidth().height(120.dp).background(Color.LightGray)) {
                // Gambar tumpukan plastik
                Image(painter = painterResource(id = R.drawable.sampah1), contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
                // Tag "Plastik"
                Surface(color = DarkBackground, shape = RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp), modifier = Modifier.align(Alignment.BottomStart).padding(bottom = 8.dp)) {
                    Text("Plastik", color = Color.White, fontSize = 10.sp, modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp))
                }
            }
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Keajaiban Daur Ulang!", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text("Pernahkah kamu membayangkan punya tongkat ajaib yang bisa menyulap semua tumpukan sampah plastik jadi barang baru seketika? ✨", fontSize = 11.sp, color = Color.DarkGray, lineHeight = 16.sp)
            }
        }
    }
}

// --- KOMPONEN: Leaderboard ---
@Composable
fun LeaderboardSection() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Default.BarChart, contentDescription = null, modifier = Modifier.background(Color.White, RoundedCornerShape(8.dp)).padding(4.dp).size(24.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text("Papan Peringkat", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
    }
    Spacer(modifier = Modifier.height(16.dp))
    LeaderboardItem(imageRes = R.drawable.dinoprofile2, name = "Alvaro", points = "1000 poin", title = "Si Paling Bersih", color = TealPrimary, rank = "1st")
    Spacer(modifier = Modifier.height(8.dp))
    LeaderboardItem(imageRes = R.drawable.dinoprofile, name = "Evan", points = "950 poin", title = "Si Paling Rajin", color = OrangePrimary, rank = "2nd")
    Spacer(modifier = Modifier.height(8.dp))
    LeaderboardItem(imageRes = R.drawable.dinoprofile3, name = "Noah", points = "900 poin", title = "Si Paling Rajin", color = Color.Gray, rank = "3Rd")
}

@Composable
fun LeaderboardItem(imageRes: Int, name: String, points: String, title: String, color: Color, rank: String) {
    Row(
        modifier = Modifier.fillMaxWidth().background(Color.White, RoundedCornerShape(12.dp)).border(1.dp, Color.LightGray, RoundedCornerShape(12.dp)).padding(end = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(painter = painterResource(id = imageRes), contentDescription = null, modifier = Modifier.size(50.dp).padding(4.dp).clip(CircleShape))
        Column(modifier = Modifier.weight(1f).padding(start = 8.dp, top = 8.dp, bottom = 8.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(points, fontSize = 10.sp, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text("Achieve: $title", fontSize = 10.sp, color = Color.DarkGray)
        }
        Text(rank, fontWeight = FontWeight.ExtraBold, fontSize = 24.sp, color = color, modifier = Modifier.padding(start = 16.dp))
    }
}

// --- KOMPONEN: Toko Poin ---
@Composable
fun PointShopSection() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Default.Storefront, contentDescription = null, modifier = Modifier.background(Color.White, RoundedCornerShape(8.dp)).padding(4.dp).size(24.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text("Toko Poin", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
    }
    Spacer(modifier = Modifier.height(16.dp))
    Row(modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState())) {
        ShopItem(image = R.drawable.dino_face, price = "20 Poin")
        Spacer(modifier = Modifier.width(16.dp))
        ShopItem(image = R.drawable.dino_face, price = "10 Poin")
        Spacer(modifier = Modifier.width(16.dp))
        ShopItem(image = R.drawable.dino_face, price = "30 Poin")
    }
}

@Composable
fun ShopItem(image: Int, price: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.background(Color.White, RoundedCornerShape(12.dp)).border(1.dp, Color.LightGray, RoundedCornerShape(12.dp)).padding(8.dp)) {
        Image(painter = painterResource(id = image), contentDescription = null, modifier = Modifier.size(70.dp))
        Spacer(modifier = Modifier.height(8.dp))
        Surface(color = TealPrimary, shape = RoundedCornerShape(4.dp)) {
            Text("🪙 $price", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
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
            .padding(16.dp) // Memberikan efek melayang (floating)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(24.dp))
                .border(1.dp, Color.LightGray, RoundedCornerShape(24.dp))
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon 1: Home/Gua
            BottomNavIcon(
                icon = R.drawable.navbar1,
                isSelected = selectedIndex == 0,
                onClick = { onItemSelected(0) }
            )
            // Icon 2: Buku
            BottomNavIcon(
                icon = R.drawable.navbar2,
                isSelected = selectedIndex == 1,
                onClick = { onItemSelected(1) }
            )
            // Icon 3: Dino Koin
            BottomNavIcon(
                icon = R.drawable.navbar3,
                isSelected = selectedIndex == 2,
                onClick = { onItemSelected(2) }
            )
            // Icon 4: Profil Dino
            BottomNavIcon(
                icon = R.drawable.navbar4,
                isSelected = selectedIndex == 3,
                onClick = { onItemSelected(3) }
            )
        }
    }
}

@Composable
fun BottomNavIcon(icon: Int, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) OrangePrimary.copy(alpha = 0.5f) else Color.Transparent)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Image(painter = painterResource(id = icon), contentDescription = null, modifier = Modifier.size(32.dp))
    }
}

// Halaman Kosong Sementara untuk Navbar
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
            containerColor = CreamBackground
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                HomeTabContent()
            }
        }
    }
}