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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.raion.R

@Composable
fun RegisterStep6Screen(
    onBackClick: () -> Unit = {},
    onNextClick: () -> Unit = {}
) {
    var confirmPassword by remember { mutableStateOf("") }

    val CreamBackground = Color(0xFFFFFBE6)
    val TealColor = Color(0xFF6AC9AB)
    val TealShadowColor = Color(0xFF4E9C85)
    val OrangeBubble = Color(0xFFE89552)
    val GrayProgressBar = Color(0xFFD9D9D9)

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
            Box(modifier = Modifier.weight(1f).height(14.dp).background(color = GrayProgressBar, shape = RoundedCornerShape(7.dp)), contentAlignment = Alignment.CenterStart) {
                Box(modifier = Modifier.fillMaxWidth(0.75f).fillMaxHeight().background(color = OrangeBubble, shape = RoundedCornerShape(7.dp))) // Terisi 75%
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.offset(y = (-20).dp)) {
            Box(modifier = Modifier.background(color = OrangeBubble, shape = RoundedCornerShape(12.dp)).padding(horizontal = 24.dp, vertical = 16.dp), contentAlignment = Alignment.Center) {
                Text("Masukkan sandi yang\nbaru kamu buat", style = MaterialTheme.typography.titleMedium.copy(color = Color.White, fontWeight = FontWeight.Bold, letterSpacing = 1.sp, fontSize = 18.sp, lineHeight = 26.sp), textAlign = TextAlign.Center)
            }
            Canvas(modifier = Modifier.size(24.dp).offset(x = 40.dp)) {
                val path = Path().apply { moveTo(0f, 0f); lineTo(size.width, 0f); lineTo(size.width / 2f, size.height); close() }
                drawPath(path = path, color = OrangeBubble)
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Box(contentAlignment = Alignment.BottomCenter, modifier = Modifier.fillMaxWidth().offset(y = (-20).dp)) {
            Canvas(modifier = Modifier.fillMaxWidth(0.40f).height(30.dp).offset(y = (-10).dp)) { drawOval(color = Color.Black.copy(alpha = 0.12f)) }
            // GANTI DENGAN GAMBAR DINO SSSHH (Telunjuk di mulut)
            Image(painter = painterResource(id = R.drawable.dinokunci2), contentDescription = "Dino Ssh", modifier = Modifier.fillMaxWidth(0.7f).height(220.dp), contentScale = ContentScale.Fit)
        }
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = confirmPassword, onValueChange = { confirmPassword = it },
            placeholder = { Text("Masukkan Sekali lagi kata sandinya", color = Color.Gray) },
            modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = TealColor, unfocusedBorderColor = Color(0xFFBDBDBD), focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent)
        )
        Spacer(modifier = Modifier.weight(1.5f))
        Button(
            onClick = onNextClick, modifier = Modifier.fillMaxWidth().height(45.dp).drawBehind { drawRoundRect(color = TealShadowColor, topLeft = Offset(0f, 5.dp.toPx()), size = size, cornerRadius = CornerRadius(14.dp.toPx())) },
            shape = RoundedCornerShape(14.dp), colors = ButtonDefaults.buttonColors(containerColor = TealColor), elevation = null
        ) {
            Text("LANJUTKAN", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold, letterSpacing = 2.sp, color = Color.White))
        }
        Spacer(modifier = Modifier.height(32.dp))
    }
}