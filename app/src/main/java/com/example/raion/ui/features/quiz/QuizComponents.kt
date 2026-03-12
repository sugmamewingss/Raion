package com.example.raion.ui.features.quiz

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.raion.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizTopNavBar(title: String = "", onBackClick: () -> Unit) {
    TopAppBar(
        title = {
            if (title.isNotEmpty()) {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.Black
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Black
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        )
    )
}

@Composable
fun Badge(text: String, bgColor: Color, textColor: Color, isCoin: Boolean = false) {
    Surface(
        shape = RoundedCornerShape(4.dp),
        border = BorderStroke(1.dp, textColor.copy(alpha = 0.5f)),
        color = bgColor
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
            Text(
                text = text,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
            if (isCoin) {
                Spacer(modifier = Modifier.width(2.dp))
                Image(
                    painter = painterResource(id = R.drawable.ic_gold),
                    contentDescription = "Coin",
                    modifier = Modifier.size(12.dp)
                )
            }
        }
    }
}

@Composable
fun LockedPlaceholderCard() {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFFD6D6D6), // Light structural grey
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = "Locked Placeholder",
                tint = Color.White,
                modifier = Modifier.size(36.dp)
            )
        }
    }
}

@Composable
fun InfoBox(
    title: String,
    subtitle: String,
    iconRes: Int,
    badges: @Composable () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color(0xFF1D5C42)), // Green border
        color = Color.White,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = "$title Icon",
                modifier = Modifier.size(34.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.DarkGray
                )
                Spacer(modifier = Modifier.height(6.dp))
                badges()
            }
        }
    }
}

@Composable
fun MascotBubbleCard(
    imageRes: Int = R.drawable.img_dino_thinking,
    text: String = "Sudah siap\nuntuk\nmengerjakan\nTantangan\nJenius, Sobat\nGobi?"
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color(0xFF1D5C42)), // Dark Green Border
        color = Color.White,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Text Container
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 28.dp, bottom = 28.dp, start = 160.dp, end = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = text,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Mascot Image (Left)
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = "Mascot",
                modifier = Modifier
                    .width(170.dp)
                    .align(Alignment.BottomStart)
                    .offset(x = 4.dp, y = 4.dp), // y = 4.dp mendorong paksa gambar turun sehingga amblas ke batas Surface (ter-clip outline)
                contentScale = ContentScale.FillWidth,
                alignment = Alignment.BottomCenter
            )
        }
    }
}

@Composable
fun GenericListCard(
    title: String,
    subtitle: String,
    iconRes: Int,
    bonusXp: Int = 0,
    bonusCoins: Int = 0,
    bgColor: Color,
    borderColor: Color,
    isLocked: Boolean = false,
    actionButton: @Composable (() -> Unit)? = null,
    onClick: () -> Unit = {}
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, borderColor),
        color = bgColor,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = !isLocked, onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = "Icon",
                modifier = Modifier.size(56.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Details Column
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.DarkGray
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Badges Row
                if (bonusXp > 0 || bonusCoins > 0) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        if (bonusXp > 0) {
                            Badge(text = "+$bonusXp XP", bgColor = Color(0xFFD9F1FF), textColor = Color(0xFF2C84C7))
                        }
                        if (bonusXp > 0 && bonusCoins > 0) {
                            Spacer(modifier = Modifier.width(6.dp))
                        }
                        if (bonusCoins > 0) {
                            Badge(text = "+$bonusCoins", bgColor = Color(0xFFFFECB3), textColor = Color(0xFFD69400), isCoin = true)
                        }
                    }
                }
            }

            // Action Button Right
            if (actionButton != null) {
                actionButton()
            } else {
                Surface(
                    shape = CircleShape,
                    border = BorderStroke(1.dp, if (isLocked) Color(0xFFC97C7C) else Color(0xFFC0A261)),
                    color = Color.White,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .clickable(enabled = !isLocked, onClick = onClick)
                ) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        if (isLocked) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Locked",
                                tint = Color.Black,
                                modifier = Modifier.size(18.dp)
                            )
                        } else {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = "Enter",
                                tint = Color.Black,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
