package com.example.raion.ui.features.quiz

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.raion.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizBreakScreen(
    onNavigateBack: () -> Unit,
    onContinue: () -> Unit
) {
    val creamBgColor = Color(0xFFFCFDF2)
    val totalQuestions = 10
    val currentProgress = 5 // Halfway

    Box(modifier = Modifier.fillMaxSize()) {
        com.example.raion.ui.features.auth.components.WaveBackground()
        Scaffold(
            containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
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
                    // Custom Progress Bar (5/10)
                    val progressRatio = currentProgress.toFloat() / totalQuestions.toFloat()
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
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Encouragement Bubble
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            ) {
                Column {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFF1D5C42), // Dark Green
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Hebat! Kamu sudah setengah perjalanan mengerjakan tantangan jenius.\nAyo selesaikan sampai akhir!",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(16.dp),
                            lineHeight = 20.sp
                        )
                    }
                    // Tail points down to the mascot
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        androidx.compose.foundation.Canvas(
                            modifier = Modifier
                                .padding(end = 40.dp)
                                .width(28.dp)
                                .height(24.dp)
                        ) {
                            val path = Path().apply {
                                moveTo(0f, 0f)
                                lineTo(size.width, 0f)
                                lineTo(size.width * 0.5f, size.height)
                                close()
                            }
                            drawPath(path = path, color = Color(0xFF1D5C42))
                        }
                    }
                }
            }

            // Mascot Image
            Image(
                painter = painterResource(id = R.drawable.dinobreak),
                contentDescription = "Dino Break Encouragement",
                modifier = Modifier
                    .height(280.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.weight(1f))

            // Continue Button
            Button(
                onClick = onContinue,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF549E83) // Soft Green
                )
            ) {
                Text(
                    text = "Lanjutkan!",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
    }
}
