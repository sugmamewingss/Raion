package com.example.raion.ui.features.diary

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import com.example.raion.R
import com.example.raion.ui.theme.DesignTokens
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun DiaryScreen(
    streak: Int,
    isMissionCompletedToday: Boolean,
    userName: String,
    userLevel: Int,
    missionsCompleted: Int,
    totalXp: Int,
    totalCoins: Int,
    onNavigateBack: () -> Unit
) {
    val idLocale = Locale("id", "ID")
    
    // State for the currently displayed month in the calendar
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    
    // Popup state
    var showPopup by remember { mutableStateOf(false) }

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
                    imageVector = Icons.Default.ArrowBack,
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
                .padding(horizontal = 24.dp),
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
                    painter = painterResource(id = R.drawable.fire_streak),
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
                                            
                                            // The most recent date in the streak set gets the fire icon
                                            val maxStreakDate = streakDates.maxOrNull()
                                            val isLatestStreakDay = isStreakDay && maxStreakDate != null && cellDate.isEqual(maxStreakDate)
                                            
                                            // Handle highlighting
                                            if (isStreakDay) {
                                                if (isLatestStreakDay) {
                                                    // Latest streak day: show fire, clickable
                                                    Image(
                                                        painter = painterResource(id = R.drawable.fire_streak),
                                                        contentDescription = "Streak Today",
                                                        modifier = Modifier
                                                            .size(28.dp)
                                                            .clickable { showPopup = true },
                                                        colorFilter = ColorFilter.tint(DesignTokens.Colors.OrangePrimary)
                                                    )
                                                } else {
                                                    // Past streak day: circle #FCD38D 
                                                    Box(
                                                        modifier = Modifier
                                                            .size(28.dp)
                                                            .background(Color(0xFFFCD38D), CircleShape)
                                                            .clickable { showPopup = true }, // Fallback clickable
                                                        contentAlignment = Alignment.Center
                                                    ) {
                                                        Text(
                                                            text = dayNumber.toString(),
                                                            fontSize = 14.sp,
                                                            color = Color.Black
                                                        )
                                                    }
                                                }
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
            visible = showPopup,
            enter = androidx.compose.animation.slideInVertically(
                initialOffsetY = { it },
                animationSpec = androidx.compose.animation.core.tween(durationMillis = 300)
            ) + androidx.compose.animation.fadeIn(),
            exit = androidx.compose.animation.slideOutVertically(
                targetOffsetY = { it },
                animationSpec = androidx.compose.animation.core.tween(durationMillis = 300)
            ) + androidx.compose.animation.fadeOut(),
            modifier = Modifier.fillMaxSize()
        ) {
            DailyRecapPopup(
                isVisible = showPopup,
                userName = userName,
                userLevel = userLevel,
                missionsCompleted = missionsCompleted,
                storiesRead = 0, // Placeholder
                totalXp = totalXp,
                totalCoins = totalCoins,
                onClose = { showPopup = false }
            )
        }
    }
}

@Preview
@Composable
fun DiaryScreenPreview() {
    DiaryScreen(
        streak = 7,
        isMissionCompletedToday = true,
        userName = "Kevin Aditya Pratama",
        userLevel = 3,
        missionsCompleted = 38,
        totalXp = 1500,
        totalCoins = 250,
        onNavigateBack = {}
    )
}
