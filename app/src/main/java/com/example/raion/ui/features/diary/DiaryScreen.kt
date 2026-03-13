package com.example.raion.ui.features.diary

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.raion.R
import com.example.raion.ui.theme.DesignTokens
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

import coil.compose.AsyncImage
@Composable
fun DiaryScreen(
    viewModel: DiaryViewModel = hiltViewModel(),
    streak: Int,
    isMissionCompletedToday: Boolean,
    userName: String,
    userLevel: Int,
    missionsCompleted: Int, // Currently unused in popup since we use day-specific
    quizzesCompleted: Int,  // "
    totalXp: Int,           // "
    totalCoins: Int,        // "
    avatarUrl: String,      // Menambahkan URL Avatar User
    onNavigateToQuiz: () -> Unit = {},
    onNavigateToStory: () -> Unit = {},
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    
    val idLocale = Locale.Builder().setLanguage("id").setRegion("ID").build()
    
    // State for the currently displayed month in the calendar
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    
    // Popup state
    var showPopup by remember { mutableStateOf(false) }
    var showActionPopup by remember { mutableStateOf(false) }

    // When selectedDateHistory becomes available (not null), show the popup automatically
    LaunchedEffect(uiState.selectedDateHistory) {
        if (uiState.selectedDateHistory != null) {
            showPopup = true
        }
    }

    // Pre-calculate which Dates are active in the streak
    val latestStreakDate = remember(isMissionCompletedToday) {
        val today = LocalDate.now()
        if (isMissionCompletedToday) today else today.minusDays(1)
    }

    val streakDates = remember(streak, latestStreakDate) {
        val dates = mutableSetOf<LocalDate>()
        if (streak > 0) {
            for (i in 0 until streak) {
                dates.add(latestStreakDate.minusDays(i.toLong()))
            }
        }
        dates
    }

    Scaffold(
        containerColor = Color(0xFFFFF9E6), // Light yellow tint background like the design
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier
                        .size(32.dp)
                        .clickable { onNavigateBack() },
                    tint = Color.Black
                )
                
                Spacer(modifier = Modifier.weight(1f))
                
                Text(
                    text = "Buku Harian",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
                
                Spacer(modifier = Modifier.weight(1f))
                
                // Invisible spacer for centering
                Spacer(modifier = Modifier.size(32.dp))
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            
            Spacer(modifier = Modifier.height(40.dp))
            
            // --- TOP HERO SECTION ---
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                // Number with outline style (as shown in design)
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = streak.toString(),
                        fontSize = 100.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFFFCD38D).copy(alpha = 0.5f), // Inner color
                        style = androidx.compose.ui.text.TextStyle(
                            drawStyle = androidx.compose.ui.graphics.drawscope.Stroke(
                                miter = 10f,
                                width = 15f,
                                join = androidx.compose.ui.graphics.StrokeJoin.Round
                            )
                        )
                    )
                    Text(
                        text = streak.toString(),
                        fontSize = 100.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                }
                
                Spacer(modifier = Modifier.width(8.dp))
                
                Image(
                    painter = painterResource(id = R.drawable.ic_fire_streak),
                    contentDescription = "Fire Streak",
                    modifier = Modifier.size(80.dp),
                    colorFilter = ColorFilter.tint(DesignTokens.Colors.OrangePrimary)
                )
            }
            
            Text(
                text = "hari Streak!",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFCD38D)
            )
            
            Spacer(modifier = Modifier.height(60.dp))
            
            // --- CALENDAR SECTION ---
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Kalender Streak",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, Color.DarkGray),
                    color = Color(0xFFFCFBF6), // Slightly off-white
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        // Month Header
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.ChevronLeft,
                                contentDescription = "Previous Month",
                                modifier = Modifier
                                    .clickable { currentMonth = currentMonth.minusMonths(1) }
                                    .padding(8.dp),
                                tint = Color.DarkGray
                            )
                            
                            val monthName = currentMonth.month.getDisplayName(TextStyle.FULL, idLocale)
                            Text(
                                text = "$monthName ${currentMonth.year}",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                            
                            Icon(
                                imageVector = Icons.Default.ChevronRight,
                                contentDescription = "Next Month",
                                modifier = Modifier
                                    .clickable { currentMonth = currentMonth.plusMonths(1) }
                                    .padding(8.dp),
                                tint = Color.DarkGray
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Days Header
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            val daysOfWeek = listOf("Mn", "Sn", "Sl", "Rb", "Km", "Jm", "Sb")
                            for (day in daysOfWeek) {
                                Text(
                                    text = day,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.DarkGray,
                                    modifier = Modifier.weight(1f),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Days Grid
                        val daysInMonth = currentMonth.lengthOfMonth()
                        val firstDateOfMonth = currentMonth.atDay(1)
                        // In Java Time, Monday = 1, Sunday = 7
                        // Our layout: Mn(Sun)=1, Sn(Mon)=2, Sl(Tue)=3, Rb(Wed)=4, Km(Thu)=5, Jm(Fri)=6, Sb(Sat)=7
                        // So first day offset: If Sunday (7), then index 0. If Monday (1), index 1.
                        val firstDayOfWeekInt = firstDateOfMonth.dayOfWeek.value
                        val emptyDaysBefore = if (firstDayOfWeekInt == 7) 0 else firstDayOfWeekInt
                        
                        val totalCells = daysInMonth + emptyDaysBefore
                        val rows = Math.ceil(totalCells / 7.0).toInt()
                        
                        val today = LocalDate.now()
                        
                        for (row in 0 until rows) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                for (col in 0..6) {
                                    val cellIndex = row * 7 + col
                                    val dayNumber = cellIndex - emptyDaysBefore + 1
                                    
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .aspectRatio(1f),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        if (dayNumber in 1..daysInMonth) {
                                            val cellDate = currentMonth.atDay(dayNumber)
                                            val isStreakDay = streakDates.contains(cellDate)
                                            val isToday = cellDate.isEqual(today)
                                            
                                            // The most recent date in the streak set gets the fire icon
                                            val maxStreakDate = streakDates.maxOrNull()
                                            val isLatestStreakDay = isStreakDay && maxStreakDate != null && cellDate.isEqual(maxStreakDate)
                                            
                                            // Handle highlighting
                                            if (isStreakDay) {
                                                if (isLatestStreakDay) {
                                                    // Latest streak day: show fire, clickable
                                                    Image(
                                                        painter = painterResource(id = R.drawable.ic_fire_streak),
                                                        contentDescription = "Streak Today",
                                                        modifier = Modifier
                                                            .size(28.dp)
                                                            .clickable { 
                                                                val dateStr = String.format(Locale.US, "%04d-%02d-%02d", currentMonth.year, currentMonth.monthValue, dayNumber)
                                                                viewModel.loadHistoryForDate(dateStr)
                                                            },
                                                        colorFilter = ColorFilter.tint(DesignTokens.Colors.OrangePrimary)
                                                    )
                                                } else {
                                                    // Past streak day: circle #FCD38D 
                                                    Box(
                                                        modifier = Modifier
                                                            .size(28.dp)
                                                            .background(Color(0xFFFCD38D), CircleShape)
                                                            .clickable { 
                                                                val dateStr = String.format(Locale.US, "%04d-%02d-%02d", currentMonth.year, currentMonth.monthValue, dayNumber)
                                                                viewModel.loadHistoryForDate(dateStr)
                                                            }, // Fallback clickable
                                                        contentAlignment = Alignment.Center
                                                    ) {
                                                        Text(
                                                            text = dayNumber.toString(),
                                                            fontSize = 14.sp,
                                                            color = Color.Black
                                                        )
                                                    }
                                                }
                                            } else if (isToday) {
                                                // Hari ini belum nyala streak-nya -> icon api abu-abu
                                                Image(
                                                    painter = painterResource(id = R.drawable.ic_fire_streak),
                                                    contentDescription = "No Streak Today",
                                                    modifier = Modifier
                                                        .size(28.dp)
                                                        .clickable { 
                                                            showActionPopup = true
                                                        },
                                                    colorFilter = ColorFilter.tint(Color.LightGray)
                                                )
                                            } else {
                                                // Normal day
                                                Text(
                                                    text = dayNumber.toString(),
                                                    fontSize = 14.sp,
                                                    color = Color.DarkGray
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        
        // --- POPUP OVERLAY ---
        androidx.compose.animation.AnimatedVisibility(
            visible = showPopup || uiState.isLoadingHistory,
            enter = androidx.compose.animation.fadeIn(animationSpec = androidx.compose.animation.core.tween(durationMillis = 300)),
            exit = androidx.compose.animation.fadeOut(animationSpec = androidx.compose.animation.core.tween(durationMillis = 300)),
            modifier = Modifier.fillMaxSize()
        ) {
            if (uiState.isLoadingHistory) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f))
                        .clickable(enabled = false, onClick = {}),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = DesignTokens.Colors.OrangePrimary)
                }
            } else if (uiState.selectedDateHistory != null) {
                DailyRecapPopup(
                    isVisible = showPopup,
                    userName = userName,
                    userLevel = userLevel,
                    missionsCompleted = uiState.selectedDateHistory!!.missionsCompleted,
                    quizzesCompleted = uiState.selectedDateHistory!!.quizzesCompleted,
                    modulesCompleted = uiState.selectedDateHistory!!.modulesCompleted,
                    totalXp = uiState.selectedDateHistory!!.xpGained,
                    totalCoins = uiState.selectedDateHistory!!.coinsGained,
                    avatarUrl = avatarUrl,
                    onClose = { 
                        showPopup = false
                        viewModel.clearSelectedHistory()
                    }
                )
            }
        }

        // --- ACTION POPUP OVERLAY ---
        androidx.compose.animation.AnimatedVisibility(
            visible = showActionPopup,
            enter = androidx.compose.animation.fadeIn(),
            exit = androidx.compose.animation.fadeOut()
        ) {
            StartActionPopup(
                onClose = { showActionPopup = false },
                onKuisClick = {
                    showActionPopup = false
                    onNavigateToQuiz()
                },
                onCeritaClick = {
                    showActionPopup = false
                    onNavigateToStory()
                }
            )
        }
    }
}

@Composable
private fun DailyRecapPopup(
    isVisible: Boolean,
    userName: String,
    userLevel: Int,
    missionsCompleted: Int,
    quizzesCompleted: Int,
    modulesCompleted: Int,
    totalXp: Int,
    totalCoins: Int,
    avatarUrl: String,
    onClose: () -> Unit
) {
    if (!isVisible) return

    val darkGreenColor = Color(0xFF33534A)
    val buttonColor = Color(0xFF6BBFAB)
    val lightGreenCard = Color(0xFFE8F6F1)
    val lightBlueCard = Color(0xFFE8F4FE)
    val lightYellowCard = Color(0xFFFFF7E6)

    // Overlay Box: Intercepts clicks to close when tapping outside
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .clickable(onClick = onClose),
        contentAlignment = Alignment.Center
    ) {
        // Prevent clicks on the card from bubbling up to close the popup
        Box(
            modifier = Modifier
                .clickable(enabled = false, onClick = {}) 
                .padding(horizontal = 24.dp)
        ) {
            
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Container for Card and Mascot
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.TopCenter
                ) {
                    // Main White Card (drawn first so mascot sits on top)
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White,
                        border = androidx.compose.foundation.BorderStroke(1.dp, darkGreenColor),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 110.dp) // Space for mascot overlap
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp)
                        ) {
                            Spacer(modifier = Modifier.height(30.dp)) // Buffer under mascot hands
                        
                        // 1. User Info Header
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // Avatar
                            Box(
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(CircleShape)
                                    .border(2.dp, Color.LightGray, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                if (avatarUrl.isNotEmpty()) {
                                    AsyncImage(
                                        model = avatarUrl,
                                        contentDescription = "Avatar",
                                        modifier = Modifier.size(46.dp).clip(CircleShape),
                                        contentScale = ContentScale.Crop
                                    )
                                } else {
                                    Image(
                                        painter = painterResource(id = R.drawable.img_dino_default),
                                        contentDescription = "Avatar",
                                        modifier = Modifier.size(46.dp).clip(CircleShape),
                                        contentScale = ContentScale.Crop
                                    )
                                }
                            }
                            
                            Spacer(modifier = Modifier.width(16.dp))
                            
                            // Name & Level
                            Column {
                                Text(
                                    text = userName,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.Black
                                )
                                Text(
                                    text = "$userLevel - Sekolah Dasar",
                                    fontSize = 14.sp,
                                    color = Color.DarkGray
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        // 2. Main Stats Area (Green Outline Box)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(140.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            
                            // Left Column: Quests Completed (Inside Outline)
                            Column(
                                modifier = Modifier
                                    .weight(1.5f)
                                    .fillMaxHeight()
                                    .background(Color.White, RoundedCornerShape(8.dp))
                                    .border(1.dp, darkGreenColor, RoundedCornerShape(8.dp))
                                    .padding(horizontal = 16.dp, vertical = 12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Aktivitas Hari Ini",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = darkGreenColor,
                                    textAlign = TextAlign.Center
                                )
                                
                                Spacer(modifier = Modifier.weight(1f))
                                
                                // Missions & Quiz Completed Badge (Combined, Green)
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(lightGreenCard, RoundedCornerShape(4.dp))
                                        .border(1.dp, Color(0xFF88C9B9), RoundedCornerShape(4.dp))
                                        .padding(vertical = 8.dp),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Check",
                                        tint = buttonColor,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "${missionsCompleted + quizzesCompleted} Misi Selesai",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = buttonColor
                                    )
                                }
                                
                                Spacer(modifier = Modifier.height(8.dp))
                                
                                // Story/Module Completed Badge (Blue)
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(lightBlueCard, RoundedCornerShape(4.dp))
                                        .border(1.dp, Color(0xFF8BB5ED), RoundedCornerShape(4.dp))
                                        .padding(vertical = 8.dp),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Check",
                                        tint = Color(0xFF3F82DB),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "$modulesCompleted Cerita Selesai",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF3F82DB)
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
                                        .background(Color(0xFFDFF1FD), RoundedCornerShape(6.dp))
                                        .border(1.dp, Color(0xFF8BB5ED), RoundedCornerShape(6.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "$totalXp XP",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color(0xFF5A9DDF)
                                    )
                                }
                                
                                Spacer(modifier = Modifier.height(16.dp))
                                
                                // Coin Badge
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f)
                                        .background(lightYellowCard, RoundedCornerShape(6.dp))
                                        .border(1.dp, Color(0xFFDCA855), RoundedCornerShape(6.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "$totalCoins ",
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = Color(0xFF916A42)
                                        )
                                        Image(
                                            painter = painterResource(id = R.drawable.ic_gold),
                                            contentDescription = "Coin",
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                } // <--- Closes Surface
                    
                // Mascot (drawn exactly after Surface to be on top)
                Image(
                        painter = painterResource(id = R.drawable.img_dino_daily),
                        contentDescription = "Mascot",
                        modifier = Modifier
                            .width(220.dp)
                            .height(190.dp) // Adjust size so it sticks out of the 110.dp padding
                            .offset(x = 20.dp, y = (-80).dp), // Shift upwards by 5dp more as requested
                        contentScale = ContentScale.Fit
                    )
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                // 3. Okey Button
                Button(
                    onClick = onClose,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = buttonColor,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.size(width = 140.dp, height = 50.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        text = "Okey!",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun StartActionPopup(onClose: () -> Unit, onKuisClick: () -> Unit, onCeritaClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .clickable(onClick = onClose),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFFFFFDF5),
            modifier = Modifier
                .width(320.dp)
                .clickable(enabled = false, onClick = {}) 
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Mulai Kerjakan !",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(32.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = onKuisClick,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF104068)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                    ) {
                        Text("Kuis", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(
                        onClick = onCeritaClick,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF137351)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                    ) {
                        Text("Cerita", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
