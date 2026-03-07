package com.example.raion.ui.features.mission

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
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
import com.example.raion.ui.features.auth.components.WaveBackground
import com.example.raion.ui.theme.DesignTokens

@Composable
fun MissionIncompleteScreen(
    onBackClick: () -> Unit,
    onContinue: () -> Unit,
    onExit: () -> Unit
) {
    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 24.dp)
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = "Kembali",
                        tint = Color.Black,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        },
        containerColor = Color.Transparent,
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            WaveBackground()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = DesignTokens.Dimensions.PaddingLarge),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(32.dp))

                // Ilustrasi Dino (belum selesai)
                Image(
                    painter = painterResource(id = R.drawable.dinonotdoneyet),
                    contentDescription = "Dino Belum Selesai",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp),
                    contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Judul "Misi Kamu Belum Selesai Nih!"
                Text(
                    text = "Misi Kamu Belum\nSelesai Nih!",
                    color = Color(0xFF1B4D3E),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 26.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 34.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Deskripsi
                Text(
                    text = "Ayo selesaikan misi hari ini agar\nmendapat hadiah imbalan untuk aksi\nkerenmu!",
                    color = Color.Black,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 21.sp
                )

                Spacer(modifier = Modifier.weight(1f))

                // Tombol "Lanjutkan!" dengan shadow
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .background(Color(0xFF2C4331), RoundedCornerShape(12.dp))
                        .padding(bottom = 4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFF50B498), RoundedCornerShape(12.dp))
                            .border(1.dp, Color(0xFF2C4331), RoundedCornerShape(12.dp))
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { onContinue() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Lanjutkan!",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Tombol "Keluar" (outlined, tanpa shadow)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .border(2.dp, Color(0xFF50B498), RoundedCornerShape(12.dp))
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onExit() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Keluar",
                        color = Color(0xFF50B498),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}
