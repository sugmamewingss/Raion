package com.example.raion.ui.features.auth.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path

@Composable
fun WaveBackground(
    modifier: Modifier = Modifier,
    baseColor: Color = Color(0xFFE4F1EE), // Biru pucat (light aqua background base)
    waveColor: Color = Color(0xFFFCFBF2)  // Krem organik blob (Off-white wave shape)
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(baseColor)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            val path = Path().apply {
                // Mulai dari sisi kiri layar tengah atas
                moveTo(0f, height * 0.36f)
                
                // Lengkungan puncak ke arah tengah atas
                cubicTo(
                    width * 0.1f, height * 0.36f, 
                    width * 0.25f, height * 0.1f, 
                    width * 0.45f, height * 0.1f
                )
                
                // Menurun melengkung ke arah kanan atas
                cubicTo(
                    width * 0.7f, height * 0.1f, 
                    width * 0.85f, height * 0.2f, 
                    width, height * 0.2f
                )
                
                // Garis menurun lurus di batas kanan layar
                lineTo(width, height * 0.38f)
                
                // Lekukan (indent) ke arah dalam dari sisi kanan
                cubicTo(
                    width * 0.85f, height * 0.4f, 
                    width * 0.85f, height * 0.5f, 
                    width, height * 0.52f
                )
                
                // Lanjut turun di batas kanan layar
                lineTo(width, height * 0.75f)
                
                // Bulatan besar di sudut kanan bawah
                cubicTo(
                    width * 0.9f, height * 0.95f, 
                    width * 0.5f, height * 0.95f, 
                    width * 0.4f, height * 0.85f
                )
                
                // Lekukan kembali naik dengan lembut ke sisi kiri layar (lebih kalem dibanding sebelumnya)
                cubicTo(
                    width * 0.3f, height * 0.72f, 
                    width * 0.1f, height * 0.72f, 
                    0f, height * 0.7f
                )
                
                // Tutup path secara otomatis melalui garis lurus ke titik awal (moveTo)
                close()
            }

            drawPath(
                path = path,
                color = waveColor
            )
        }
    }
}
