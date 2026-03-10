package com.example.raion.ui.features.quiz

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.raion.R
import com.example.raion.ui.theme.DesignTokens

@Composable
fun QuizScreen(
    viewModel: QuizViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToEpisodes: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    // Mimic the cream background from the design
    val creamBgColor = Color(0xFFFCFDF2) // Slightly darker than white, a warm cream

    Box(modifier = Modifier.fillMaxSize()) {
        com.example.raion.ui.features.auth.components.WaveBackground()
        Scaffold(
            containerColor = Color.Transparent
        ) { paddingValues ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = DesignTokens.Colors.TealPrimary)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 20.dp, vertical = 24.dp)
            ) {
                Spacer(modifier = Modifier.height(24.dp))
                // 1. Top Profile Header
                QuizProfileHeader(
                    name = uiState.userProfile?.name ?: "User",
                    level = uiState.userProfile?.level ?: 1,
                    rank = uiState.userRank,
                    xp = uiState.userProfile?.totalXp ?: 0
                )

                Spacer(modifier = Modifier.height(24.dp))

                // 2. Target Progress Card
                TargetProgressCard(soalBenar = uiState.quizProgress)

                Spacer(modifier = Modifier.height(24.dp))

                // 3. Challenge List Container
                ChallengeListContainer(onNavigateToEpisodes = onNavigateToEpisodes)
            }
        }
    }
    }
}

@Composable
fun QuizProfileHeader(name: String, level: Int, rank: Int, xp: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        // Avatar
        Image(
            painter = painterResource(id = R.drawable.dinoprofile),
            contentDescription = "Avatar",
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .border(2.dp, Color(0xFFD4DAD4), CircleShape),
            contentScale = ContentScale.Crop
        )
        
        Spacer(modifier = Modifier.width(16.dp))

        // Info Column
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text(
                        text = name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "$level - Sekolah Dasar",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF555555)
                    )
                }

                // Rank Badge
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, Color(0xFF549E83)),
                    color = Color.White
                ) {
                    Text(
                        text = "Peringkat $rank",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF549E83),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // XP Progress Bar
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                val xpProgress = (xp % 100) / 100f
                LinearProgressIndicator(
                    progress = { xpProgress },
                    modifier = Modifier
                        .weight(1f)
                        .height(10.dp)
                        .clip(CircleShape),
                    color = Color(0xFFF09D51),
                    trackColor = Color(0xFFE8E8E8),
                    drawStopIndicator = {}
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "$xp/100 XP",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
        }
    }
}

@Composable
fun TargetProgressCard(soalBenar: Int) {
    val cardColor = Color(0xFF1D5C42) // Dark green from design
    
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = cardColor,
        border = BorderStroke(1.dp, cardColor)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Top Green Area
            Text(
                text = "Ayo selesaikan target tantangan jenius! \uD83D\uDD25",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(16.dp)
            )

            // Bottom White Area
            Surface(
                color = Color.White,
                shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val progressRatio = soalBenar / 10f
                    LinearProgressIndicator(
                        progress = { progressRatio },
                        modifier = Modifier
                            .weight(1f)
                            .height(8.dp)
                            .clip(CircleShape),
                        color = Color(0xFFF09D51),
                        trackColor = Color(0xFFEEEEEE),
                        drawStopIndicator = {}
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "$soalBenar/10 Soal Benar",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }
        }
    }
}

@Composable
fun ChallengeListContainer(onNavigateToEpisodes: () -> Unit = {}) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, Color(0xFF1D5C42)), // Green border
        color = Color.White,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = "Mau tantangan apa hari ini?",
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.Black
            )
            
            Spacer(modifier = Modifier.height(20.dp))

            // Bab 1 Card (Unlocked)
            ChallengeCard(
                title = "Bab 1",
                subtitle = "Buang sampah sembarangan",
                episodes = 2,
                bgColor = Color(0xFFFFF2CD), // Light Yellow
                borderColor = Color(0xFFDAB46C), // Orange-yellowish border
                isLocked = false,
                onClick = onNavigateToEpisodes
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Bab 2 Card (Locked but visible details)
            ChallengeCard(
                title = "Bab 2",
                subtitle = "Raja Daur Ulang?",
                episodes = 1,
                bgColor = Color(0xFFFFE0E8), // Light Pink
                borderColor = Color(0xFFDFA1A1), // Pink border
                isLocked = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Locked Placeholders
            LockedPlaceholderCard()
            Spacer(modifier = Modifier.height(16.dp))
            LockedPlaceholderCard()
        }
    }
}

@Composable
fun ChallengeCard(
    title: String,
    subtitle: String,
    episodes: Int,
    bgColor: Color,
    borderColor: Color,
    isLocked: Boolean,
    onClick: () -> Unit = {}
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, borderColor),
        color = bgColor,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Book Icon
            Image(
                painter = painterResource(id = R.drawable.book), // Assuming book.png is available in drawable
                contentDescription = "Book Icon",
                modifier = Modifier.size(56.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Details Column
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.DarkGray
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Badges Row
                Row {
                    Badge(text = "+50 XP", bgColor = Color(0xFFD9F1FF), textColor = Color(0xFF2C84C7))
                    Spacer(modifier = Modifier.width(6.dp))
                    Badge(text = "+150", bgColor = Color(0xFFFFECB3), textColor = Color(0xFFD69400), isCoin = true)
                    Spacer(modifier = Modifier.width(6.dp))
                    Badge(text = "$episodes Episode", bgColor = Color(0xFFDDF5E6), textColor = Color(0xFF388E3C))
                }
            }

            // Action Button (Right Arrow or Lock)
            Surface(
                shape = CircleShape,
                border = BorderStroke(1.dp, if (isLocked) Color(0xFFC97C7C) else Color(0xFFC0A261)),
                color = Color.White,
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .clickable(enabled = !isLocked, onClick = onClick)
            ) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    if (isLocked) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Locked",
                            tint = Color.Black,
                            modifier = Modifier.size(18.dp)
                        )
                    } else {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Enter",
                            tint = Color.Black,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Badge(text: String, bgColor: Color, textColor: Color, isCoin: Boolean = false) {
    Surface(
        shape = RoundedCornerShape(4.dp),
        border = BorderStroke(1.dp, textColor.copy(alpha = 0.5f)),
        color = bgColor
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
            Text(
                text = text,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
            if (isCoin) {
                Spacer(modifier = Modifier.width(2.dp))
                Text(text = "🪙", fontSize = 10.sp)
            }
        }
    }
}

@Composable
fun LockedPlaceholderCard() {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFFD6D6D6), // Light structural grey
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = "Locked Placeholder",
                tint = Color.White,
                modifier = Modifier.size(36.dp)
            )
        }
    }
}
