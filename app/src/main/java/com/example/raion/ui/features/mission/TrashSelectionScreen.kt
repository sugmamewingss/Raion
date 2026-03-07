package com.example.raion.ui.features.mission

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import com.example.raion.ui.features.auth.components.WaveBackground

@Composable
fun TrashSelectionScreen(
    onBackClick: () -> Unit,
    onNavigateToOrganic: () -> Unit,
    onNavigateToRecycle: () -> Unit,
    onNavigateToOthers: () -> Unit
) {
    Scaffold(
        containerColor = Color.Transparent, // Background is drawn natively by Box later
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            WaveBackground()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // TopBar dengan Progress Kosong (10%)
                TrashTopBar(progress = 0.1f, onBackClick = onBackClick)

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(16.dp))

                    // Dino and Speech Bubble Area
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Dino Thumbnail Box
                        Box(
                            modifier = Modifier
                                .size(86.dp)
                                .background(Color.White, RoundedCornerShape(12.dp))
                                .border(1.dp, Color(0xFF2C4331), RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.superdinohead),
                                contentDescription = "Dino Avatar",
                                modifier = Modifier
                                    .size(71.dp)
                                    .padding(2.dp),
                                contentScale = ContentScale.Fit
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        // Speech Bubble
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .background(Color(0xFFECA357), RoundedCornerShape(12.dp)) // Orange
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "Sampah jenis apa\nyang kamu buang\nhari ini?",
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp,
                                lineHeight = 20.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Option 1: Sampah Organik
                    TrashItemCard(
                        imageRes = R.drawable.sampahorganik,
                        title = "Sampah Organik",
                        onClick = onNavigateToOrganic
                    )

                    // Option 2: Sampah Daur Ulang
                    TrashItemCard(
                        imageRes = R.drawable.sampahdaurulang,
                        title = "Sampah Daur Ulang",
                        onClick = onNavigateToRecycle
                    )

                    // Option 3: Sampah Lainnya
                    TrashItemCard(
                        imageRes = R.drawable.sampahlainnya,
                        title = "Sampah Lainnya",
                        onClick = onNavigateToOthers
                    )

                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}
