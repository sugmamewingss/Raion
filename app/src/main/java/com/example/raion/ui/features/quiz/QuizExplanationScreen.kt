package com.example.raion.ui.features.quiz

import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class ExplanationItem(
    val id: Int,
    val question: String,
    val answer: String,
    val explanation: String
)

@Composable
fun QuizExplanationScreen(
    episodeId: String,
    viewModel: QuizExplanationViewModel = hiltViewModel(),
    onNavigateHome: () -> Unit,
    onRetryQuiz: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val darkGreenColor = Color(0xFF1D5C42)
    val lightGreenColor = Color(0xFFEAF5EA)

    Box(modifier = Modifier.fillMaxSize()) {
        com.example.raion.ui.features.auth.components.WaveBackground()
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp, bottom = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Pembahasan",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.Black
                    )
                    Text(
                        text = "Review Soal Kuis", // Optional: show episode ID context if needed
                        fontSize = 16.sp,
                        color = darkGreenColor
                    )
                }
            },
            bottomBar = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(16.dp)
                ) {
                    Button(
                        onClick = onNavigateHome,
                        colors = ButtonDefaults.buttonColors(containerColor = darkGreenColor),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                    ) {
                        Text(
                            text = "Kembali ke Beranda",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
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
                }
            }
        ) { paddingValues ->
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = com.example.raion.ui.theme.DesignTokens.Colors.TealPrimary)
                }
            } else if (!uiState.errorMessage.isNullOrEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = uiState.errorMessage ?: "Terjadi kesalahan", color = Color.Red)
                }
            } else if (uiState.questions.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Belum ada pembahasan untuk episode ini.", color = Color.Gray)
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    itemsIndexed(uiState.questions) { index, q ->
                        val correctAnswer = q.options.getOrNull(q.correctAnswerIndex) ?: ""
                        val item = ExplanationItem(
                            id = index + 1,
                            question = q.questionText,
                            answer = correctAnswer,
                            explanation = q.explanation ?: "Tidak ada pembahasan detail."
                        )
                        ExplanationCard(item = item, darkGreenColor = darkGreenColor, lightGreenColor = lightGreenColor)
                    }
                }
            }
        }
    }
}

@Composable
fun ExplanationCard(item: ExplanationItem, darkGreenColor: Color, lightGreenColor: Color) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, darkGreenColor),
        color = Color.White,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Number Circle and Question text
            Row(
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(lightGreenColor, CircleShape)
                        .padding(4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = item.id.toString(),
                        fontWeight = FontWeight.ExtraBold,
                        color = darkGreenColor,
                        fontSize = 14.sp
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Soal",
                        fontWeight = FontWeight.Bold,
                        color = darkGreenColor,
                        fontSize = 14.sp
                    )
                    Text(
                        text = item.question,
                        fontSize = 14.sp,
                        color = Color.Black,
                        modifier = Modifier.padding(top = 4.dp, bottom = 12.dp),
                        lineHeight = 20.sp
                    )
                }
            }

            // Answer Segment
            Text(
                text = "Jawaban",
                fontWeight = FontWeight.Bold,
                color = darkGreenColor,
                fontSize = 14.sp,
                modifier = Modifier.padding(start = 44.dp)
            )
            Text(
                text = item.answer,
                fontSize = 14.sp,
                color = darkGreenColor, // Correct answer in slightly bold color 
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(start = 44.dp, top = 4.dp, bottom = 12.dp)
            )

            // Explanation Segment
            Text(
                text = "Pembahasan",
                fontWeight = FontWeight.Bold,
                color = darkGreenColor,
                fontSize = 14.sp,
                modifier = Modifier.padding(start = 44.dp)
            )
            Text(
                text = item.explanation,
                fontSize = 14.sp,
                color = Color.Black,
                lineHeight = 20.sp,
                modifier = Modifier.padding(start = 44.dp, top = 4.dp)
            )
        }
    }
}
