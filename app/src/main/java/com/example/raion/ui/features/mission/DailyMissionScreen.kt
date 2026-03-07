package com.example.raion.ui.features.mission

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.raion.R
import com.example.raion.ui.theme.DesignTokens

@Composable
fun DailyMissionScreen(
    onBackClick: () -> Unit,
    onNavigateNext: () -> Unit = {}
) {
    Scaffold(
        containerColor = Color(0xFFFAF9F0) // Background kuning gading
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = DesignTokens.Dimensions.PaddingLarge)
        ) {
            Spacer(modifier = Modifier.height(DesignTokens.Dimensions.PaddingMedium))
            MissionTopProfile()
            
            Spacer(modifier = Modifier.height(24.dp))
            GobiBeraksiBanner()
            
            Spacer(modifier = Modifier.height(24.dp))
            MissionTrashSection(onNavigateNext = onNavigateNext)
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

// --- 1. Mission Top Profile ---
@Composable
fun MissionTopProfile() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Kotak Avatar Kiri
        Box(
            modifier = Modifier
                .width(70.dp)
                .background(Color.White, RoundedCornerShape(8.dp))
                .border(2.dp, Color(0xFF2C4331), RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Avatar Dino
                Image(
                    painter = painterResource(id = R.drawable.dinomini),
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(54.dp)
                        .padding(top = 4.dp),
                    contentScale = ContentScale.Fit
                )
                // Level Banner Bawah
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Color(0xFF2C4331), 
                            RoundedCornerShape(bottomStart = 6.dp, bottomEnd = 6.dp)
                        )
                        .padding(vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("LEVEL 10", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.ExtraBold)
                }
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Info Kanan (Nama, Kategori, Progress)
        Column(modifier = Modifier.weight(1f)) {
            Text(
                "Kevin Aditya Pratama",
                fontSize = 15.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.Black
            )
            Text(
                "3 - Sekolah Dasar",
                fontSize = 12.sp,
                color = Color.DarkGray
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Baris Peringkat & Koin
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Peringkat Pill
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    border = BorderStroke(1.dp, Color(0xFF50B498)),
                    color = Color.White
                ) {
                    Text(
                        "Peringkat 5",
                        color = Color(0xFF50B498),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
                
                // Si Paling Tertib Pill
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    border = BorderStroke(1.dp, Color(0xFF4A7A64)),
                    color = Color.White
                ) {
                    Text(
                        "Si Paling Tertib",
                        color = Color(0xFF4A7A64),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                // Gold Coin Box
                Box(
                    modifier = Modifier
                        .background(Color(0xFFFFDF8D), RoundedCornerShape(4.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.goldimage),
                            contentDescription = "Coin",
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            "100",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF8C6200)
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Progress Bar & XP
            Row(verticalAlignment = Alignment.CenterVertically) {
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
                            .fillMaxWidth(0.55f) // 55%
                            .background(DesignTokens.Colors.OrangePrimary, CircleShape)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    "55/100 XP",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray
                )
            }
        }
    }
}

// --- 2. Gobi Beraksi Banner ---
@Composable
fun GobiBeraksiBanner() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp) // Ukuran height ditambahkan agar Dino bisa off-bounds
    ) {
        // Container Utama Putih
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp) // Mengamankan ruang bawah untuk sayap dino offset
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White)
                .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(8.dp))
        ) {
            // Header Hijau Gelap
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF2C4331))
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "GOBI BERAKSI",
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp
                )
            }
            
            // Area Konten Putih (kosong, diisi tumpukan Box absolute)
            Box(modifier = Modifier.fillMaxWidth().height(100.dp))
        }

        // Tumpukan Elemen Mengambang
        
        // 1. Balon Teks "WoHoo!!!"
        Box(
            modifier = Modifier
                .padding(start = 24.dp, top = 60.dp)
                .background(DesignTokens.Colors.OrangePrimary, RoundedCornerShape(8.dp))
                .border(1.dp, Color(0xFFC05900), RoundedCornerShape(8.dp))
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Text(
                "WoHoo!!!",
                color = Color.White,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 18.sp
            )
        }
        
        // 2. Kilau kuning kecil di atas balon
        // Memakai text kuning (simplified) atau bisa digambar canvas sederhana, kita pakai unicode
        Text(
            "✨", 
            fontSize = 32.sp, 
            color = Color(0xFFFFD54F),
            modifier = Modifier.offset(x = 24.dp, y = 30.dp)
        )

        // 3. Super Dino Gambar (Offset kanan bawah)
        Image(
            painter = painterResource(id = R.drawable.superdino1),
            contentDescription = "Super Dino",
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 12.dp)
                .offset(y = 10.dp) // Offset ke bawah keluar dari kotak putih
                .size(140.dp),
            contentScale = ContentScale.Fit
        )
    }
}

// --- 3. Mission Trash Section ---
@Composable
fun MissionTrashSection(onNavigateNext: () -> Unit = {}) {
    Column {
        // Header Orange "Ayo kumpulkan sampahmu!"
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFFF6ED), RoundedCornerShape(8.dp))
                .border(1.dp, DesignTokens.Colors.OrangePrimary, RoundedCornerShape(8.dp))
                .padding(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    border = BorderStroke(1.dp, DesignTokens.Colors.OrangePrimary),
                    color = Color.White
                ) {
                    Text(
                        "0/5",
                        color = DesignTokens.Colors.OrangePrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    "Ayo kumpulkan sampahmu!",
                    color = DesignTokens.Colors.OrangePrimary,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Card Area Hijau Tosca
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFE2F0EA), RoundedCornerShape(12.dp))
                .border(1.dp, Color(0xFF4A7A64), RoundedCornerShape(12.dp))
                .padding(vertical = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Balon Kata "Mana Sampahmu?"
                Box(
                    modifier = Modifier
                        .background(DesignTokens.Colors.OrangePrimary, RoundedCornerShape(8.dp))
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        "Mana Sampahmu?",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
                
                // Stack Gambar Tong Sampah & Efek Pow
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    // POW 1 (Dibelakang Kiri)
                    Image(
                        painter = painterResource(id = R.drawable.pow1),
                        contentDescription = "Pow Left",
                        modifier = Modifier
                            .offset(x = (-60).dp, y = (-20).dp)
                            .size(70.dp),
                        contentScale = ContentScale.Fit
                    )
                    
                    // Shadow Oval Tong Sampah
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 12.dp)
                            .width(100.dp)
                            .height(14.dp)
                            .background(Color(0xFF9AACAA), RoundedCornerShape(50))
                    )
                    
                    // Tong Sampah Utama
                    Image(
                        painter = painterResource(id = R.drawable.trashcan),
                        contentDescription = "Trash Can",
                        modifier = Modifier
                            .size(160.dp)
                            .padding(bottom = 8.dp),
                        contentScale = ContentScale.Fit
                    )
                    
                    // POW 2 (Di depan Kanan)
                    Image(
                        painter = painterResource(id = R.drawable.pow2),
                        contentDescription = "Pow Right",
                        modifier = Modifier
                            .offset(x = 70.dp, y = 60.dp)
                            .size(80.dp),
                        contentScale = ContentScale.Fit
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Tombol Mulai Kumpulkan
                Button(
                    onClick = onNavigateNext,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF50B498)),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(48.dp)
                ) {
                    Text(
                        "Mulai Kumpulkan!",
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}
