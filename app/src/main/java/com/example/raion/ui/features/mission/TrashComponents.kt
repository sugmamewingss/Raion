package com.example.raion.ui.features.mission

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.text.appendInlineContent
import com.example.raion.R
import com.example.raion.ui.theme.DesignTokens

@Composable
fun TrashTopBar(
    progress: Float, // 0.0 to 1.0
    onBackClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Tombol Kembali
        IconButton(onClick = onBackClick) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                contentDescription = "Kembali",
                tint = Color.Black,
                modifier = Modifier.size(32.dp)
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Progress Bar
        Box(
            modifier = Modifier
                .weight(1f)
                .height(18.dp)
                .background(Color(0xFFE0E0E0), RoundedCornerShape(50))
                .clip(RoundedCornerShape(50))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(fraction = progress)
                    .background(DesignTokens.Colors.OrangePrimary)
            )
        }
    }
}

@Composable
fun TrashItemCard(
    imageRes: Int,
    title: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(Color.White, RoundedCornerShape(12.dp))
            .border(1.dp, DesignTokens.Colors.OrangePrimary, RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Gambar Sampah
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Text Label Box (Orange Outline)
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.8f) // Not completely taking the width
                    .background(Color(0xFFFFF6ED), RoundedCornerShape(8.dp))
                    .border(1.dp, DesignTokens.Colors.OrangePrimary, RoundedCornerShape(8.dp))
                    .clip(RoundedCornerShape(8.dp)) // Memastikan ripple effect sesuai bentuk
                    .clickable { onClick() }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = title,
                    color = DesignTokens.Colors.OrangePrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
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
                        painter = painterResource(id = R.drawable.img_cling_2),
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
                        painter = painterResource(id = R.drawable.img_pow_1),
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
                        painter = painterResource(id = R.drawable.img_trashcan_main),
                        contentDescription = "Trash Can",
                        modifier = Modifier
                            .size(160.dp)
                            .offset(y = 7.dp),
                        contentScale = ContentScale.Fit
                    )
                    
                    // POW 2 (Di depan Kanan)
                    Image(
                        painter = painterResource(id = R.drawable.img_pow_2),
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
                val boxImageRes = if (isReady) R.drawable.img_box_open else R.drawable.img_box
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
                // === Fun Fact Carousel ===
                val funFacts = listOf(
                    Triple(
                        R.drawable.img_truck,
                        "Tahukah kamu? Setelah kita membuang sampah dengan benar, truk sampah akan mengumpulkannya untuk dibawa ke tempat pengolahan! \uD83D\uDE9B\u267B\uFE0F",
                        Color(0xFF2E7D32)
                    ),
                    Triple(
                        R.drawable.img_truck_blue,
                        "Fun Fact! Sampah yang sudah dikumpulkan oleh truk akan dibawa ke tempat khusus untuk dipilah dan diolah! \uD83C\uDF0D\u267B\uFE0F",
                        Color(0xFF1565C0)
                    ),
                    Triple(
                        R.drawable.img_recycle,
                        "Pernahkah kamu membayangkan? Sampah ternyata bisa didaur ulang menjadi barang baru yang berguna! \u267B\uFE0F",
                        Color(0xFF388E3C)
                    )
                )
                val pagerState = rememberPagerState(pageCount = { funFacts.size })

                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                ) { page ->
                    val (imageRes, text, _) = funFacts[page]
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = imageRes),
                            contentDescription = "Fun Fact Illustration",
                            modifier = Modifier
                                .height(120.dp)
                                .fillMaxWidth(0.6f),
                            contentScale = ContentScale.Fit
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = text,
                            fontSize = 11.sp,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Justify,
                            lineHeight = 16.sp,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Button Selesai (kiri) + Dot Indicator (kanan) sejajar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Selesai Button with Shadow Neo-Brutalism
                    Box(
                        modifier = Modifier
                            .width(120.dp)
                            .height(44.dp)
                            .background(Color(0xFF2C4331), RoundedCornerShape(8.dp))
                            .padding(bottom = 3.dp)
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
                                fontSize = 14.sp
                            )
                        }
                    }

                    // Dot Indicator
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        funFacts.forEachIndexed { index, _ ->
                            val isSelected = pagerState.currentPage == index
                            Box(
                                modifier = Modifier
                                    .padding(horizontal = 4.dp)
                                    .then(
                                        if (isSelected) Modifier
                                            .width(24.dp)
                                            .height(8.dp)
                                            .background(DesignTokens.Colors.OrangePrimary, RoundedCornerShape(4.dp))
                                        else Modifier
                                            .size(8.dp)
                                            .background(Color.LightGray, CircleShape)
                                    )
                            )
                        }
                    }
                }
            }
        }
    }
}

