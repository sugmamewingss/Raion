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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.raion.R
import com.example.raion.ui.theme.DesignTokens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizEpisodeScreen(
    onNavigateBack: () -> Unit,
    onChangeBabClick: () -> Unit = onNavigateBack, // Sama dengan back untuk sementara
    onNavigateToPrep: () -> Unit = {}
) {
    // Cream background mimicking the design
    val creamBgColor = Color(0xFFFCFDF2) // Slightly darker than white, a warm cream

    Box(modifier = Modifier.fillMaxSize()) {
        com.example.raion.ui.features.auth.components.WaveBackground()
        Scaffold(
            containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Pilih Episode",
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
            // Selected Chapter Header Box
            SelectedChapterCard(onChangeBabClick = onChangeBabClick)

            Spacer(modifier = Modifier.height(24.dp))

            // Episodes List Container
            EpisodeListContainer(onNavigateToPrep = onNavigateToPrep)
        }
    }
    }
}

@Composable
fun SelectedChapterCard(onChangeBabClick: () -> Unit) {
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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Book Icon
                    Image(
                        painter = painterResource(id = R.drawable.book),
                        contentDescription = "Book Icon",
                        modifier = Modifier.size(56.dp),
                        contentScale = ContentScale.Fit
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    // Details Column
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Bab 1",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Buang sampah sembarangan",
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
                            Badge(text = "2 Episode", bgColor = Color(0xFFDDF5E6), textColor = Color(0xFF388E3C))
                        }
                    }

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
            }
        }
    }
}

@Composable
fun EpisodeListContainer(onNavigateToPrep: () -> Unit = {}) {
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

            // Episode 1 Card (Unlocked)
            EpisodeCard(
                title = "Episode 1",
                subtitle = "Si Trex",
                bgColor = Color(0xFFFFF2CD), // Light Yellow
                borderColor = Color(0xFFDAB46C), // Orange-yellowish border
                isLocked = false,
                onClick = onNavigateToPrep
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Episode 2 Card (Locked)
            EpisodeCard(
                title = "Episode 2",
                subtitle = "Peduli",
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
fun EpisodeCard(
    title: String,
    subtitle: String,
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
            // Paper Icon
            Image(
                painter = painterResource(id = R.drawable.paper),
                contentDescription = "Paper Icon",
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
