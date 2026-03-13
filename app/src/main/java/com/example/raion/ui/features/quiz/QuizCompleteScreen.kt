package com.example.raion.ui.features.quiz

import androidx.compose.foundation.background
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.Image
import com.example.raion.R
import com.example.raion.ui.theme.DesignTokens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizCompleteScreen(
    episodeId: String,
    correctAnswersCount: Int,
    viewModel: QuizCompleteViewModel = hiltViewModel(),
    onNavigateToExplanation: () -> Unit,
    onRetryQuiz: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val darkGreenColor = Color(0xFF1D5C42)

    Box(modifier = Modifier.fillMaxSize()) {
        com.example.raion.ui.features.auth.components.WaveBackground()
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                // Empty top bar area used for spacing to match design
                Spacer(modifier = Modifier.windowInsetsTopHeight(WindowInsets.statusBars))
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
            } else if (uiState.result != null) {
                val totalQuestions = uiState.totalQuestions
                val wrongAnswersCount = totalQuestions - correctAnswersCount
                val xpEarned = uiState.result!!.gainedXp
                val coinsEarned = uiState.result!!.gainedCoins
                val isPassed = (correctAnswersCount.toFloat() / totalQuestions.toFloat()) >= 0.7f

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(40.dp))

                    // Title
                    Text(
                        text = "Tantangan Jenius Selesai!",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Mascot Image
                    Image(
                        painter = painterResource(id = R.drawable.img_dino_quiz_complete),
                        contentDescription = "Quiz Complete Mascot",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp),
                        contentScale = ContentScale.Fit
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // Score Dashboard
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Top Row: Benar / Salah
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Benar Card
                            Surface(
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFFE4F4E4), // Light Green bg
                                border = BorderStroke(1.dp, Color(0xFF7CC47C)) // Green border
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center,
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_y),
                                        contentDescription = "Benar",
                                        tint = Color(0xFF4CA64C),
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "$correctAnswersCount soal benar",
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF4CA64C) // Green text
                                    )
                                }
                            }

                            // Salah Card
                            Surface(
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFFF9E4E4), // Light Red bg
                                border = BorderStroke(1.dp, Color(0xFFD67777)) // Red border
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center,
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_x),
                                        contentDescription = "Salah",
                                        tint = Color(0xFFC75151),
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "$wrongAnswersCount soal salah",
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFC75151) // Red text
                                    )
                                }
                            }
                        }

                        // Bottom Row: XP, Coins, Status
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // XP Card
                            Surface(
                                modifier = Modifier.weight(0.8f),
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFFE2F0F9), // Light Blue bg
                                border = BorderStroke(1.dp, Color(0xFF7AAED6)) // Blue border
                            ) {
                                Text(
                                    text = "+$xpEarned XP",
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF4A90E2), // Blue text
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(vertical = 12.dp)
                                )
                            }

                            // Coins Card
                            Surface(
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFFFDF6DE), // Light yellow/orange bg
                                border = BorderStroke(1.dp, Color(0xFFDAB76F)) // Gold border
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(vertical = 10.dp)
                                ) {
                                    Text(
                                        text = "+$coinsEarned ",
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFB8860B), // Dark Gold text
                                    )
                                    // Coin Icon representation
                                    Image(
                                        painter = painterResource(id = R.drawable.ic_gold),
                                        contentDescription = "Coin",
                                        modifier = Modifier.size(18.dp),
                                        contentScale = ContentScale.Fit
                                    )
                                }
                            }

                            // Status Lulus/Gagal
                            Surface(
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(8.dp),
                                color = if (isPassed) Color(0xFFFCEAED) else Color(0xFFF5F5F5),
                                border = BorderStroke(1.dp, if (isPassed) Color(0xFFD28258) else Color.Gray) 
                            ) {
                                Text(
                                    text = if (isPassed) "LULUS" else "GAGAL",
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (isPassed) Color(0xFFD28258) else Color.DarkGray,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(vertical = 12.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    // Action Buttons
                    Button(
                        onClick = onNavigateToExplanation,
                        colors = ButtonDefaults.buttonColors(containerColor = darkGreenColor),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                    ) {
                        Text(
                            text = "Lihat Pembahasan",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedButton(
                        onClick = onRetryQuiz,
                        border = BorderStroke(1.dp, darkGreenColor),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = darkGreenColor),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                    ) {
                        Text(
                            text = "Kerjakan Ulang",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))
                }
            } else {
                Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                    Text(uiState.errorMessage ?: "Terjadi kesalahan", color = Color.Red)
                }
            }
        }
    }
}
