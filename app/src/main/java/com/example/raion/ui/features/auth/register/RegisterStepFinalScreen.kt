package com.example.raion.ui.features.auth.register

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.raion.R

@Composable
fun RegisterStepFinalScreen(
    onBackClick: () -> Unit = {},
    onFinishClick: () -> Unit = {}
) {
    val CreamBackground = Color(0xFFFFFBE6)
    val TealColor = Color(0xFF6AC9AB)
    val TealShadowColor = Color(0xFF4E9C85)
    val OrangeBubble = Color(0xFFE89552)

    Column(
        modifier = Modifier.fillMaxSize().background(CreamBackground).padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBackClick, modifier = Modifier.padding(start = 0.dp)) {
                Icon(Icons.Default.ArrowBack, "Kembali", tint = Color.Black, modifier = Modifier.size(32.dp))
            }
            Spacer(modifier = Modifier.width(8.dp))
            // Progress Bar (Penuh 100%)
            Box(modifier = Modifier.weight(1f).height(14.dp).background(color = OrangeBubble, shape = RoundedCornerShape(7.dp)))
        }
        Spacer(modifier = Modifier.weight(1f))
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.offset(y = (-20).dp)) {
            Box(modifier = Modifier.background(color = OrangeBubble, shape = RoundedCornerShape(12.dp)).padding(horizontal = 24.dp, vertical = 16.dp), contentAlignment = Alignment.Center) {
                Text("Yey Semuanya\nSelesaii", style = MaterialTheme.typography.titleMedium.copy(color = Color.White, fontWeight = FontWeight.Bold, letterSpacing = 1.sp, fontSize = 18.sp, lineHeight = 26.sp), textAlign = TextAlign.Center)
            }
            Canvas(modifier = Modifier.size(24.dp).offset(x = 40.dp)) {
                val path = Path().apply { moveTo(0f, 0f); lineTo(size.width, 0f); lineTo(size.width / 2f, size.height); close() }
                drawPath(path = path, color = OrangeBubble)
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Box(contentAlignment = Alignment.BottomCenter, modifier = Modifier.fillMaxWidth().offset(y = (-20).dp)) {
            Canvas(modifier = Modifier.fillMaxWidth(0.40f).height(30.dp).offset(y = (-10).dp)) { drawOval(color = Color.Black.copy(alpha = 0.12f)) }
            // GANTI DENGAN GAMBAR DINO CONFETTI (Merayakan)
            Image(painter = painterResource(id = R.drawable.dinoyeay), contentDescription = "Dino Selesai", modifier = Modifier.fillMaxWidth(0.85f).height(280.dp), contentScale = ContentScale.Fit)
        }

        Spacer(modifier = Modifier.weight(1.5f)) // Mengisi ruang sisa (karena tidak ada textfield)

        Button(
            onClick = onFinishClick, modifier = Modifier.fillMaxWidth().height(45.dp).drawBehind { drawRoundRect(color = TealShadowColor, topLeft = Offset(0f, 5.dp.toPx()), size = size, cornerRadius = CornerRadius(14.dp.toPx())) },
            shape = RoundedCornerShape(14.dp), colors = ButtonDefaults.buttonColors(containerColor = TealColor), elevation = null
        ) {
            Text("SELESAI", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold, letterSpacing = 2.sp, color = Color.White))
        }
        Spacer(modifier = Modifier.height(32.dp))
    }
}