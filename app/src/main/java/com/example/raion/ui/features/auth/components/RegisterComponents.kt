package com.example.raion.ui.features.auth.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.alpha
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.raion.ui.theme.DesignTokens

/**
 * Komponen Reusable Header untuk halaman Registrasi.
 * Membungkus Tombol Back dan Progress Bar.
 *
 * @param progress Nilai progres dari 0f (0%) hingga 1f (100%).
 * @param onBackClick Aksi ketika tombol panah kembali ditekan.
 */
@Composable
fun RegisterHeader(
    progress: Float,
    showBackButton: Boolean = true,
    onBackClick: () -> Unit
) {
    val grayProgressBar = Color(0xFFD9D9D9)
    val orangeBubble = DesignTokens.Colors.BrandSecondary // Gunakan warna Orange dari DesignTokens
    
    // Animasikan opacity progress bar (0 jika pre-form, 1 jika sedang mengisi form)
    val progressAlpha by androidx.compose.animation.core.animateFloatAsState(
        targetValue = if (progress == 0f) 0f else 1f,
        label = "progress_alpha"
    )

    Row(
        modifier = Modifier.fillMaxWidth().height(48.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AnimatedVisibility(
            visible = showBackButton,
            enter = expandHorizontally(),
            exit = shrinkHorizontally()
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.offset(x = (-12).dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack, // Menggunakan Varian Rounded yang bersahabat
                    contentDescription = "Kembali",
                    tint = Color.Black,
                    modifier = Modifier.size(32.dp)
                )
            }
        }

        // Progress Bar Wrapper (Background Abu-abu)
        Box(
            modifier = Modifier
                .weight(1f)
                .height(14.dp)
                .alpha(progressAlpha)
                .background(color = grayProgressBar, shape = RoundedCornerShape(7.dp)),
            contentAlignment = Alignment.CenterStart
        ) {
            // Indikator Progres (Foreground Oranye)
            if (progress > 0f) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progress)
                        .fillMaxHeight()
                        .background(color = orangeBubble, shape = RoundedCornerShape(7.dp))
                )
            }
        }
    }
}

/**
 * Bentuk (Shape) kustom Jetpack Compose untuk balon percakapan dengan ekor
 * Menggabungkan Kotak (RoundedRect) dan Segitiga (Ekor) menjadi SATU kesatuan path utuh.
 */
class SpeechBubbleShape(
    private val cornerRadiusDp: Dp = 12.dp,
    private val tailHeightDp: Dp = 16.dp, // Tinggi ekor
    private val tailWidthDp: Dp = 24.dp,  // Lebar ekor
    private val tailOffsetDp: Dp = 40.dp  // Seberapa jauh ekor digeser ke kanan dari titik tengah
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val cornerRadius = with(density) { cornerRadiusDp.toPx() }
        val tailHeight = with(density) { tailHeightDp.toPx() }
        val tailWidth = with(density) { tailWidthDp.toPx() }
        val tailOffset = with(density) { tailOffsetDp.toPx() }

        // 1. Path untuk Kotak Balon Utama (sisakan ruang di bawah untuk ekor)
        val bubbleRect = Rect(0f, 0f, size.width, size.height - tailHeight)
        val bubblePath = Path().apply {
            addRoundRect(
                RoundRect(
                    rect = bubbleRect,
                    cornerRadius = CornerRadius(cornerRadius, cornerRadius)
                )
            )
        }

        // 2. Path untuk Ekor Segitiga
        val tailStartX = (size.width / 2f) + tailOffset - (tailWidth / 2f)
        val tailEndX = tailStartX + tailWidth
        
        val tailPath = Path().apply {
            moveTo(tailStartX, size.height - tailHeight) // Sudut kiri ekor
            lineTo(tailStartX + (tailWidth / 2f), size.height) // Titik ujung paling bawah ekor
            lineTo(tailEndX, size.height - tailHeight) // Sudut kanan ekor
            close()
        }

        // 3. Gabungkan keduanya menjadi satu kesatuan poligon tanpa celah
        val finalPath = Path()
        finalPath.op(bubblePath, tailPath, PathOperation.Union)

        return Outline.Generic(finalPath)
    }
}

/**
 * Komponen Reusable untuk Balon Dialog Dino.
 *
 * @param text Pesan yang diucapkan oleh Dino. Ganti baris dengan \n.
 */
@Composable
fun DinoDialogBox(
    text: String,
    modifier: Modifier = Modifier
) {
    val orangeBubble = DesignTokens.Colors.BrandSecondary

    Box(
        modifier = modifier
            .offset(y = (-20).dp)
            .background(
                color = orangeBubble,
                shape = SpeechBubbleShape()
            )
            // Padding bottom harus lebih besar untuk mengompensasi tinggi ekor balon
            // agar teks tetap berada di tengah area kotak.
            .padding(start = 24.dp, end = 24.dp, top = 16.dp, bottom = 32.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium.copy(
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.sp,
                fontSize = 16.sp,
                lineHeight = 26.sp
            ),
            textAlign = TextAlign.Center
        )
    }
}
