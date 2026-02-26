package com.example.raion.ui.features.auth

import androidx.compose.ui.unit.sp
import android.os.Build.VERSION.SDK_INT
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.example.raion.R

@Composable
fun AuthSelectionScreen(
    onLoginClick: () -> Unit = {},
    onRegisterClick: () -> Unit = {}
) {
    val context = LocalContext.current

    val imageLoader = remember {
        ImageLoader.Builder(context)
            .components {
                if (SDK_INT >= 28) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
            }
            .build()
    }


    // --- SEMENTARA: Warna Manual (Nanti kita ganti ke DesignTokens) ---
    val TealBackground = Color(0xFF6AC9AB)
    val CreamBackground = Color(0xFFFFFBE6)
    val OrangeText = Color(0xFFF4A261)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(TealBackground)
    ) {
        // --- LAYER 1 (Paling Bawah): Kartu Krem ---
        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .fillMaxHeight(0.75f),
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
            color = CreamBackground
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 1. Menggeser teks agak ke bawah menjauhi cakar Dino
                Spacer(modifier = Modifier.height(27.dp))

                // 2. Menambahkan letterSpacing dan ExtraBold pada judul
                Text(
                    text = "Selamat Datang",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.ExtraBold, // Dibuat lebih tebal
                        color = Color.Black,
                        letterSpacing = 3.sp,
                        fontSize = 35.sp
                    )
                )

                Text(
                    text = "Sahabat Dino!",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = OrangeText,
                        letterSpacing = 1.5.sp,
                        fontSize = 24.sp
                    )
                )

                // 3. Mengatur jarak atas GIF
                Spacer(modifier = Modifier.weight(0.8f))

                AsyncImage(
                    model = R.drawable.trash, // Asumsi kamu masih pakai Coil
                    imageLoader = imageLoader,
                    contentDescription = "Ilustrasi Tong Sampah Bergerak",
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .height(240.dp),
                    contentScale = ContentScale.Fit
                )

                // 4. Mengatur jarak bawah GIF agar tombol tidak terlalu nempel
                Spacer(modifier = Modifier.weight(1.2f))

                // Tombol MASUK
                Button(
                    onClick = onLoginClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = TealBackground),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "MASUK",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 3.sp // Jarak huruf agak jauh
                        )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Tombol BUAT AKUN
                OutlinedButton(
                    onClick = onRegisterClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    // Menambahkan border tegas agar mirip desain
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, Color.Black),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black)
                ) {
                    Text(
                        text = "BUAT AKUN",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 3.sp // Jarak huruf agak jauh
                        )
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // 5. Jarak huruf sangat jauh untuk "version 1.0"
                Text(
                    text = "version 1.0",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = Color.LightGray,
                        letterSpacing = 10.sp, // Jarak huruf sangat jauh
                        fontWeight = FontWeight.Medium
                    )
                )

                Spacer(modifier = Modifier.height(16.dp)) // Beri sedikit ruang di paling bawah
            }
        }

        // --- LAYER 2 (Paling Atas): Kepala Dino ---
        Image(
            painter = painterResource(id = R.drawable.dinohead),
            contentDescription = "Dino Mascot",
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth(0.6f)
                .padding(top = 65.dp),
            contentScale = ContentScale.FillWidth
        )
    }
}

@Preview(showBackground = true, heightDp = 800)
@Composable
fun AuthScreenPreview() {
    MaterialTheme {
        AuthSelectionScreen()
    }
}