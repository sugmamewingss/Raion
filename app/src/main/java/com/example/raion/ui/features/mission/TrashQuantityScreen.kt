package com.example.raion.ui.features.mission

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.raion.R
import com.example.raion.ui.features.auth.components.WaveBackground
import com.example.raion.ui.theme.DesignTokens

@Composable
fun TrashQuantityScreen(
    onBackClick: () -> Unit,
    onQuantitySelected: (Int) -> Unit
) {
    var customQuantity by remember { mutableStateOf("") }

    Scaffold(
        containerColor = Color.Transparent,
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            WaveBackground()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // TopBar dengan Progress hampir penuh (~90%)
                TrashTopBar(progress = 0.9f, onBackClick = onBackClick)

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(16.dp))

                    // Dino and Speech Bubble Area
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Dino Thumbnail Box
                        Box(
                            modifier = Modifier
                                .size(86.dp)
                                .background(Color.White, RoundedCornerShape(12.dp))
                                .border(1.dp, Color(0xFF2C4331), RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.superdinohead),
                                contentDescription = "Dino Avatar",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(12.dp)),
                                contentScale = ContentScale.Crop
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        // Speech Bubble
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .background(Color(0xFFECA357), RoundedCornerShape(12.dp))
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "Berapa jumlah\nsampah yang kamu\nbuang?",
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp,
                                lineHeight = 20.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Opsi 1 Buah
                    QuantityOptionCard(text = "1 Buah", onClick = { onQuantitySelected(1) })
                    Spacer(modifier = Modifier.height(12.dp))

                    // Opsi 2 Buah
                    QuantityOptionCard(text = "2 Buah", onClick = { onQuantitySelected(2) })
                    Spacer(modifier = Modifier.height(12.dp))

                    // Opsi 3 Buah
                    QuantityOptionCard(text = "3 Buah", onClick = { onQuantitySelected(3) })
                    Spacer(modifier = Modifier.height(12.dp))

                    // Opsi 4 Buah
                    QuantityOptionCard(text = "4 Buah", onClick = { onQuantitySelected(4) })

                    Spacer(modifier = Modifier.height(16.dp))

                    // Label "Lainnya"
                    Text(
                        text = "Lainnya",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier.align(Alignment.Start)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Input Field "Masukkan Jumlah Sampah"
                    OutlinedTextField(
                        value = customQuantity,
                        onValueChange = { customQuantity = it },
                        placeholder = {
                            Text(
                                "Masukkan Jumlah Sampah",
                                color = Color(0xFFBDBDBD),
                                fontSize = 14.sp
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = DesignTokens.Colors.OrangePrimary,
                            unfocusedBorderColor = DesignTokens.Colors.OrangePrimary,
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black
                        ),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}

@Composable
fun QuantityOptionCard(
    text: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(12.dp))
            .border(1.dp, DesignTokens.Colors.OrangePrimary, RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(vertical = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = DesignTokens.Colors.OrangePrimary,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
    }
}
