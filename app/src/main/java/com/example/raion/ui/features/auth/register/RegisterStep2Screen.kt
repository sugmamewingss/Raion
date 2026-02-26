package com.example.raion.ui.features.auth.register

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.raion.R
import com.example.raion.ui.theme.RaionTheme

@Composable
fun RegisterStep2Screen(
    onBackClick: () -> Unit = {},
    onNextClick: () -> Unit = {}
) {
    val CreamBackground = Color(0xFFFFFBE6)
    val TealColor = Color(0xFF6AC9AB)
    val OrangeBubble = Color(0xFFE89552) // Warna oranye untuk balon teks
    val TealShadowColor = Color(0xFF4E9C85)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CreamBackground)
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- HEADER (Tombol Back) ---
        Spacer(modifier = Modifier.height(24.dp))
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart) {
            IconButton(
                onClick = onBackClick,
                // Menggeser sedikit ke kiri agar sejajar dengan margin
                modifier = Modifier.padding(start = 0.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Kembali",
                    tint = Color.Black,
                    modifier = Modifier.size(32.dp) // Ukuran icon diperbesar sedikit
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // --- BALON TEKS (Speech Bubble) ---
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.offset(x = 35.dp, y = (-30).dp) // Semakin besar angkanya, semakin ke kanan
        ) {
            // Kotak Teks Oranye
            Box(
                modifier = Modifier
                    .background(color = OrangeBubble, shape = RoundedCornerShape(12.dp))
                    .padding(horizontal = 27.dp, vertical = 15.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Ayo siapkan akun\nkamu dengan\nbeberapa pertanyaan",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp,
                        lineHeight = 24.sp,
                        fontSize = 19.sp
                    ),
                    textAlign = TextAlign.Center
                )
            }

            // Segitiga di bawah kotak teks (Buntut balon)
            Canvas(modifier = Modifier.size(24.dp)) {
                val path = Path().apply {
                    moveTo(15f, 0f) // Titik kiri atas
                    lineTo(size.width, -5f) // Titik kanan atas
                    lineTo(size.width / 40f, size.height) // Titik tengah bawah
                    close()
                }
                drawPath(path = path, color = OrangeBubble)
            }
        }

        Spacer(modifier = Modifier.height(3.dp))

        // --- ILUSTRASI DINO ---
        Box(
            contentAlignment = Alignment.BottomCenter, // Menyelaraskan konten di tengah bawah
            modifier = Modifier.fillMaxWidth()
                .offset(x = -30.dp, y = (-60).dp)
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxWidth(0.40f) // Lebar bayangan
                    .height(30.dp)       // Ketebalan bayangan
                    .offset(y = (-20).dp)        // Posisi bayangan
            ) {
                drawOval(color = Color.Black.copy(alpha = 0.12f)) // Warna abu-abu transparan
            }

            Image(
                painter = painterResource(id = R.drawable.dinonyapa),
                contentDescription = "Dino Menyapa",
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .height(250.dp),
                contentScale = ContentScale.Fit
            )
        }

        Spacer(modifier = Modifier.weight(1.5f))

        // --- TOMBOL LANJUTKAN ---
        Button(
            onClick = onNextClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(45.dp)
                // [TRIK UTAMA] Menggambar bayangan di belakang tombol
                .drawBehind {
                    drawRoundRect(
                        color = TealShadowColor, // Warna bayangan gelap
                        // Geser posisi gambar ke bawah (Offset Y plus)
                        // Angka 8.dp.toPx() ini adalah ketebalan bayangannya
                        topLeft = Offset(0f, 5.dp.toPx()),
                        size = size, // Ukuran sama dengan tombol
                        // Samakan radius sudutnya dengan bentuk tombol
                        cornerRadius = CornerRadius(14.dp.toPx())
                    )
                },
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = TealColor),
            // [PENTING] Matikan elevation bawaan agar tidak aneh
            elevation = null
        ) {
            Text(
                text = "LANJUTKAN",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 2.sp,
                    color = Color.White
                )
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Preview(showBackground = true, heightDp = 800)
@Composable
fun RegisterStep2Preview() {
    RaionTheme {
        RegisterStep2Screen()
    }
}