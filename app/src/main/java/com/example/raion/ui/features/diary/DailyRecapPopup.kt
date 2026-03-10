package com.example.raion.ui.features.diary

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.raion.R
import com.example.raion.ui.theme.DesignTokens

@Composable
fun DailyRecapPopup(
    isVisible: Boolean,
    userName: String,
    userLevel: Int,
    missionsCompleted: Int,
    storiesRead: Int,
    totalXp: Int,
    totalCoins: Int,
    onClose: () -> Unit
) {
    if (!isVisible) return

    val darkGreenColor = Color(0xFF33534A)
    val buttonColor = Color(0xFF6BBFAB)
    val lightGreenCard = Color(0xFFE8F6F1)
    val lightBlueCard = Color(0xFFE8F4FE)
    val lightYellowCard = Color(0xFFFFF7E6)

    // Overlay Box: Intercepts clicks to close when tapping outside
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .clickable(onClick = onClose),
        contentAlignment = Alignment.Center
    ) {
        // Prevent clicks on the card from bubbling up to close the popup
        Box(
            modifier = Modifier
                .clickable(enabled = false, onClick = {}) 
                .padding(horizontal = 24.dp)
        ) {
            
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Container for Card and Mascot
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.TopCenter
                ) {
                    // Main White Card (drawn first so mascot sits on top)
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White,
                        border = androidx.compose.foundation.BorderStroke(1.dp, darkGreenColor),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 110.dp) // Space for mascot overlap
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp)
                        ) {
                            Spacer(modifier = Modifier.height(30.dp)) // Buffer under mascot hands
                        
                        // 1. User Info Header
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // Avatar
                            Box(
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(CircleShape)
                                    .border(2.dp, Color.LightGray, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.dinoprofile),
                                    contentDescription = "Avatar",
                                    modifier = Modifier.size(46.dp).clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                            }
                            
                            Spacer(modifier = Modifier.width(16.dp))
                            
                            // Name & Level
                            Column {
                                Text(
                                    text = userName,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.Black
                                )
                                Text(
                                    text = "$userLevel - Sekolah Dasar",
                                    fontSize = 14.sp,
                                    color = Color.DarkGray
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        // 2. Main Stats Area (Green Outline Box)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(140.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            
                            // Left Column: Quests Completed (Inside Outline)
                            Column(
                                modifier = Modifier
                                    .weight(1.5f)
                                    .fillMaxHeight()
                                    .background(Color.White, RoundedCornerShape(8.dp))
                                    .border(1.dp, darkGreenColor, RoundedCornerShape(8.dp))
                                    .padding(horizontal = 16.dp, vertical = 12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Quiz dan Modul Terselesaikan",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = darkGreenColor,
                                    textAlign = TextAlign.Center
                                )
                                
                                Spacer(modifier = Modifier.weight(1f))
                                
                                // Missions Completed Badge
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(lightGreenCard, RoundedCornerShape(4.dp))
                                        .border(1.dp, Color(0xFF88C9B9), RoundedCornerShape(4.dp))
                                        .padding(vertical = 8.dp),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Check",
                                        tint = buttonColor,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "$missionsCompleted Misi Selesai",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = buttonColor
                                    )
                                }
                                
                                Spacer(modifier = Modifier.height(8.dp))
                                
                                // Story Read Badge
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(lightBlueCard, RoundedCornerShape(4.dp))
                                        .border(1.dp, Color(0xFF8BB5ED), RoundedCornerShape(4.dp))
                                        .padding(vertical = 8.dp),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Check",
                                        tint = Color(0xFF3F82DB),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "$storiesRead Cerita dibaca",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF3F82DB)
                                    )
                                }
                            }
                            
                            Spacer(modifier = Modifier.width(16.dp))
                            
                            // Right Column: Rewards
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight(),
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                // XP Badge
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f)
                                        .background(Color(0xFFDFF1FD), RoundedCornerShape(6.dp))
                                        .border(1.dp, Color(0xFF8BB5ED), RoundedCornerShape(6.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "$totalXp XP",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color(0xFF5A9DDF)
                                    )
                                }
                                
                                Spacer(modifier = Modifier.height(16.dp))
                                
                                // Coin Badge
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f)
                                        .background(lightYellowCard, RoundedCornerShape(6.dp))
                                        .border(1.dp, Color(0xFFDCA855), RoundedCornerShape(6.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "$totalCoins ",
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = Color(0xFF916A42)
                                        )
                                        Text("🪙", fontSize = 16.sp)
                                    }
                                }
                            }
                        }
                    }
                } // <--- Closes Surface
                    
                // Mascot (drawn exactly after Surface to be on top)
                Image(
                        painter = painterResource(id = R.drawable.dinodaily),
                        contentDescription = "Mascot",
                        modifier = Modifier
                            .width(220.dp)
                            .height(190.dp) // Adjust size so it sticks out of the 110.dp padding
                            .offset(x = 20.dp, y = (-80).dp), // Shift upwards by 5dp more as requested
                        contentScale = ContentScale.Fit
                    )
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                // 3. Okey Button
                Button(
                    onClick = onClose,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = buttonColor,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.size(width = 140.dp, height = 50.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        text = "Okey!",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Preview
@Composable
fun DailyRecapPopupPreview() {
    Box(modifier = Modifier.fillMaxSize()) {
        DailyRecapPopup(
            isVisible = true,
            userName = "Kevin Aditya Pratama",
            userLevel = 3,
            missionsCompleted = 15,
            storiesRead = 1,
            totalXp = 1500,
            totalCoins = 250,
            onClose = {}
        )
    }
}
