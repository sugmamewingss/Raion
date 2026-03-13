package com.example.raion.ui.features.mission

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.ui.text.font.FontStyle

@Composable
fun MissionRewardScreen(
    onBackClick: () -> Unit
) {
    Scaffold(
        containerColor = Color.Transparent,
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Background gelombang ala halaman SignIn
            WaveBackground()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = DesignTokens.Dimensions.PaddingLarge),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(72.dp))

                // Ilustrasi Mistery Box
                Image(
                    painter = painterResource(id = R.drawable.img_mystery_box),
                    contentDescription = "Mistery Box",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp),
                    contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Teks Judul
                Text(
                    text = "YAY SELAMAT!",
                    color = Color(0xFF1B4D3E), // Hijau Gelap
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 28.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Teks Deskripsi
                Text(
                    text = "Kamu mendapatkan kisi-kisi untuk soal\nbonus pada tantangan jenius!",
                    color = Color.Black,
                    fontWeight = FontWeight.Medium,
                    fontSize = 15.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Kamu keren banget!",
                    color = Color(0xFFCC8E57), // Coklat terang kemerahan
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                    fontSize = 15.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Card Kisi-kisi
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White,
                    border = BorderStroke(1.dp, Color(0xFF1B4D3E)), // Border Hijau Gelap
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Kisi-kisi: Aku sering dijadikan botol\nminuman atau kantong belanja. Aku\nmusuh nomor satu lingkungan!",
                        color = Color(0xFF1B4D3E),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        lineHeight = 20.sp,
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 28.dp)
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                // Tombol Selesai
                Button(
                    onClick = onBackClick,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6AB69E)), // Teal Soft / Hijau
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .padding(bottom = 8.dp)
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        // Inner shadow (bevel bawah)
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .fillMaxWidth()
                                .height(6.dp)
                                .background(Color.Black.copy(alpha = 0.15f))
                        )
                        Text(
                            text = "Selesai",
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 20.sp,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}
