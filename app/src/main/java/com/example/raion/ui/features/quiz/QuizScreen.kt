package com.example.raion.ui.features.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.raion.data.local.UserPreferences
import com.example.raion.data.model.UserProfile
import com.example.raion.data.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.raion.data.model.quiz.QuizChapter
import com.example.raion.data.model.quiz.UserChapterProgress
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.lifecycle.SavedStateHandle
import com.example.raion.data.model.quiz.QuizEpisode
import com.example.raion.data.model.quiz.UserQuizProgress
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.style.TextAlign
import com.example.raion.data.model.quiz.QuizQuestionDto
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.runtime.saveable.rememberSaveable
import kotlinx.coroutines.delay
import com.example.raion.data.model.quiz.QuizResultResponse
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton


@Composable
fun QuizScreen(
    viewModel: QuizViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToEpisodes: (String) -> Unit = {}
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
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 24.dp)
            ) {
                // 0. Top Navigation
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onNavigateBack, modifier = Modifier.size(48.dp)) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali",
                            tint = Color.Black
                        )
                    }
                    Text(
                        text = "Tantangan Jenius",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.Black,
                        modifier = Modifier.weight(1f).offset(x = (-24).dp),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
                // 1. Top Profile Header
                QuizProfileHeader(
                    name = uiState.userProfile?.name ?: "User",
                    level = uiState.userProfile?.level ?: 1,
                    rank = uiState.userRank,
                    xp = uiState.userProfile?.totalXp ?: 0,
                    avatarUrl = uiState.userProfile?.currentAvatarUrl ?: ""
                )

                Spacer(modifier = Modifier.height(24.dp))

                // 2. Target Progress Card
                TargetProgressCard(
                    progress = uiState.currentTargetProgress,
                    maxTarget = uiState.currentTargetMax
                )

                Spacer(modifier = Modifier.height(24.dp))

                // 3. Challenge List Container
                ChallengeListContainer(
                    chapters = uiState.chapters,
                    chapterProgress = uiState.chapterProgress,
                    onNavigateToEpisodes = onNavigateToEpisodes
                )
            }
        }
    }
    }
}

@Composable
fun QuizProfileHeader(name: String, level: Int, rank: Int, xp: Int, avatarUrl: String = "") {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        // Avatar
        val fallbackUrl = "https://nnloirkwladlazxgpgrm.supabase.co/storage/v1/object/public/avatars/dino_default.png"
        val imageToLoad = if (avatarUrl.isNotEmpty()) avatarUrl else fallbackUrl
        
        coil.compose.SubcomposeAsyncImage(
            model = imageToLoad,
            contentDescription = "Avatar",
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .border(2.dp, Color(0xFFD4DAD4), CircleShape),
            contentScale = ContentScale.Crop,
            loading = {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color(0xFFF09D51),
                        strokeWidth = 2.dp
                    )
                }
            },
            error = {
                Image(
                    painter = painterResource(id = R.drawable.img_dino_default),
                    contentDescription = "Avatar fallback",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
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

            // XP Progress Bar Segment
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Determine current XP progress inside the current level
                val currentLevelXp = xp % 100
                val nextLevelMax = 100
                val xpRatio = currentLevelXp.toFloat() / nextLevelMax.toFloat()
                val xpText = "$currentLevelXp / $nextLevelMax XP"

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(10.dp)
                        .background(Color(0xFFE2E2E2), CircleShape)
                        .clip(CircleShape),
                    contentAlignment = Alignment.CenterStart
                ) {
                    // Outer bar shadow simulation
                    Box(modifier = Modifier.fillMaxSize().border(1.dp, Color(0xFFCCCCCC), CircleShape))
                    // Inner progress
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(xpRatio.coerceIn(0f, 1f))
                            .background(Color(0xFFF09D51), CircleShape)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = xpText,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.Black
                )
            }
        }
    }
}

@Composable
fun TargetProgressCard(progress: Int, maxTarget: Int) {
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
                    val safeMax = if (maxTarget > 0) maxTarget else 1
                    val progressRatio = (progress.toFloat() / safeMax).coerceIn(0f, 1f)
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
                        text = "$progress/$maxTarget Soal Benar",
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
fun ChallengeListContainer(
    chapters: List<com.example.raion.ui.features.quiz.QuizChapterUiModel>,
    chapterProgress: List<com.example.raion.data.model.quiz.UserChapterProgress>,
    onNavigateToEpisodes: (String) -> Unit = {}
) {
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

            if (chapters.isEmpty()) {
                Text("Belum ada babak kuis.", color = Color.Gray, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)
            } else {
                chapters.forEachIndexed { index, chapterModel ->
                    val chapter = chapterModel.chapter
                    
                    // A chapter is unlocked if it's the first chapter (number 1)
                    // OR if the PREVIOUS chapter is completed.
                    val isUnlocked = if (chapter.chapterNumber == 1) {
                        true
                    } else {
                        // Find the previous chapter
                        val prevChapter = chapters.find { it.chapter.chapterNumber == chapter.chapterNumber - 1 }?.chapter
                        // Check if previous chapter is in progress list and isCompleted == true
                        val prevProgress = chapterProgress.find { it.chapterId == prevChapter?.id }
                        prevProgress?.isCompleted == true
                    }
                    
                    // Style logic (alternating colors like the design)
                    val isOdd = index % 2 == 0
                    val bgColor = if (isOdd) Color(0xFFFFF2CD) else Color(0xFFFFE0E8)
                    val borderColor = if (isOdd) Color(0xFFDAB46C) else Color(0xFFDFA1A1)

                    ChallengeCard(
                        title = "Bab ${chapter.chapterNumber}",
                        subtitle = chapter.title,
                        episodes = chapterModel.totalEpisodes,
                        accumulativeXp = chapterModel.accumulativeXp,
                        accumulativeCoins = chapterModel.accumulativeCoins,
                        bgColor = bgColor,
                        borderColor = borderColor,
                        isLocked = !isUnlocked,
                        onClick = {
                            if (isUnlocked) {
                                onNavigateToEpisodes(chapter.id)
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
                
                // Keep some locked placeholders for visual padding if we have few chapters
                if (chapters.size < 3) {
                    repeat(3 - chapters.size) {
                        LockedPlaceholderCard()
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun ChallengeCard(
    title: String,
    subtitle: String,
    episodes: Int,
    accumulativeXp: Int = 0,
    accumulativeCoins: Int = 0,
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
            .clickable(enabled = !isLocked, onClick = onClick) // Clickable entire surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Book Icon
            Image(
                painter = painterResource(id = R.drawable.ic_book), // Assuming book.png is available in drawable
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
                    Badge(text = "+$accumulativeXp XP", bgColor = Color(0xFFD9F1FF), textColor = Color(0xFF2C84C7))
                    Spacer(modifier = Modifier.width(6.dp))
                    Badge(text = "+$accumulativeCoins", bgColor = Color(0xFFFFECB3), textColor = Color(0xFFD69400), isCoin = true)
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

