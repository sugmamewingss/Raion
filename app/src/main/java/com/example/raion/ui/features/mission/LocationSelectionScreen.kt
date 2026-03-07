package com.example.raion.ui.features.mission

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import com.example.raion.ui.theme.DesignTokens

@Composable
fun LocationSelectionScreen(
    onBackClick: () -> Unit,
    onLocationClick: (String) -> Unit
) {
    Scaffold(
        containerColor = Color.Transparent,
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            WaveBackground()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // TopBar dengan Progress ~65%
                TrashTopBar(progress = 0.65f, onBackClick = onBackClick)

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
                                .background(Color(0xFFECA357), RoundedCornerShape(12.dp))
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "Kamu membuang sampah tersebut di tempat sampah yang terletak dimana?",
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp,
                                lineHeight = 19.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // 2x2 Grid of Location Cards
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        LocationCard(
                            imageRes = R.drawable.kantin,
                            title = "Kantin",
                            onClick = { onLocationClick("Kantin") },
                            modifier = Modifier.weight(1f)
                        )
                        LocationCard(
                            imageRes = R.drawable.ruangkelas,
                            title = "Ruang Kelas",
                            onClick = { onLocationClick("Ruang Kelas") },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        LocationCard(
                            imageRes = R.drawable.halaman,
                            title = "Halaman",
                            onClick = { onLocationClick("Halaman") },
                            modifier = Modifier.weight(1f)
                        )
                        LocationCard(
                            imageRes = R.drawable.toilet,
                            title = "Toilet",
                            onClick = { onLocationClick("Toilet") },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}

@Composable
fun LocationCard(
    imageRes: Int,
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(Color.White, RoundedCornerShape(12.dp))
            .border(1.dp, DesignTokens.Colors.OrangePrimary, RoundedCornerShape(12.dp))
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Gambar Lokasi
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = title,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.2f)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Tombol Label
        Box(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .background(Color(0xFFFFF6ED), RoundedCornerShape(8.dp))
                .border(1.dp, DesignTokens.Colors.OrangePrimary, RoundedCornerShape(8.dp))
                .clip(RoundedCornerShape(8.dp))
                .clickable { onClick() }
                .padding(vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = title,
                color = DesignTokens.Colors.OrangePrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }
    }
}
