package com.example.raion.ui.features.mission

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.raion.R
import com.example.raion.ui.theme.DesignTokens

enum class DailyMissionState {
    IN_PROGRESS,
    TRUCK_READY,
    MYSTERY_BOX_READY
}

@Composable
fun DailyMissionScreen(
    viewModel: DailyMissionViewModel = hiltViewModel(),
    missionState: DailyMissionState = DailyMissionState.IN_PROGRESS,
    onBackClick: () -> Unit,
    onNavigateNext: () -> Unit = {},
    onTruckFinishClick: () -> Unit = {},
    onClaimRewardClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        containerColor = Color(0xFFFAF9F0)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = DesignTokens.Dimensions.PaddingLarge)
        ) {
            // Optional: Back icon if you want to place it somewhere at the top
            // Since there was none in the Scaffold version, I'll keep the profile at the top
            Spacer(modifier = Modifier.height(DesignTokens.Dimensions.PaddingMedium))
            MissionTopProfile(
                userName = uiState.userName,
                schoolInfo = uiState.schoolInfo,
                level = uiState.level,
                rank = uiState.rank,
                title = uiState.title,
                coins = uiState.coins,
                xpProgressRatio = uiState.xpProgressRatio,
                xpProgressText = uiState.xpProgressText
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            GobiBeraksiBanner()
            
            Spacer(modifier = Modifier.height(24.dp))
            if (missionState == DailyMissionState.TRUCK_READY) {
                MissionTruckSection(
                    completedCount = uiState.completedMissionCount,
                    totalTarget = uiState.totalMissionTarget,
                    onFinishClick = onTruckFinishClick
                )
            } else {
                MissionTrashSection(
                    completedCount = uiState.completedMissionCount,
                    totalTarget = uiState.totalMissionTarget,
                    onNavigateNext = onNavigateNext
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            MysteryBoxSection(
                isReady = missionState == DailyMissionState.MYSTERY_BOX_READY,
                onClaimRewardClick = onClaimRewardClick
            )
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

// --- 1. Mission Top Profile ---
@Composable
fun MissionTopProfile(
    userName: String,
    schoolInfo: String,
    level: Int,
    rank: Int,
    title: String,
    coins: Int,
    xpProgressRatio: Float,
    xpProgressText: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        // Kotak Avatar Kiri — lebih tinggi sesuai desain
        Box(
            modifier = Modifier
                .width(75.dp)
                .background(Color.White, RoundedCornerShape(8.dp))
                .border(2.dp, Color(0xFF2C4331), RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Avatar Dino — lebih besar
                Image(
                    painter = painterResource(id = R.drawable.dinomini),
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(68.dp)
                        .padding(top = 4.dp, start = 2.dp, end = 2.dp),
                    contentScale = ContentScale.Fit
                )
                Spacer(modifier = Modifier.height(4.dp))
                // Level Banner Bawah
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Color(0xFF2C4331), 
                            RoundedCornerShape(bottomStart = 6.dp, bottomEnd = 6.dp)
                        )
                        .padding(vertical = 5.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "LEVEL $level",
                        color = Color.White,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(14.dp))

        // Info Kanan (Nama, Sekolah, Progress)
        Column(modifier = Modifier.weight(1f)) {
            Text(
                userName,
                fontSize = 15.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.Black
            )
            if (schoolInfo.isNotBlank()) {
                Text(
                    schoolInfo,
                    fontSize = 12.sp,
                    color = Color.DarkGray
                )
            }
            
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
                        "Peringkat $rank",
                        color = Color(0xFF50B498),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
                
                // Title Pill
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    border = BorderStroke(1.dp, Color(0xFF4A7A64)),
                    color = Color.White
                ) {
                    Text(
                        title,
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
                            "$coins",
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
                            .fillMaxWidth(xpProgressRatio.coerceIn(0f, 1f))
                            .background(DesignTokens.Colors.OrangePrimary, CircleShape)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    xpProgressText,
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
            .height(160.dp)
    ) {
        // Container Utama Putih
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp)
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
            
            // Area Konten Putih
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
        
        // 2. Percikan Cling di sebelah WoHoo!!!
        Image(
            painter = painterResource(id = R.drawable.cling1),
            contentDescription = "Sparkle",
            modifier = Modifier
                .offset(x = 15.dp, y = 42.dp)
                .size(30.dp),
            contentScale = ContentScale.Fit
        )

        // 3. Super Dino Gambar (Offset kanan bawah)
        Image(
            painter = painterResource(id = R.drawable.superdino1),
            contentDescription = "Super Dino",
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 12.dp)
                .offset(y = 10.dp)
                .size(140.dp),
            contentScale = ContentScale.Fit
        )
    }
}

// --- 3. Mission Trash Section ---
@Composable
fun MissionTrashSection(
    completedCount: Int = 0,
    totalTarget: Int = 10,
    onNavigateNext: () -> Unit = {}
) {
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
                        "$completedCount/$totalTarget",
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
                // Balon Kata "Mana Sampahmu?" dengan Cling
                Box {
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 20.dp) // ruang untuk cling
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
                    // Cling2 di sebelah kanan "Mana Sampahmu?"
                    Image(
                        painter = painterResource(id = R.drawable.cling2),
                        contentDescription = "Sparkle",
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .offset(x = 0.dp, y = (-8).dp)
                            .size(28.dp),
                        contentScale = ContentScale.Fit
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
                    
                    // Shadow Ellipse Tong Sampah (Canvas)
                    Canvas(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 8.dp)
                            .size(width = 110.dp, height = 16.dp)
                    ) {
                        drawOval(
                            color = Color(0xFF7B9695).copy(alpha = 0.45f)
                        )
                    }
                    
                    // Tong Sampah Utama (di atas shadow, sedikit lebih turun)
                    Image(
                        painter = painterResource(id = R.drawable.trashcan),
                        contentDescription = "Trash Can",
                        modifier = Modifier
                            .size(160.dp)
                            .offset(y = 7.dp),
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
                
                // Tombol Mulai Kumpulkan dengan Shadow Neo-Brutalism
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(52.dp)
                        .background(Color(0xFF2C4331), RoundedCornerShape(8.dp))
                        .padding(bottom = 4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFF50B498), RoundedCornerShape(8.dp))
                            .border(1.dp, Color(0xFF2C4331), RoundedCornerShape(8.dp))
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { onNavigateNext() },
                        contentAlignment = Alignment.Center
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
}

// --- 4. Mystery Box Section ---
@Composable
fun MysteryBoxSection(
    isReady: Boolean = false,
    onClaimRewardClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(12.dp))
            .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(12.dp))
            .padding(vertical = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 24.dp)
        ) {
            // Box Image with Ellipse Shadow
            Box(
                modifier = Modifier.size(80.dp),
                contentAlignment = Alignment.Center
            ) {
                // Shadow Ellipse di bawah box
                Canvas(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 2.dp)
                        .size(width = 60.dp, height = 12.dp)
                ) {
                    drawOval(
                        color = Color(0xFF7B9695).copy(alpha = 0.4f)
                    )
                }
                // Box Image (di atas shadow, sedikit lebih turun)
                val boxImageRes = if (isReady) R.drawable.boxopen else R.drawable.box
                Image(
                    painter = painterResource(id = boxImageRes),
                    contentDescription = "Mystery Box",
                    modifier = Modifier
                        .size(70.dp)
                        .offset(y = 5.dp),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                if (!isReady) {
                    // Default / Locked state: "Mistery Box!!" menggunakan orange pucat
                    Box(
                        modifier = Modifier
                            .height(42.dp)
                            .background(Color(0xFFE5A87B), RoundedCornerShape(8.dp)) // subtle shadow
                            .padding(bottom = 3.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .background(Color(0xFFFFF6ED), RoundedCornerShape(8.dp))
                                .border(1.dp, DesignTokens.Colors.OrangePrimary, RoundedCornerShape(8.dp))
                                .padding(horizontal = 24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "Mistery Box!!",
                                color = DesignTokens.Colors.OrangePrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        }
                    }
                } else {
                    // Ready / Unlocked state: "Mistery Box!!" (orange pucat) + "Dapatkan!" button
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(42.dp)
                            .background(Color(0xFFFFF6ED), RoundedCornerShape(8.dp))
                            .border(1.dp, DesignTokens.Colors.OrangePrimary, RoundedCornerShape(8.dp))
                            .padding(horizontal = 24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Mistery Box!!",
                            color = DesignTokens.Colors.OrangePrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(42.dp)
                            .background(Color(0xFFC05900), RoundedCornerShape(8.dp))
                            .padding(bottom = 3.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth()
                                .background(DesignTokens.Colors.OrangePrimary, RoundedCornerShape(8.dp))
                                .border(1.dp, Color(0xFFC05900), RoundedCornerShape(8.dp))
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { onClaimRewardClick() },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "Dapatkan!",
                                color = Color.White,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 15.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

// --- 5. Mission Truck Section (for TRUCK_READY state) ---
@Composable
fun MissionTruckSection(
    completedCount: Int = 10,
    totalTarget: Int = 10,
    onFinishClick: () -> Unit = {}
) {
    Column {
        // Header Orange "Sampah berhasil terkumpul!"
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
                        "$completedCount/$totalTarget",
                        color = DesignTokens.Colors.OrangePrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    "Sampah berhasil terkumpul!",
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
                .padding(vertical = 24.dp, horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Truck Image
                Image(
                    painter = painterResource(id = R.drawable.truck),
                    contentDescription = "Garbage Truck",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .padding(bottom = 16.dp),
                    contentScale = ContentScale.Fit
                )
                
                // Info Text
                Text(
                    text = "Tahukah kamu? Setelah kita membuang sampah dengan benar, truk sampah akan mengumpulkannya untuk dibawa ke tempat pengolahan! \uD83D\uDE9A♻️", // 🚛♻️
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 16.sp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Selesai Button with Shadow Neo-Brutalism
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .height(52.dp)
                        .background(Color(0xFF2C4331), RoundedCornerShape(8.dp))
                        .padding(bottom = 4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(DesignTokens.Colors.TealPrimary, RoundedCornerShape(8.dp))
                            .border(1.dp, Color(0xFF2C4331), RoundedCornerShape(8.dp))
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { onFinishClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Selesai!",
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}
