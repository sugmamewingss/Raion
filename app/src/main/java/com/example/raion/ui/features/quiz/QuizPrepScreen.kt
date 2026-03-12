package com.example.raion.ui.features.quiz

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.raion.R
import com.example.raion.ui.theme.DesignTokens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizPrepScreen(
    episodeId: String,
    viewModel: QuizPrepViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onStartQuiz: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        com.example.raion.ui.features.auth.components.WaveBackground()
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                QuizTopNavBar(
                    title = "Persiapan",
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
                    // Mascot Talk Bubble Card
                    MascotBubbleCard()

                    Spacer(modifier = Modifier.height(24.dp))

                    // Details Container
                    PrepDetailsContainer(
                        chapter = uiState.chapter,
                        episode = uiState.episode,
                        questionsCount = uiState.questionsCount,
                        onStartQuiz = onStartQuiz
                    )
                }
            }
        }
    }
}

@Composable
fun PrepDetailsContainer(
    chapter: com.example.raion.data.model.quiz.QuizChapter?,
    episode: com.example.raion.data.model.quiz.QuizEpisode?,
    questionsCount: Int,
    onStartQuiz: () -> Unit
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
                text = "Rincian Tantangan Jenius",
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
                Column(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
                ) {
                    if (chapter != null && episode != null) {
                        // Bab Info
                        InfoBox(
                            title = "Bab ${chapter.chapterNumber}",
                            subtitle = chapter.title,
                            iconRes = R.drawable.ic_book,
                            badges = {
                                Row(modifier = Modifier.fillMaxWidth()) {
                                    Badge(text = "+${chapter.bonusXp} XP", bgColor = Color(0xFFD9F1FF), textColor = Color(0xFF2C84C7))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Badge(text = "+${chapter.bonusCoins}", bgColor = Color(0xFFFFECB3), textColor = Color(0xFFD69400), isCoin = true)
                                }
                            }
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Episode Info
                        InfoBox(
                            title = "Episode ${episode.episodeNumber}",
                            subtitle = episode.title,
                            iconRes = R.drawable.ic_paper,
                            badges = {
                                Row(modifier = Modifier.fillMaxWidth()) {
                                    Badge(text = "+${episode.rewardXp} XP", bgColor = Color(0xFFD9F1FF), textColor = Color(0xFF2C84C7))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Badge(text = "+${episode.rewardCoins}", bgColor = Color(0xFFFFECB3), textColor = Color(0xFFD69400), isCoin = true)
                                }
                            }
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    // Question Count Info
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, cardColor), // Dark Green Border 
                        color = Color.White,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(10.dp)
                        ) {
                            // Conceptual clock icon placeholder
                            Surface(
                                shape = CircleShape,
                                border = BorderStroke(1.5.dp, Color(0xFF5F7D93)), // Slate grey border for clock
                                color = Color.Transparent,
                                modifier = Modifier.size(32.dp)
                            ) {
                                // Simple line mimicking a clock hand
                                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                                    androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
                                        drawLine(
                                            color = Color(0xFF5F7D93),
                                            start = androidx.compose.ui.geometry.Offset(size.width / 2, size.height / 2),
                                            end = androidx.compose.ui.geometry.Offset(size.width / 2, size.height * 0.2f),
                                            strokeWidth = 3f,
                                            cap = androidx.compose.ui.graphics.StrokeCap.Round
                                        )
                                        drawLine(
                                            color = Color(0xFF5F7D93),
                                            start = androidx.compose.ui.geometry.Offset(size.width / 2, size.height / 2),
                                            end = androidx.compose.ui.geometry.Offset(size.width * 0.7f, size.height / 2),
                                            strokeWidth = 3f,
                                            cap = androidx.compose.ui.graphics.StrokeCap.Round
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Column {
                                Text(
                                    text = "Jumlah Soal",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.Black
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "$questionsCount Soal",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.DarkGray
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Start Button
                    Button(
                        onClick = onStartQuiz,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF549E83) // Soft Green Button Color
                        )
                    ) {
                        Text(
                            text = "Mulai Kerjakan!",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}
