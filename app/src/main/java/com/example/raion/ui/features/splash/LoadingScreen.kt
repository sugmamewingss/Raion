package com.example.raion.ui.features.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.raion.R
import com.example.raion.ui.features.auth.components.WaveBackground
import com.example.raion.ui.theme.DesignTokens
import kotlinx.coroutines.delay

@Composable
fun LoadingScreen(
    onNavigateNext: () -> Unit
) {
    // Kumpulan Trivias
    val triviaList = listOf(
        Pair(
            "Butuh 450 tahun untuk plastik terurai, tapi akhirnya hanya jadi partikel kecil yang mencemari lingkungan.",
            "Mengerikan ya!"
        ),
        Pair(
            "Menghemat 1 ton kertas sama dengan menyelamatkan 17 batang pohon dari penebangan.",
            "Ayo hemat kertas!"
        ),
        Pair(
            "Tahukah kamu? Energi dari 1 kaleng daur ulang cukup untuk menyalakan TV selama 3 jam.",
            "Luar biasa!"
        ),
        Pair(
            "Meninggalkan pengisi daya HP tercolok tetap menyedot listrik walau sedang tidak dipakai.",
            "Jangan lupa dicabut!"
        )
    )

    // Pengambil trivia acak saat layar pertama kali dirender
    val selectedTrivia = remember { triviaList.random() }

    // State untuk titik animasi "Memuat"
    var dotsCount by remember { mutableIntStateOf(1) }

    // Launcher untuk animasi titik dan navigasi keluar
    LaunchedEffect(Unit) {
        // Efek titik memuat berjalan asinkron
        while (true) {
            delay(500)
            dotsCount = (dotsCount % 4) + 1
        }
    }

    LaunchedEffect(Unit) {
        // Tunggu 3,5 detik baru pindah layar
        delay(3500)
        onNavigateNext()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Latar gelombang dari layar Auth
        WaveBackground()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Maskot Dino Loading
            Image(
                painter = painterResource(id = R.drawable.img_dino_loading),
                contentDescription = "Dino Membuang Sampah",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(260.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Judul
            Text(
                text = "Tahukah Kamu?",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 28.sp,
                    color = DesignTokens.Colors.BrandDark
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Deskripsi dan Penekanan Emosional
            val highlightedText = buildAnnotatedString {
                withStyle(style = SpanStyle(color = Color.Black, fontSize = 16.sp, fontWeight = FontWeight.Medium)) {
                    append(selectedTrivia.first)
                }
                append("\n")
                withStyle(style = SpanStyle(
                    color = DesignTokens.Colors.OrangePrimary, 
                    fontSize = 16.sp, 
                    fontWeight = FontWeight.Bold,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                )) {
                    append(selectedTrivia.second)
                }
            }
            Text(
                text = highlightedText,
                textAlign = TextAlign.Center,
                lineHeight = 24.sp
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Teks Memuat
            val dotsString = ".".repeat(dotsCount)
            Text(
                text = "Memuat$dotsString",
                style = MaterialTheme.typography.displaySmall.copy(
                    fontWeight = FontWeight.Black,
                    color = Color.Black
                )
            )
        }
    }
}
