package com.example.raion.ui.features.quiz

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.saveable.rememberSaveable
import com.example.raion.R
import kotlinx.coroutines.delay

data class QuizQuestion(
    val id: Int,
    val questionText: String,
    val options: List<String>,
    val correctAnswerIndex: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizQuestionScreen(
    onNavigateBack: () -> Unit,
    onHalfwayBreak: () -> Unit = {},
    onQuizFinished: (Int) -> Unit = {} // Added Int for passing correctAnswersCount
) {
    val creamBgColor = Color(0xFFFCFDF2)
    var selectedAnswerIndex by remember { mutableStateOf<Int?>(null) }
    var showPopup by remember { mutableStateOf(false) }
    var isAnswerCorrect by remember { mutableStateOf(false) }
    
    // Timer state
    var timeLeft by remember { mutableStateOf(3) }

    // Hardcoded question list
    val questions = remember {
        listOf(
            QuizQuestion(
                id = 1,
                questionText = "Mengapa T-Rex di dalam cerita disebut merusak keindahan hutan?",
                options = listOf(
                    "Karena suaranya terlalu berisik",
                    "Karena membuang sampah kaleng sembarangan",
                    "Karena dia terlalu besar untuk hutan salju"
                ),
                correctAnswerIndex = 1
            ),
            QuizQuestion(
                id = 2,
                questionText = "Sampah kaleng yang dibuang T-Rex terbuat dari logam. Berapa lama yang dibutuhkan alam untuk menghancurkan kaleng?",
                options = listOf(
                    "1 minggu",
                    "1 tahun",
                    "50 sampai 100 tahun"
                ),
                correctAnswerIndex = 2
            ),
            QuizQuestion(
                id = 3,
                questionText = "Hutan salju yang bersih menghasilkan udara segar. Gas apa yang paling kita butuhkan dari udara segar tersebut?",
                options = listOf(
                    "Karbondioksida",
                    "Oksigen",
                    "Nitrogen"
                ),
                correctAnswerIndex = 1
            ),
            QuizQuestion(
                id = 4,
                questionText = "Apa yang akan terjadi jika sampah kaleng menumpuk di hutan dan tertutup salju?",
                options = listOf(
                    "Sampah akan berubah menjadi es",
                    "Tanah menjadi tercemar dan hewan bisa terluka",
                    "Sampah akan hilang terbawa angin"
                ),
                correctAnswerIndex = 1
            ),
            QuizQuestion(
                id = 5,
                questionText = "Bagaimana sikapmu jika melihat temanmu membuang sampah sembarangan di hutan?",
                options = listOf(
                    "Membiarkannya saja karena bukan urusan kita",
                    "Ikut membuang sampah sembarangan",
                    "Menegur dengan sopan dan mengajaknya mencari tempat sampah"
                ),
                correctAnswerIndex = 2
            ),
            QuizQuestion(
                id = 6,
                questionText = "Aku adalah julukan untuk benda yang bisa dipakai kembali agar tidak jadi sampah, seperti botol minum. Siapakah aku?",
                options = listOf(
                    "Sekali pakai",
                    "Reusable (Guna Ulang)",
                    "Sekali buang"
                ),
                correctAnswerIndex = 1
            ),
            QuizQuestion(
                id = 7,
                questionText = "Sampah kaleng dan botol kaca sebaiknya dibuang ke tempat sampah berwarna apa?",
                options = listOf(
                    "Hijau (Organik)",
                    "Kuning (Daur ulang)",
                    "Merah (Bahan Berbahaya)"
                ),
                correctAnswerIndex = 1
            ),
            QuizQuestion(
                id = 8,
                questionText = "Pohon di hutan salju membantu mendinginkan bumi. Apa nama peristiwa memanasnya suhu bumi akibat hutan yang rusak?",
                options = listOf(
                    "Hujan badai",
                    "Pemanasan Global",
                    "Musim salju abadi"
                ),
                correctAnswerIndex = 1
            ),
            QuizQuestion(
                id = 9,
                questionText = "Di dunia nyata, kaleng bekas bisa dilebur untuk dibuat menjadi benda baru. Apa nama proses ini?",
                options = listOf(
                    "Penimbunan",
                    "Daur Ulang (Recycle)",
                    "Pembakaran"
                ),
                correctAnswerIndex = 1
            ),
            QuizQuestion(
                id = 10,
                questionText = "Apa pesan moral utama dari komik petualangan Dino tadi?",
                options = listOf(
                    "Jangan bermain di hutan salju sendirian",
                    "T-Rex adalah dinosaurus yang paling kuat",
                    "Menjaga kebersihan adalah tugas kita bersama, bukan hanya Dino"
                ),
                correctAnswerIndex = 2
            ),
            QuizQuestion(
                id = 11,
                questionText = "Jika seekor Brontosaurus membuang aku ke tanah pada zaman purba jutaan tahun lalu, mungkin sampai hari ini aku masih ada dan belum hancur. Aku tidak bisa dimakan oleh tanah (tidak bisa membusuk). Siapakah aku?",
                options = listOf(
                    "Kulit Pisang",
                    "Tulang Ikan",
                    "Plastik"
                ),
                correctAnswerIndex = 2
            )
        )
    }

    var currentQuestionIndex by rememberSaveable { mutableStateOf(0) }
    var correctAnswersCount by rememberSaveable { mutableStateOf(0) }
    val currentQuestion = questions.getOrNull(currentQuestionIndex) ?: questions.last()

    // Total expected questions for progress bar
    val totalQuestions = 10 

    // Timer Logic
    LaunchedEffect(showPopup) {
        if (showPopup) {
            timeLeft = 3 // Reset timer
            while (timeLeft > 0) {
                delay(1000L)
                timeLeft--
            }
            
            // Timer finished -> tally score and move to next question or finish quiz
            // Tally score before resetting
            if (isAnswerCorrect) {
                correctAnswersCount++
            }

            if (currentQuestionIndex < questions.size - 1) {
                // If we just finished question 5 (index 4), trigger halfway break first
                if (currentQuestionIndex == 4) {
                    currentQuestionIndex++ // Move index to 5 so when returning we load question 6
                    selectedAnswerIndex = null
                    showPopup = false
                    onHalfwayBreak()
                } else {
                    currentQuestionIndex++
                    selectedAnswerIndex = null
                    showPopup = false
                }
            } else if (currentQuestionIndex == questions.size - 1) {
                // We reached the end of currently available hardcoded questions.
                // If index is 4, it means we finished the 5th question and there are NO MORE questions.
                // But the user wants a break after the 5th question.
                if (currentQuestionIndex == 4) {
                    currentQuestionIndex++ // Push to 5
                    selectedAnswerIndex = null
                    showPopup = false
                    onHalfwayBreak()
                } else {
                    onQuizFinished(correctAnswersCount)
                }
            } else {
                onQuizFinished(correctAnswersCount) // Fallback
            }
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
                    if (showPopup) {
                        CircularTimerIndicator(timeLeft = timeLeft)
                    } else {
                        // Custom Progress Bar (Capped at 1.0 for bonus questions)
                        val progressRatio = ((currentQuestionIndex + 1).toFloat() / totalQuestions.toFloat()).coerceAtMost(1.0f)
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
            // Main Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (currentQuestionIndex == 10) {
                    Text(
                        text = "Soal Bonus",
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
                    painter = painterResource(id = R.drawable.dinoask),
                    contentDescription = "Dino Asking",
                    modifier = Modifier
                        .height(260.dp)
                        .fillMaxWidth(),
                    contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Options
                currentQuestion.options.forEachIndexed { index, optionText ->
                    val isSelected = selectedAnswerIndex == index
                    OptionCard(
                        text = optionText,
                        isSelected = isSelected,
                        onClick = {
                            if (!showPopup) { // Prevent clicking again while popup is showing
                                selectedAnswerIndex = index
                                isAnswerCorrect = (index == currentQuestion.correctAnswerIndex)
                                showPopup = true
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            // Animated Popup Overlay at the bottom
            AnimatedVisibility(
                visible = showPopup,
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
                        // Provide a translucent background to dim behind the popup if desired
                        // .background(Color.Black.copy(alpha = 0.3f)) 
                        .padding(bottom = 7.dp) // 7dp offset from bottom as requested
                ) {
                    val popupImageRes = if (isAnswerCorrect) R.drawable.benar else R.drawable.salah
                    Image(
                        painter = painterResource(id = popupImageRes),
                        contentDescription = if (isAnswerCorrect) "Jawaban Benar" else "Jawaban Salah",
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight() // Adjust based on the actual asset's aspect ratio
                            .clickable {
                                // Close popup on click (or proceed to next question)
                                showPopup = false
                                selectedAnswerIndex = null
                            },
                        contentScale = ContentScale.FillWidth
                    )
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
