package com.example.raion.ui.features.quiz

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.raion.R
import com.example.raion.ui.theme.DesignTokens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizEpisodeScreen(
    viewModel: QuizEpisodeViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onChangeBabClick: () -> Unit = onNavigateBack,
    onNavigateToPrep: (String) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        com.example.raion.ui.features.auth.components.WaveBackground()
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                QuizTopNavBar(
                    title = "Pilih Episode",
                    onBackClick = onNavigateBack
                )
            }
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
                    uiState.chapter?.let { chapter ->
                        // Selected Chapter Header Box
                        SelectedChapterCard(
                            chapter = chapter,
                            episodesCount = uiState.episodes.size,
                            onChangeBabClick = onChangeBabClick
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Episodes List Container
                    EpisodeListContainer(
                        episodes = uiState.episodes,
                        progressList = uiState.episodeProgress,
                        onNavigateToPrep = onNavigateToPrep
                    )
                }
            }
        }
    }
}

@Composable
fun SelectedChapterCard(
    chapter: com.example.raion.data.model.quiz.QuizChapter,
    episodesCount: Int,
    onChangeBabClick: () -> Unit
) {
    val cardColor = Color(0xFF1D5C42) // Dark green from design
    
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = cardColor,
        border = BorderStroke(1.dp, cardColor)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Top Green Area
            Text(
                text = "Bab yang kamu pilih, nih!",
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
                // We use our new GenericListCard inner anatomy but customized for "SelectedChapter"
                GenericListCard(
                    title = "Bab ${chapter.chapterNumber}",
                    subtitle = chapter.title,
                    iconRes = R.drawable.ic_book,
                    bonusXp = chapter.bonusXp,
                    bonusCoins = chapter.bonusCoins,
                    bgColor = Color.White,
                    borderColor = Color.Transparent,
                    isLocked = false,
                    actionButton = {
                        // "Ubah" Button
                        Surface(
                            shape = RoundedCornerShape(50),
                            border = BorderStroke(1.dp, Color(0xFF1D5C42)), // Green border
                            color = Color.White,
                            modifier = Modifier
                                .clickable(onClick = onChangeBabClick)
                        ) {
                            Text(
                                text = "Ubah",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun EpisodeListContainer(
    episodes: List<com.example.raion.data.model.quiz.QuizEpisode>,
    progressList: List<com.example.raion.data.model.quiz.UserQuizProgress>,
    onNavigateToPrep: (String) -> Unit = {}
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
                text = "Mau pilih episode yang mana?",
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.Black
            )
            
            Spacer(modifier = Modifier.height(20.dp))

            if (episodes.isEmpty()) {
                Text("Belum ada episode di babak ini.", color = Color.Gray, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)
            } else {
                episodes.forEachIndexed { index, episode ->
                    // Logic for unlocking: Episode #1 is always unlocked.
                    // Consecutive episodes unlocked if the PREVIOUS one was passed (isPassed == true)
                    val isUnlocked = if (episode.episodeNumber == 1) {
                        true
                    } else {
                        val prevEpisode = episodes.find { it.episodeNumber == episode.episodeNumber - 1 }
                        val prevProgress = progressList.find { it.episodeId == prevEpisode?.id }
                        prevProgress?.isPassed == true
                    }
                    
                    val isOdd = index % 2 == 0
                    val bgColor = if (isOdd) Color(0xFFFFF2CD) else Color(0xFFFFE0E8)
                    val borderColor = if (isOdd) Color(0xFFDAB46C) else Color(0xFFDFA1A1)

                    GenericListCard(
                        title = "Episode ${episode.episodeNumber}",
                        subtitle = episode.title,
                        iconRes = R.drawable.ic_paper,
                        bonusXp = episode.rewardXp,
                        bonusCoins = episode.rewardCoins,
                        bgColor = bgColor,
                        borderColor = borderColor,
                        isLocked = !isUnlocked,
                        onClick = { onNavigateToPrep(episode.id) }
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                }
                
                // Keep some locked placeholders for visual padding if we have few episodes
                if (episodes.size < 3) {
                    repeat(3 - episodes.size) {
                        LockedPlaceholderCard()
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}
