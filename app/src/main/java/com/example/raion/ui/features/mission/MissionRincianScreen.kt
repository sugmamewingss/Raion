package com.example.raion.ui.features.mission

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import com.example.raion.R

@Composable
fun MissionRincianScreen(
    totalXp: Int,
    totalCoins: Int,
    onNavigateBack: () -> Unit,
    onStartMission: () -> Unit
) {
    val darkGreenColor = Color(0xFF1B4F45)
    val darkGreenButton = Color(0xFF568F7B)
    val lightYellowCard = Color(0xFFFCF6ED)
    val lightBlueBadge = Color(0xFFDFF1FD)
    val lightBlueOutline = Color(0xFF67B0E8)
    val lightYellowBadge = Color(0xFFFEF3DF)
    val lightYellowOutline = Color(0xFFDBA854)
    val goldCoinColor = Color(0xFFF1C40F) // Coin circle color
    
    // Background tint
    val backgroundColor = Color(0xFFFFFDF5) // Very light yellow/creamy background

    Scaffold(
        containerColor = backgroundColor,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier
                        .size(32.dp)
                        .clickable { onNavigateBack() },
                    tint = Color.Black
                )
                
                Spacer(modifier = Modifier.weight(1f))
                
                Text(
                    text = "RINCIAN",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.Black
                )
                
                Spacer(modifier = Modifier.weight(1f))
                
                // Invisible spacer for centering
                Spacer(modifier = Modifier.size(32.dp))
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            
            // 1. Hero Card (Target Misi)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, darkGreenColor, RoundedCornerShape(12.dp))
                    .background(Color.White)
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.Bottom
                ) {
                    // Mascot (Left)
                    Image(
                        painter = painterResource(id = R.drawable.dinodetailmisi2),
                        contentDescription = "Mascot Detail Misi 2",
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .padding(top = 16.dp),
                        contentScale = ContentScale.Fit
                    )
                    
                    // Info Column (Right)
                    Column(
                        modifier = Modifier
                            .weight(1.2f)
                            .fillMaxHeight()
                            .padding(top = 24.dp, end = 16.dp),
                        verticalArrangement = Arrangement.Top
                    ) {
                        Text(
                            text = "TARGET MISI",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.Black
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Text(
                            text = "Kamu hari ini harus dapat hadiah imbalan:",
                            fontSize = 12.sp,
                            color = Color.DarkGray,
                            lineHeight = 16.sp
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Badges Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // XP Badge
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(36.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(lightBlueBadge)
                                    .border(1.dp, lightBlueOutline, RoundedCornerShape(4.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "$totalXp XP",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = lightBlueOutline
                                )
                            }
                            
                            // Coin Badge
                            Row(
                                modifier = Modifier
                                    .weight(1.2f)
                                    .height(36.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(lightYellowBadge)
                                    .border(1.dp, lightYellowOutline, RoundedCornerShape(4.dp)),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "$totalCoins",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF8C6420) // Darker gold text
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                // Custom Coin Icon
                                Box(
                                    modifier = Modifier
                                        .size(20.dp)
                                        .background(goldCoinColor, androidx.compose.foundation.shape.CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "🪙",
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // 2. Content Card (Peranmu & Caranya)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, darkGreenColor, RoundedCornerShape(12.dp))
                    .background(Color.White)
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    // Header Area (Green)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(darkGreenColor)
                            .padding(vertical = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Kumpulkan Sampah Di Sekitarmu!",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    
                    // Body Text Area - Wrapped in VerticalScroll if content is too long
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp)
                    ) {
                        // PERANMU Label
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(backgroundColor)
                                .border(1.dp, darkGreenColor, RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "PERANMU",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.Black
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Text(
                            text = "Sekali membuang sampah mampu menyelamatkan beruang kutub dari kepunahan, serta menjaga keseimbangan ekosistem bumi untuk masa depan.",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Black,
                            textAlign = TextAlign.Justify,
                            lineHeight = 20.sp
                        )
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        // CARANYA Label
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(backgroundColor)
                                .border(1.dp, darkGreenColor, RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "CARANYA",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.Black
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Text(
                            text = "Pernah dengar? Sebaiknya bersihkan sampah dahulu sebelum kamu membuangnya, supaya tidak mencemari lingkungan di sekitarmu.",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Black,
                            textAlign = TextAlign.Justify,
                            lineHeight = 20.sp
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // 3. Bottom Action Button
            Button(
                onClick = onStartMission,
                colors = ButtonDefaults.buttonColors(
                    containerColor = darkGreenButton,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(
                    text = "Mulai Misi!",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Preview
@Composable
fun MissionRincianScreenPreview() {
    MissionRincianScreen(
        totalXp = 50,
        totalCoins = 100,
        onNavigateBack = {},
        onStartMission = {}
    )
}
