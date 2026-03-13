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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import coil.compose.AsyncImage

enum class DetailStep {
    OVERVIEW,
    RINCIAN
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MissionDetailScreen(
    onNavigateBack: () -> Unit,
    userName: String = "Kevin Aditya Pratama",
    userLevel: Int = 3,
    missionsCompleted: Int = 38,
    totalXp: Int = 500,
    totalCoins: Int = 1000,
    avatarUrl: String = "",
    isMissionCompletedToday: Boolean = false,
    targetProgress: Int = 5,
    onStartMission: () -> Unit
) {
    var currentStep by remember { mutableStateOf(DetailStep.OVERVIEW) }
    
    // Calculate dynamic rewards based on RPC logic
    val dynamicTargetXp = (targetProgress * 10) + 50
    val dynamicTargetCoins = (targetProgress * 2) + 10
    
    // Custom Back Handler to intercept system back button
    BackHandler(enabled = currentStep == DetailStep.RINCIAN) {
        currentStep = DetailStep.OVERVIEW
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (currentStep == DetailStep.OVERVIEW) "DETAIL MISI" else "RINCIAN",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.Black,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            if (currentStep == DetailStep.RINCIAN) {
                                currentStep = DetailStep.OVERVIEW
                            } else {
                                onNavigateBack()
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            modifier = Modifier.size(32.dp),
                            tint = Color.Black
                        )
                    }
                },
                actions = {
                    Spacer(modifier = Modifier.width(48.dp))
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DesignTokens.Colors.CreamBackground
                )
            )
        },
        containerColor = DesignTokens.Colors.CreamBackground
    ) { paddingValues ->
        AnimatedContent(
            targetState = currentStep,
            transitionSpec = {
                if (targetState == DetailStep.RINCIAN) {
                    (slideInHorizontally(tween(300)) { it } + fadeIn(tween(300)))
                        .togetherWith(slideOutHorizontally(tween(300)) { -it } + fadeOut(tween(300)))
                } else {
                    (slideInHorizontally(tween(300)) { -it } + fadeIn(tween(300)))
                        .togetherWith(slideOutHorizontally(tween(300)) { it } + fadeOut(tween(300)))
                }
            },
            label = "DetailStepTransition",
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) { step ->
            when (step) {
                DetailStep.OVERVIEW -> {
                    MissionOverviewContent(
                        userName = userName,
                        userLevel = userLevel,
                        missionsCompleted = missionsCompleted,
                        totalXp = totalXp,
                        totalCoins = totalCoins,
                        avatarUrl = avatarUrl,
                        isMissionCompletedToday = isMissionCompletedToday,
                        onNavigateToRincian = { currentStep = DetailStep.RINCIAN },
                        onStartMission = onStartMission
                    )
                }
                DetailStep.RINCIAN -> {
                    MissionRincianContent(
                        targetXp = dynamicTargetXp,
                        targetCoins = dynamicTargetCoins,
                        onStartMission = onStartMission
                    )
                }
            }
        }
    }
}

// ============================================================================
// Overview Content
// ============================================================================

@Composable
private fun MissionOverviewContent(
    userName: String,
    userLevel: Int,
    missionsCompleted: Int,
    totalXp: Int,
    totalCoins: Int,
    avatarUrl: String,
    isMissionCompletedToday: Boolean,
    onNavigateToRincian: () -> Unit,
    onStartMission: () -> Unit
) {
    val darkGreenColor = Color(0xFF1B4F45)
    val tabSelesaiColor = Color(0xFF185A48)
    val darkGreenButton = Color(0xFF568F7B)
    val textColor = Color(0xFF1B4D43)
    val lightGreenCard = Color(0xFFE8F6F1)
    val lightYellowCard = Color(0xFFFFF7E6)
    val lightCyanBlue = Color(0xFFDFF1FD)
    val greenBubble = Color(0xFF1B4F45)
    val paleYellowCover = Color(0xFFF7EED3)

    var selectedTab by remember { mutableStateOf(0) } // 0 = Aktif, 1 = Selesai

    Column(
        modifier = Modifier
            .fillMaxSize()
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
            // Aktif Tab Content
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
                    // Hero Card
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(paleYellowCover)
                            .border(1.dp, darkGreenColor, RoundedCornerShape(12.dp))
                            .padding(vertical = 24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        DinoSpeechBubble(
                            question = "Jangan lupa mengerjakan misinya ya, sobat Gobi!",
                            bubbleColor = greenBubble
                        )
                    }
                    
                    // Active Mission Card
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
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            val today = java.time.LocalDate.now()
                            val dayNum = today.dayOfMonth
                            val monthName = today.month.getDisplayName(java.time.format.TextStyle.FULL, java.util.Locale.Builder().setLanguage("id").setRegion("ID").build())
                            
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
                        
                        Spacer(modifier = Modifier.width(12.dp))
                        Box(
                            modifier = Modifier
                                .width(1.dp)
                                .height(40.dp)
                                .background(darkGreenColor.copy(alpha = 0.5f))
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (isMissionCompletedToday) "Misi hari ini Selesai! Hebat!" else "Misi Gobi belum selesai, nih!",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = darkGreenColor
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            
                            Button(
                                onClick = onNavigateToRincian,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isMissionCompletedToday) Color.Gray else darkGreenButton,
                                    contentColor = Color.White
                                ),
                                shape = RoundedCornerShape(4.dp),
                                contentPadding = PaddingValues(0.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(26.dp)
                            ) {
                                Text(
                                    text = if (isMissionCompletedToday) "Selesai" else "Lihat rincian misi!",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Upcoming Missions (Next 4 Days)
                    val todayDate = java.time.LocalDate.now()
                    for (i in 1..4) {
                        val nextDate = todayDate.plusDays(i.toLong())
                        val isTomorrow = i == 1
                        
                        val dayLabel = if (isTomorrow) "Besok" else {
                            val dayName = nextDate.dayOfWeek.getDisplayName(java.time.format.TextStyle.FULL, java.util.Locale.Builder().setLanguage("id").setRegion("ID").build())
                            val monthName = nextDate.month.getDisplayName(java.time.format.TextStyle.SHORT, java.util.Locale.Builder().setLanguage("id").setRegion("ID").build())
                            "$dayName, ${nextDate.dayOfMonth} $monthName"
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .padding(bottom = 12.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFE8E8E8))
                                .border(1.dp, darkGreenColor.copy(alpha = 0.6f), RoundedCornerShape(8.dp))
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Locked",
                                tint = Color.Gray,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text(
                                    text = dayLabel,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.DarkGray
                                )
                                Text(
                                    text = "Segera hadir! Kembalilah besok.",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
        } else {
            // Selesai Tab Content
            Text(
                text = "GOBI RECAP",
                fontSize = 28.sp,
                fontWeight = FontWeight.Black,
                color = textColor,
                letterSpacing = 1.sp
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(top = 16.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White,
                    border = BorderStroke(1.dp, darkGreenColor),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 220.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(CircleShape)
                                    .border(2.dp, Color.LightGray, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                if (avatarUrl.isNotEmpty()) {
                                    AsyncImage(
                                        model = avatarUrl,
                                        contentDescription = "Avatar",
                                        modifier = Modifier.size(50.dp).clip(CircleShape),
                                        contentScale = ContentScale.Crop
                                    )
                                } else {
                                    Image(
                                        painter = painterResource(id = R.drawable.img_dino_default),
                                        contentDescription = "Avatar Default",
                                        modifier = Modifier.size(50.dp).clip(CircleShape),
                                        contentScale = ContentScale.Crop
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(16.dp))
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
                        
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            // Quests Completed
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
                            
                            // Rewards
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight(),
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f)
                                        .background(lightCyanBlue, RoundedCornerShape(4.dp))
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
                                        Image(
                                            painter = painterResource(id = R.drawable.ic_gold),
                                            contentDescription = "Coin",
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                Image(
                    painter = painterResource(id = R.drawable.img_dino_daily),
                    contentDescription = "Mascot",
                    modifier = Modifier
                        .width(220.dp)
                        .height(200.dp)
                        .offset(x = 10.dp, y = 20.dp),
                    contentScale = ContentScale.Fit
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Button(
                onClick = onStartMission,
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
        }
    }
}

// ============================================================================
// Rincian Content
// ============================================================================

@Composable
private fun MissionRincianContent(
    targetXp: Int,
    targetCoins: Int,
    onStartMission: () -> Unit
) {
    val darkGreenColor = Color(0xFF1B4F45)
    val darkGreenButton = Color(0xFF568F7B)
    val lightBlueBadge = Color(0xFFDFF1FD)
    val lightBlueOutline = Color(0xFF67B0E8)
    val lightYellowBadge = Color(0xFFFEF3DF)
    val lightYellowOutline = Color(0xFFDBA854)
    val goldCoinColor = Color(0xFFF1C40F)
    val backgroundColor = Color(0xFFFFFDF5) 

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Hero Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(12.dp))
                .border(1.dp, darkGreenColor, RoundedCornerShape(12.dp))
                .background(Color.White)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.Bottom
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_dino_mission_detail_2),
                    contentDescription = "Mascot Detail Misi",
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(top = 16.dp),
                    contentScale = ContentScale.Fit
                )
                
                Column(
                    modifier = Modifier
                        .weight(1.2f)
                        .fillMaxHeight()
                        .padding(top = 24.dp, end = 16.dp),
                    verticalArrangement = Arrangement.Top
                ) {
                    Text(
                        text = "TARGET MISI",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Kamu hari ini harus dapat hadiah imbalan:",
                        fontSize = 12.sp,
                        color = Color.DarkGray,
                        lineHeight = 16.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(36.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(lightBlueBadge)
                                .border(1.dp, lightBlueOutline, RoundedCornerShape(4.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "$targetXp XP",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = lightBlueOutline
                            )
                        }
                        
                        Row(
                            modifier = Modifier
                                .weight(1.2f)
                                .height(36.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(lightYellowBadge)
                                .border(1.dp, lightYellowOutline, RoundedCornerShape(4.dp)),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "$targetCoins",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF8C6420)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .background(goldCoinColor, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_gold),
                                    contentDescription = "Coin",
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Content Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .border(1.dp, darkGreenColor, RoundedCornerShape(12.dp))
                .background(Color.White)
        ) {
            val todayDayOfYear = java.time.LocalDate.now().dayOfYear
            val edukasiMisiList = listOf(
                Triple("Kumpulkan Sampah Di Sekitarmu!", "Sekali membuang sampah mampu menyelamatkan beruang kutub dari kepunahan, serta menjaga keseimbangan ekosistem bumi untuk masa depan.", "Pernah dengar? Sebaiknya bersihkan sampah dahulu sebelum kamu membuangnya, supaya tidak mencemari lingkungan di sekitarmu."),
                Triple("Pilahlah Sampah Dengan Bijak!", "Tindakan kecilmu memilah sampah organik dan anorganik dapat mengurangi gas metana di TPA yang merusak lapisan ozon kita.", "Siapkan dua tempat sampah berbeda di rumah. Biasakan mulai dari sekarang untuk membuang sesuai jenisnya!"),
                Triple("Daur Ulang Botol Plastik!", "Botol plastik butuh ratusan tahun untuk terurai. Dengan mendaur ulangnya, kamu menyelamatkan ikan-ikan di laut dari polusi mikroplastik.", "Jangan langsung dibuang! Kumpulkan botol plastik, bilas bersih, dan kreasikan menjadi pot tanaman atau berikan ke bank sampah."),
                Triple("Kurangi Penggunaan Kertas!", "Setiap lembar kertas yang kamu hemat berarti ada satu pohon yang tetap berdiri tegak untuk menghasilkan oksigen segar bagi kita semua.", "Gunakan buku tulismu sampai halaman terakhir. Jika ada kertas bekas, gunakan sisi baliknya untuk coret-coretan belajarmu!")
            )
            
            val selectedMisiIndex = todayDayOfYear % edukasiMisiList.size
            val (judulMisi, peranText, caraText) = edukasiMisiList[selectedMisiIndex]
            
            Column(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(darkGreenColor)
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = judulMisi,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(backgroundColor)
                            .border(1.dp, darkGreenColor, RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "PERANMU",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.Black
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = peranText,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black,
                        textAlign = TextAlign.Justify,
                        lineHeight = 20.sp
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(backgroundColor)
                            .border(1.dp, darkGreenColor, RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "CARANYA",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.Black
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = caraText,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black,
                        textAlign = TextAlign.Justify,
                        lineHeight = 20.sp
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Button(
            onClick = onStartMission,
            colors = ButtonDefaults.buttonColors(
                containerColor = darkGreenButton,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text(
                text = "Mulai Misi!",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
        
        Spacer(modifier = Modifier.height(32.dp))
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
        onStartMission = {}
    )
}
