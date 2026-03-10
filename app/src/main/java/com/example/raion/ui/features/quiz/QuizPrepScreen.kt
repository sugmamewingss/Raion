package com.example.raion.ui.features.quiz

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.raion.R
import com.example.raion.ui.theme.DesignTokens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizPrepScreen(
    onNavigateBack: () -> Unit,
    onStartQuiz: () -> Unit = {} // Placeholder for future quiz execution
) {
    val creamBgColor = Color(0xFFFCFDF2) // Warm cream background

    Box(modifier = Modifier.fillMaxSize()) {
        com.example.raion.ui.features.auth.components.WaveBackground()
        Scaffold(
            containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Persiapan",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.Black
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.Black
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
                .padding(horizontal = 20.dp, vertical = 24.dp)
        ) {
            // Mascot Talk Bubble Card
            MascotBubbleCard()

            Spacer(modifier = Modifier.height(24.dp))

            // Details Container
            PrepDetailsContainer(onStartQuiz = onStartQuiz)
        }
    }
    }
}

@Composable
fun MascotBubbleCard() {
    Surface(
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color(0xFF1D5C42)), // Dark Green Border
        color = Color.White,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 16.dp, end = 20.dp, top = 20.dp, bottom = 0.dp) // Bottom padding 0 to let mascot overflow slightly if needed, adjust if cut off
        ) {
            // Mascot Image (Left)
            Image(
                painter = painterResource(id = R.drawable.dinothinking),
                contentDescription = "Mascot Thinking",
                modifier = Modifier
                    .width(140.dp)
                    .height(160.dp)
                    .offset(y = 10.dp), // Push down slightly to align bottom with card edge if desired
                contentScale = ContentScale.Fit
            )
            
            Spacer(modifier = Modifier.width(16.dp))

            // Text (Right)
            Text(
                text = "Sudah siap\nuntuk\nmengerjakan\nTantangan\nJenius, Sobat\nGobi?",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = 16.dp)
            )
        }
    }
}

@Composable
fun PrepDetailsContainer(onStartQuiz: () -> Unit) {
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
                    modifier = Modifier.padding(16.dp)
                ) {
                    // Bab Info
                    InfoBox(
                        title = "Bab 1",
                        subtitle = "Buang sampah sembarangan",
                        iconRes = R.drawable.book,
                        badges = {
                            Row {
                                Badge(text = "+50 XP", bgColor = Color(0xFFD9F1FF), textColor = Color(0xFF2C84C7))
                                Spacer(modifier = Modifier.width(6.dp))
                                Badge(text = "+150", bgColor = Color(0xFFFFECB3), textColor = Color(0xFFD69400), isCoin = true)
                                Spacer(modifier = Modifier.width(6.dp))
                                Badge(text = "2 Episode", bgColor = Color(0xFFDDF5E6), textColor = Color(0xFF388E3C))
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Episode Info
                    InfoBox(
                        title = "Episode 1",
                        subtitle = "Si Trex",
                        iconRes = R.drawable.paper,
                        badges = {
                            Row {
                                Badge(text = "+50 XP", bgColor = Color(0xFFD9F1FF), textColor = Color(0xFF2C84C7))
                                Spacer(modifier = Modifier.width(6.dp))
                                Badge(text = "+150", bgColor = Color(0xFFFFECB3), textColor = Color(0xFFD69400), isCoin = true)
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Question Count Info
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, cardColor), // Dark Green Border 
                        color = Color.White,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(16.dp)
                        ) {
                            // Conceptual clock icon placeholder, we can use an outline circle with text or image
                            Surface(
                                shape = CircleShape,
                                border = BorderStroke(1.5.dp, Color(0xFF5F7D93)), // Slate grey border for clock
                                color = Color.Transparent,
                                modifier = Modifier.size(40.dp)
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
                                    text = "10 Soal",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.DarkGray
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Start Button
                    Button(
                        onClick = onStartQuiz,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
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


@Composable
fun InfoBox(
    title: String,
    subtitle: String,
    iconRes: Int,
    badges: @Composable () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color(0xFF1D5C42)), // Green border
        color = Color.White,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = "$title Icon",
                modifier = Modifier.size(46.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
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
                badges()
            }
        }
    }
}
