package com.example.raion.ui.features.quiz

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.StrokeCap
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
fun QuizQuestionScreen(
    episodeId: String,
    viewModel: QuizQuestionViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onHalfwayBreak: () -> Unit = {},
    onQuizFinished: (Int) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val questions = uiState.questions
    val currentQuestion = questions.getOrNull(uiState.currentQuestionIndex)
    
    // Total expected questions for progress bar
    val totalQuestions = if (questions.isNotEmpty()) questions.size else 10

    // Effect triggers when quiz is marked finished in viewModel
    LaunchedEffect(uiState.isQuizFinished) {
        if (uiState.isQuizFinished) {
            onQuizFinished(uiState.correctAnswersCount)
        }
    }

    // Effect triggers when halfway break is reached
    LaunchedEffect(uiState.isHalfwayBreak) {
        if (uiState.isHalfwayBreak) {
            onHalfwayBreak()
            viewModel.dismissHalfwayBreak() // Reset state so it doesn't trigger again
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        com.example.raion.ui.features.auth.components.WaveBackground()
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                CenterAlignedTopAppBar(
                    title = { },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.Black
                            )
                        }
                    },
                    actions = {
                        // Show Timer overlay if popup is active, else show Progress bar
                        if (uiState.showPopup) {
                            CircularTimerIndicator(timeLeft = uiState.timeLeft)
                        } else {
                            // Custom Progress Bar (Capped at 1.0 for bonus questions)
                            val progressRatio = ((uiState.currentQuestionIndex + 1).toFloat() / totalQuestions.toFloat()).coerceAtMost(1.0f)
                            Box(
                                modifier = Modifier
                                    .padding(end = 20.dp)
                                    .height(12.dp)
                                    .fillMaxWidth(0.9f)
                                    .clip(CircleShape)
                                    .background(Color(0xFFE8E8E8)),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .fillMaxWidth(progressRatio)
                                        .clip(CircleShape)
                                        .background(Color(0xFFF09D51)) // Orange
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                if (uiState.isLoading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = DesignTokens.Colors.TealPrimary)
                    }
                } else if (questions.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Belum ada soal untuk episode ini.", color = Color.Gray)
                    }
                } else if (currentQuestion != null) {
                    // Main Content
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = 20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (uiState.currentQuestionIndex > 0 && uiState.currentQuestionIndex == questions.size - 1) {
                            Text(
                                text = "Soal Terakhir",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black,
                                modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
                            )
                        } else {
                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        // Question Bubble
                        QuestionBubble(text = currentQuestion.questionText)

                        // Mascot
                        Image(
                            painter = painterResource(id = R.drawable.img_dino_ask),
                            contentDescription = "Dino Asking",
                            modifier = Modifier
                                .height(260.dp)
                                .fillMaxWidth(),
                            contentScale = ContentScale.Fit
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Options
                        currentQuestion.options.forEachIndexed { index, optionText ->
                            val isSelected = uiState.selectedAnswerIndex == index
                            OptionCard(
                                text = optionText,
                                isSelected = isSelected,
                                onClick = {
                                    viewModel.onAnswerSelected(index)
                                }
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }

                    // Animated Popup Overlay at the bottom
                    AnimatedVisibility(
                        visible = uiState.showPopup,
                        enter = slideInVertically(
                            initialOffsetY = { it },
                            animationSpec = tween(durationMillis = 400) // Smooth slide up
                        ),
                        exit = slideOutVertically(
                            targetOffsetY = { it },
                            animationSpec = tween(durationMillis = 300)
                        ),
                        modifier = Modifier.align(Alignment.BottomCenter)
                    ) {
                        // Dimmed background + Popup content
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 7.dp) // 7dp offset from bottom
                        ) {
                            val popupImageRes = if (uiState.isAnswerCorrect) R.drawable.ic_correct else R.drawable.ic_wrong
                            Image(
                                painter = painterResource(id = popupImageRes),
                                contentDescription = if (uiState.isAnswerCorrect) "Jawaban Benar" else "Jawaban Salah",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight() // Adjust based on the actual asset's aspect ratio
                                    .clickable {
                                        viewModel.dismissPopupFast()
                                    },
                                contentScale = ContentScale.FillWidth
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun QuestionBubble(text: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        Column {
            // Main Bubble Body
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFF1D5C42), // Dark Green
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = text,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp),
                    lineHeight = 20.sp
                )
            }
            // Tail points down-right
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                // Draw a simple triangle using a custom shape or canvas
                androidx.compose.foundation.Canvas(
                    modifier = Modifier
                        .padding(end = 40.dp)
                        .width(28.dp)
                        .height(24.dp)
                ) {
                    val path = Path().apply {
                        moveTo(0f, 0f) // Top left of tail (connected to box)
                        lineTo(size.width, 0f) // Top right
                        lineTo(size.width * 0.5f, size.height) // Bottom point
                        close()
                    }
                    drawPath(path = path, color = Color(0xFF1D5C42))
                }
            }
        }
    }
}

@Composable
fun OptionCard(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    // The design uses a white background with a thin orange/brown border
    Surface(
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, if (isSelected) Color(0xFF1D5C42) else Color(0xFFC0A261)), // Highlight green if selected, otherwise brown
        color = Color.White,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(vertical = 18.dp, horizontal = 16.dp)
        ) {
            Text(
                text = text,
                fontSize = 14.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF1D5C42), // Dark green text always
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun CircularTimerIndicator(timeLeft: Int) {
    Box(
        modifier = Modifier
            .padding(end = 20.dp)
            .size(40.dp),
        contentAlignment = Alignment.Center
    ) {
        // Draw the circular static track
        androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                color = Color(0xFFE8E8E8),
                radius = size.width / 2,
                style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
            )
            // Draw an orange progress line based on time left
            val sweepAngle = (timeLeft / 3f) * 360f
            drawArc(
                color = Color(0xFFF09D51), // Orange
                startAngle = -90f,
                sweepAngle = sweepAngle,
                useCenter = false,
                style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
            )
        }
        
        // Inner text number
        Text(
            text = timeLeft.toString(),
            color = Color(0xFF1D5C42),
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
    }
}
