package com.example.raion.ui.features.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.example.raion.ui.theme.DesignTokens
import com.example.raion.ui.util.formatCompactNumber

// --- 1. Header ---
@Composable
fun ProfileHeader(userName: String) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Profil",
            fontSize = 16.sp,
            color = Color.DarkGray,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = userName,
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.Black
        )
    }
}

// --- 2. Avatar Section ---
@Composable
fun ProfileAvatarSection(coinBalance: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .background(Color(0xFFCEEBE1)) // Soft Teal/Cyan Background from design
    ) {
        // Hanger Button (Top Left)
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
                .size(40.dp)
                .background(Color.Transparent, RoundedCornerShape(8.dp))
                .border(1.dp, Color(0xFF88C9B9), RoundedCornerShape(8.dp))
                .clickable { /* TBD: Open Wardrobe */ },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Checkroom,
                contentDescription = "Wardrobe",
                modifier = Modifier.size(24.dp),
                tint = Color(0xFF33534A) // Dark teal color for icon
            )
        }

        // Coin Badge (Top Right)
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .height(30.dp)
                .background(Color(0xFFFFDF8D), RoundedCornerShape(8.dp))
                .border(1.dp, Color(0xFFE5C87A), RoundedCornerShape(8.dp))
                .padding(horizontal = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("🪙", fontSize = 12.sp) // Custom icon coin
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = formatCompactNumber(coinBalance),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF8C6200)
                )
            }
        }

        // Mascot Output (Center Bottom)
        Image(
            painter = painterResource(id = R.drawable.dinoprofile), // Re-using dino image
            contentDescription = "Hero Avatar",
            modifier = Modifier
                .size(200.dp)
                .align(Alignment.BottomCenter)
                .offset(y = 10.dp), // Slightly drop it down
            contentScale = ContentScale.Fit
        )
        
        // Bottom stroke border divider as a separator
        Box(
           modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth().height(1.dp).background(Color(0xFF88C9B9))
        )
    }
}

// --- 3. Full Info Section ---
@Composable
fun ProfileInfoSection(
    fullName: String,
    joinDate: String,
    level: Int,
    xpText: String,
    xpRatio: Float
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = fullName,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = DesignTokens.Colors.TealPrimary
                )
                Text(
                    text = joinDate,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = DesignTokens.Colors.TealPrimary.copy(alpha = 0.8f) // Slightly faded
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Hexagon Level Badge (Reusing home logic but bigger)
            Box(
                modifier = Modifier
                    .width(60.dp)
                    .height(40.dp),
                contentAlignment = Alignment.Center
            ) {
                val orangeColor = DesignTokens.Colors.OrangePrimary
                val strokeColor = Color(0xFFD67320)
                androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
                    val path = androidx.compose.ui.graphics.Path().apply {
                        moveTo(size.width / 2, 0f)
                        lineTo(size.width, size.height * 0.25f)
                        lineTo(size.width, size.height * 0.75f)
                        lineTo(size.width / 2, size.height)
                        lineTo(0f, size.height * 0.75f)
                        lineTo(0f, size.height * 0.25f)
                        close()
                    }
                    drawPath(path, color = orangeColor)
                    drawPath(path, color = strokeColor, style = androidx.compose.ui.graphics.drawscope.Stroke(width = 1.dp.toPx()))
                }
                Text(
                    text = buildAnnotatedString {
                        withStyle(SpanStyle(fontSize = 8.sp)) {
                            append("LEVEL\n")
                        }
                        withStyle(SpanStyle(fontSize = 14.sp)) {
                            append(level.toString())
                        }
                    },
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    lineHeight = 12.sp,
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // XP Progress Bar Segment
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(10.dp)
                    .background(Color(0xFFE2E2E2), CircleShape)
                    .clip(CircleShape),
                contentAlignment = Alignment.CenterStart
            ) {
                // Outer bar shadow simulation
                Box(modifier = Modifier.fillMaxSize().border(1.dp, Color(0xFFCCCCCC), CircleShape))
                // Inner progress
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(xpRatio.coerceIn(0f, 1f))
                        .background(DesignTokens.Colors.OrangePrimary, CircleShape)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = xpText,
                fontSize = 12.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.Black
            )
        }
    }
}

// --- 4. Edit Profile Button ---
@Composable
fun EditProfileButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFF5F5F5), // Light Gray
            contentColor = Color.Black
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        shape = RoundedCornerShape(24.dp),
        contentPadding = PaddingValues(0.dp) // Reset padding for custom shadow sizing
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .border(1.dp, Color(0xFFC0C0C0), RoundedCornerShape(24.dp))
        ) {
            // Inset bottom shadow simulation
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(6.dp)
                    .background(Color(0xFFD6D6D6)) // Darker bottom edge
            )
            
            Row(
                modifier = Modifier.align(Alignment.Center),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit",
                    modifier = Modifier.size(16.dp),
                    tint = Color.Black
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "EDIT PROFIL",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.Black
                )
            }
        }
    }
}

// --- 5. Streak Retention Section ---
@Composable
fun StreakRetentionCard(streak: Int, onTaskClick: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = Color(0xFFFFEEDB), // Super light soft orange
        border = BorderStroke(2.dp, Color(0xFFFFCC99)) // Soft orange border
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left Column
                Column {
                    Text(
                        text = "Kerja bagus!",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = DesignTokens.Colors.OrangePrimary
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = onTaskClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DesignTokens.Colors.OrangePrimary,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(50),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp),
                        modifier = Modifier
                            .height(34.dp)
                    ) {
                         Text(
                             text = "Kerjakan Tugas",
                             fontSize = 11.sp,
                             fontWeight = FontWeight.Bold
                         )
                    }
                }

                // Right Column (Streak Fire)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = streak.toString(),
                        fontSize = 54.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = DesignTokens.Colors.OrangePrimary
                    )
                    Image(
                        painter = painterResource(id = R.drawable.fire_streak),
                        contentDescription = "Fire Streak",
                        modifier = Modifier.size(44.dp),
                        contentScale = ContentScale.Fit,
                        colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(DesignTokens.Colors.OrangePrimary)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Day Tracker
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val days = listOf("Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu", "Minggu")
                
                // --- Dynamic Calendar Logic ---
                // 1. Get current day of week (Monday = 1, Sunday = 7)
                // In java.util.Calendar: Sunday = 1, Monday = 2, ... Saturday = 7
                val calendar = java.util.Calendar.getInstance()
                val currentDayOfWeek = calendar.get(java.util.Calendar.DAY_OF_WEEK)
                
                // Convert to 0-indexed Monday-based array (0 = Mon, ..., 6 = Sun)
                val currentDayIndex = if (currentDayOfWeek == java.util.Calendar.SUNDAY) 6 else currentDayOfWeek - 2
                
                // We show this week.
                // Lit days are `streak` consecutive days ending on today (currentDayIndex) 
                // OR ending on yesterday if the user hasn't opened app/done mission today yet
                // For simplicity assuming the streak is active up to today.
                // If streak = 3, and today is Wednesday (index 2), then Mon, Tue, Wed are lit (indices 0, 1, 2).
                
                days.forEachIndexed { index, dayName ->
                    // A day is lit if its index is within the last `streak` days ending at `currentDayIndex`
                    // We only light up days in the current week (index <= currentDayIndex)
                    // If streak spans to previous week, we still light up from Monday (index 0) up to currentDayIndex
                    val isPastOrToday = index <= currentDayIndex
                    val daysAgo = currentDayIndex - index
                    val isLit = isPastOrToday && daysAgo < streak
                    
                    val dayColor = if (isLit) DesignTokens.Colors.OrangePrimary else Color(0xFFC4C4C4)
                    
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = dayName,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = dayColor
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Image(
                            painter = painterResource(id = R.drawable.fire_streak),
                            contentDescription = "Fire",
                            modifier = Modifier.size(16.dp),
                            colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(dayColor)
                        )
                    }
                }
            }
        }
    }
}

// --- 6. Monthly Badges ---
@Composable
fun MonthlyBadgesSection(onSeeAllClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Lencana Bulanan",
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.Black
            )
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Seluruhnya",
                modifier = Modifier.clickable { onSeeAllClick() },
                tint = Color.Black
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Three badges
            Image(
                painter = painterResource(id = R.drawable.badge_1),
                contentDescription = "Badge 1",
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFFF0F0))
                    .border(2.dp, Color(0xFFFFCCCC), CircleShape)
            )
            Image(
                painter = painterResource(id = R.drawable.badge_2),
                contentDescription = "Badge 2",
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE6F7F2))
                    .border(2.dp, Color(0xFFB3E6D5), CircleShape)
            )
            Image(
                painter = painterResource(id = R.drawable.badge_3),
                contentDescription = "Badge 3",
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE8F4FE))
                    .border(2.dp, Color(0xFFACDAFB), CircleShape)
            )
        }
    }
}

// --- 7. Logout Button ---
@Composable
fun LogoutButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = Color(0xFFD32F2F) // Red warning color
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.dp, Color(0xFFEF9A9A))
    ) {
        Text(
            text = "Keluar",
            fontSize = 15.sp,
            fontWeight = FontWeight.ExtraBold
        )
    }
}
