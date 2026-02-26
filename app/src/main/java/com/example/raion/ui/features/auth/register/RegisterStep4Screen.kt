package com.example.raion.ui.features.auth.register

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
fun RegisterStep4Screen(
    onBackClick: () -> Unit = {},
    onNextClick: () -> Unit = {}
) {
    // State untuk menyimpan ketikan tanggal lahir
    var birthDate by remember { mutableStateOf("") }

    val CreamBackground = Color(0xFFFFFBE6)
    val TealColor = Color(0xFF6AC9AB)
    val TealShadowColor = Color(0xFF4E9C85)
    val OrangeBubble = Color(0xFFE89552)
    val GrayProgressBar = Color(0xFFD9D9D9)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CreamBackground)
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        // --- HEADER (Tombol Back & Progress Bar) ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.padding(start = 0.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Kembali",
                    tint = Color.Black,
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Progress Bar (Sudah terisi sekitar 25%)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(14.dp)
                    .background(color = GrayProgressBar, shape = RoundedCornerShape(7.dp)),
                contentAlignment = Alignment.CenterStart // Mulai isi dari kiri
            ) {
                // Kotak oranye sebagai indikator progres
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.25f) // Mengisi 25% dari lebar abu-abu
                        .fillMaxHeight()
                        .background(color = OrangeBubble, shape = RoundedCornerShape(7.dp))
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // --- BALON TEKS (Speech Bubble) ---
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.offset(y = (-20).dp)
        ) {
            Box(
                modifier = Modifier
                    .background(color = OrangeBubble, shape = RoundedCornerShape(12.dp))
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Kapan hari ulang\ntahunmu?",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        fontSize = 18.sp,
                        lineHeight = 26.sp
                    ),
                    textAlign = TextAlign.Center
                )
            }

            Canvas(
                modifier = Modifier
                    .size(24.dp)
                    .offset(x = 40.dp)
            ) {
                val path = Path().apply {
                    moveTo(0f, 0f)
                    lineTo(size.width, 0f)
                    lineTo(size.width / 2f, size.height)
                    close()
                }
                drawPath(path = path, color = OrangeBubble)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // --- ILUSTRASI DINO ---
        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = (-20).dp)
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxWidth(0.40f)
                    .height(30.dp)
                    .offset(y = (-10).dp)
            ) {
                drawOval(color = Color.Black.copy(alpha = 0.12f))
            }

            Image(
                painter = painterResource(id = R.drawable.dinoultah),
                contentDescription = "Dino Bawa Kue",
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(220.dp),
                contentScale = ContentScale.Fit
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- TEXT FIELD INPUT TANGGAL LAHIR ---
        OutlinedTextField(
            value = birthDate,
            onValueChange = { birthDate = it },
            placeholder = {
                Text(
                    "Masukkan Tanggal Lahir Kamu",
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodyLarge
                )
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = TealColor,
                unfocusedBorderColor = Color(0xFFBDBDBD),
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                cursorColor = TealColor
            )
        )

        Spacer(modifier = Modifier.weight(1.5f))

        // --- TOMBOL LANJUTKAN ---
        Button(
            onClick = onNextClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(45.dp)
                .drawBehind {
                    drawRoundRect(
                        color = TealShadowColor,
                        topLeft = Offset(0f, 5.dp.toPx()),
                        size = size,
                        cornerRadius = CornerRadius(14.dp.toPx())
                    )
                },
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = TealColor),
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
fun RegisterStep4Preview() {
    RaionTheme {
        RegisterStep4Screen()
    }
}