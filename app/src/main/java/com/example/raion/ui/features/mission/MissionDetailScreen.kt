package com.example.raion.ui.features.mission

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Lock
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
import androidx.compose.ui.zIndex
import com.example.raion.R
import com.example.raion.ui.theme.DesignTokens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MissionDetailScreen(
    onNavigateBack: () -> Unit,
    userName: String = "Kevin Aditya Pratama",
    userLevel: Int = 3,
    missionsCompleted: Int = 38,
    totalXp: Int = 500,
    totalCoins: Int = 1000, // Added default value for totalCoins
    onStartMission: () -> Unit,
    onNavigateToMissionRincian: () -> Unit
) {
    val darkGreenColor = Color(0xFF1B4F45)
    val tabSelesaiColor = Color(0xFF185A48)
    val darkGreenButton = Color(0xFF568F7B)
    val textColor = Color(0xFF1B4D43)
    val lightGreenCard = Color(0xFFE8F6F1)
    val lightBlueCard = Color(0xFFE8F4FE)
    val lightYellowCard = Color(0xFFFFF7E6)
    
    // Aktif tab specific colors
    val lightCyanBlue = Color(0xFFDFF1FD) // Used for the "Aktif" card background
    val greenBubble = Color(0xFF1B4F45) // Used for the chat bubble
    val paleYellowCover = Color(0xFFF7EED3) // Pale yellow behind mascot

    var selectedTab by remember { mutableStateOf(0) } // 0 = Aktif, 1 = Selesai

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "DETAIL MISI",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.Black,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            modifier = Modifier.size(32.dp),
                            tint = Color.Black
                        )
                    }
                },
                actions = {
                    // Empty spacer to balance the title
                    Spacer(modifier = Modifier.width(48.dp))
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DesignTokens.Colors.CreamBackground
                )
            )
        },
        containerColor = DesignTokens.Colors.CreamBackground
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Tab (Aktif / Selesai)
            Row(
                modifier = Modifier
                    .width(280.dp)
                    .height(40.dp)
                    .border(1.dp, tabSelesaiColor, RoundedCornerShape(8.dp))
                    .clip(RoundedCornerShape(8.dp))
            ) {
                // Aktif Tab
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(if (selectedTab == 0) tabSelesaiColor else Color.White)
                        .clickable { selectedTab = 0 },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Aktif",
                        fontWeight = FontWeight.Bold,
                        color = if (selectedTab == 0) Color.White else tabSelesaiColor,
                        fontSize = 14.sp
                    )
                }
                // Selesai Tab
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(if (selectedTab == 1) tabSelesaiColor else Color.White)
                        .clickable { selectedTab = 1 },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Selesai",
                        fontWeight = FontWeight.Bold,
                        color = if (selectedTab == 1) Color.White else tabSelesaiColor,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            if (selectedTab == 0) {
                // ==========================================
                // AKTIF TAB CONTENT
                // ==========================================
                
                // Big outline box containing everything in Aktif
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .border(1.dp, darkGreenColor, RoundedCornerShape(12.dp))
                        .background(Color.Transparent, RoundedCornerShape(12.dp))
                        .padding(bottom = 24.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // 1. Hero Card: Mascot + Speech Bubble
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(210.dp)
                                .padding(16.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(paleYellowCover)
                                .border(1.dp, darkGreenColor, RoundedCornerShape(12.dp))
                        ) {
                            Row(
                                modifier = Modifier.fillMaxSize(),
                                verticalAlignment = Alignment.Bottom
                            ) {
                                // Mascot on the left (peeking from bottom)
                                Image(
                                    painter = painterResource(id = R.drawable.dinodetailmisi),
                                    contentDescription = "Mascot Aktif",
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxHeight()
                                        .padding(top = 16.dp),
                                    contentScale = ContentScale.Fit
                                )
                                
                                // Speech Bubble on the right
                                Box(
                                    modifier = Modifier
                                        .weight(1.2f)
                                        .padding(end = 16.dp, bottom = 40.dp, top = 20.dp)
                                ) {
                                    // Main Bubble
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(greenBubble)
                                            .padding(12.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "Jangan lupa\nmengerjakan\nmisinya ya, sobat\nGobi!",
                                            color = Color.White,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                    
                                    // Bubble Tail (triangle pointing to mascot)
                                    // A simple way is to use a rotated square or a canvas, here's a canvas tail
                                    androidx.compose.foundation.Canvas(
                                        modifier = Modifier
                                            .size(20.dp)
                                            .align(Alignment.BottomStart)
                                            .offset(x = (-4).dp, y = 14.dp)
                                    ) {
                                        val path = androidx.compose.ui.graphics.Path().apply {
                                            moveTo(0f, 0f)
                                            lineTo(size.width, 0f)
                                            lineTo(size.width, size.height)
                                            close()
                                        }
                                        drawPath(path, color = greenBubble)
                                    }
                                }
                            }
                        }
                        
                        // 2. Active Mission Card
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(lightCyanBlue)
                                .border(1.dp, darkGreenColor, RoundedCornerShape(8.dp))
                                .padding(start = 16.dp, end = 12.dp, top = 12.dp, bottom = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Date Column
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                val today = java.time.LocalDate.now()
                                val dayNum = today.dayOfMonth
                                val monthName = today.month.getDisplayName(java.time.format.TextStyle.FULL, java.util.Locale("id", "ID"))
                                
                                Text(
                                    text = dayNum.toString(),
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.Black,
                                    lineHeight = 24.sp
                                )
                                Text(
                                    text = monthName,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = darkGreenColor
                                )
                            }
                            
                            // Vertical Divider line
                            Spacer(modifier = Modifier.width(12.dp))
                            Box(
                                modifier = Modifier
                                    .width(1.dp)
                                    .height(40.dp)
                                    .background(darkGreenColor.copy(alpha = 0.5f))
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            
                            // Mission Status & Button
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Misi Gobi belum selesai, nih!",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = darkGreenColor
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                
                                // Small Action Button
                                Button(
                                    onClick = onNavigateToMissionRincian,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = darkGreenButton,
                                        contentColor = Color.White
                                    ),
                                    shape = RoundedCornerShape(4.dp),
                                    contentPadding = PaddingValues(0.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(26.dp)
                                ) {
                                    Text(
                                        text = "Lihat rincian misi!",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // 3. Locked Mission Cards (Placeholders)
                        for (i in 1..4) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp)
                                    .padding(bottom = 12.dp)
                                    .height(64.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFFE8E8E8)) // Light gray
                                    .border(1.dp, darkGreenColor.copy(alpha = 0.6f), RoundedCornerShape(8.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = "Locked",
                                    tint = Color.Gray,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
            } else {
                // ==========================================
                // SELESAI TAB CONTENT (Old Original Content)
                // ==========================================
                Text(
                    text = "GOBI RECAP",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Black,
                    color = textColor,
                    letterSpacing = 1.sp
                )

            // Mascot & Info Card Stack
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(top = 16.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                // Main White Card
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White,
                    border = BorderStroke(1.dp, darkGreenColor),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 220.dp) // Space for mascot overlap
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // 1. User Info Header
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // Avatar
                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(CircleShape)
                                    .border(2.dp, Color.LightGray, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.dinoprofile),
                                    contentDescription = "Avatar",
                                    modifier = Modifier.size(50.dp).clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                            }
                            
                            Spacer(modifier = Modifier.width(16.dp))
                            
                            // Name & Level
                            Column {
                                Text(
                                    text = userName,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.Black
                                )
                                Text(
                                    text = "$userLevel - Sekolah Dasar",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.DarkGray
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        // 2. Stats Rows
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            
                            // Left Column: Quests Completed
                            Column(
                                modifier = Modifier
                                    .weight(1.5f)
                                    .fillMaxHeight()
                                    .background(Color.White, RoundedCornerShape(8.dp))
                                    .border(1.dp, darkGreenColor, RoundedCornerShape(8.dp))
                                    .padding(horizontal = 16.dp, vertical = 14.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "Misi Terselesaikan",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = darkGreenColor,
                                    textAlign = TextAlign.Center
                                )
                                
                                Spacer(modifier = Modifier.height(12.dp))
                                
                                // Missions Completed Badge
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(lightGreenCard, RoundedCornerShape(4.dp))
                                        .border(1.dp, Color(0xFF88C9B9), RoundedCornerShape(4.dp))
                                        .padding(vertical = 6.dp),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Check",
                                        tint = Color(0xFF6BBFAB),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "$missionsCompleted Misi Selesai",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF6BBFAB)
                                    )
                                }
                            }
                            
                            Spacer(modifier = Modifier.width(16.dp))
                            
                            // Right Column: Rewards
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight(),
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                // XP Badge
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f)
                                        .background(Color(0xFFDFF1FD), RoundedCornerShape(4.dp))
                                        .border(1.dp, Color(0xFF8BB5ED), RoundedCornerShape(4.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "$totalXp XP",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color(0xFF5A9DDF)
                                    )
                                }
                                
                                Spacer(modifier = Modifier.height(12.dp))
                                
                                // Coin Badge
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f)
                                        .background(lightYellowCard, RoundedCornerShape(4.dp))
                                        .border(1.dp, Color(0xFFDCA855), RoundedCornerShape(4.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = formatCompactNumber(totalCoins) + " ",
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = Color(0xFF916A42)
                                        )
                                        Text("🪙", fontSize = 14.sp)
                                    }
                                }
                            }
                        }
                    }
                } // <--- Closes Surface

                // Mascot - overlaps the white card
                Image(
                    painter = painterResource(id = R.drawable.dinodaily),
                    contentDescription = "Mascot",
                    modifier = Modifier
                        .width(220.dp)
                        .height(200.dp)
                        .offset(x = 10.dp, y = 20.dp), // Adjust position to sit well on top of the card
                    contentScale = ContentScale.Fit
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Bottom Button (Inside Selesai tab block)
            Button(
                onClick = onStartMission, // Now navigates to Daily Mission
                colors = ButtonDefaults.buttonColors(
                    containerColor = darkGreenButton,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .height(56.dp)
            ) {
                Text(
                    text = "Ayo Selesaikan Misimu!",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            } // End of Selesai Tab block
        }
    }
}

// Helper function for coins
private fun formatCompactNumber(number: Int): String {
    if (number < 1000) return number.toString()
    val kValue = number / 1000.0
    val kString = if (kValue % 1 == 0.0) {
        String.format("%.0f", kValue) // Ex: 1 for 1000
    } else {
        String.format("%.1f", kValue).replace(',', '.') // Ex: 1.5 for 1500
    }
    // Using simple formatting logic to add dots format (e.g. 1.000) for standard indonesian locale
    val resultStr = number.toString()
    if(resultStr.length > 3) {
        return resultStr.substring(0, resultStr.length - 3) + "." + resultStr.substring(resultStr.length - 3)
    }
    return resultStr
}

@Preview
@Composable
fun MissionDetailScreenPreview() {
    MissionDetailScreen(
        onNavigateBack = {},
        onStartMission = {},
        onNavigateToMissionRincian = {}
    )
}
