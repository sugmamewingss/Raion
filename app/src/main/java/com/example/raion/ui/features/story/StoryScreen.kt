package com.example.raion.ui.features.story

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import kotlinx.coroutines.delay
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.raion.R
import com.example.raion.ui.theme.DesignTokens

@Composable
fun StoryScreen(
    onNavigateToEpisode: (Int) -> Unit = {}
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(DesignTokens.Colors.CreamBackground),
        contentPadding = PaddingValues(bottom = 100.dp) // Jarak ekstra untuk BottomNavBar, sama dgn HomeScreen
    ) {
        item {
            HeaderBanner(
                modifier = Modifier.padding(horizontal = DesignTokens.Dimensions.PaddingLarge)
            )
            Spacer(modifier = Modifier.height(32.dp))
        }

        val ch1Episodes = listOf(
            Episode(1, 1, "Si Trex", R.drawable.onboarding_1, false), 
            Episode(2, 2, "Peduli", R.drawable.onboarding_2, false) // Kita ubah jadi false agar bisa diklik buat tes
        )
        item {
            ChapterSection(
                chapterTitle = "Bab 1 : Buang Sampah Sembarangan",
                episodes = ch1Episodes,
                onEpisodeClick = { episode -> onNavigateToEpisode(episode.globalId) }
            )
            Spacer(modifier = Modifier.height(24.dp))
        }

        val ch2Episodes = listOf(
            Episode(3, 1, "Museum", R.drawable.onboarding_3, true)
        )
        item {
            ChapterSection(
                chapterTitle = "Bab 2 : Raja Daur ulang?",
                episodes = ch2Episodes,
                onEpisodeClick = { episode -> onNavigateToEpisode(episode.globalId) }
            )
        }
    }
}

@Composable
fun HeaderBanner(modifier: Modifier = Modifier) {
    var isAnimated by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(150) // Jeda sedikit sebelum animasi mulai
        isAnimated = true
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(top = 16.dp)
    ) {
        // Kotak pembungkus untuk membatasi animasi di sebelah kanan maskot
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 70.dp) // Titik awal animasi dari balik maskot
                .fillMaxWidth()
                .height(70.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            // Karena kita mau ada efek "menggulung" (memanjang ke kanan) 
            // TAPi ujung kanannya harus ADA LENGKUNGAN,
            // Kita bisa memanfaatkan animasi nilai float (width percentage)
            val animatedWidth by animateFloatAsState(
                targetValue = if (isAnimated) 1f else 0f,
                animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing),
                label = "banner_width"
            )

            if (animatedWidth > 0f) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth(0.95f * animatedWidth) // Animasikan lebar permukaannya secara langsung
                        .height(70.dp),
                    shape = RoundedCornerShape(16.dp), // Semua ujung membulat
                    color = Color(0xFFF29B4A),
                    border = BorderStroke(2.dp, Color(0xFFC77732))
                ) {
                    // Agar teks tidak terpotong-potong/terkompresi saat lebar berubah,
                    // Kita bungkus dengan Box yang punya lebar fix, dan letakkan di tengah (Center)
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clipToBounds(), // Pastikan isinya tidak bocor keluar dari surface
                        contentAlignment = Alignment.Center
                    ) {
                        // Teks diletakkan di dalam container selebar penuh banner yang belum dianimasi,
                        // dengan sedikit padding kiri agar tidak tertutup maskot.
                         Text(
                            text = "Baca Cerita Yuk!",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            maxLines = 1,
                            modifier = Modifier
                                .requiredWidth(250.dp) // Ukuran tetap agar text tidak "menciut", di-center secara utuh
                                .padding(start = 24.dp), // Padding tambahan untuk mengompensasi maskot yg nutupin kiri
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
        
        // Mascot Image Overlapping
        Image(
            painter = painterResource(id = R.drawable.supergobi_journey),
            contentDescription = "Mascot",
            modifier = Modifier
                .align(Alignment.BottomStart)
                .size(140.dp) // Maskot diperbesar
                .offset(x = (0).dp, y = (7).dp),
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
fun ChapterSection(
    chapterTitle: String,
    episodes: List<Episode>,
    onEpisodeClick: (Episode) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = chapterTitle,
            color = Color(0xFFA87042),
            fontWeight = FontWeight.ExtraBold,
            fontSize = 18.sp,
            modifier = Modifier.padding(horizontal = DesignTokens.Dimensions.PaddingLarge)
        )
        Spacer(modifier = Modifier.height(16.dp))
        LazyRow(
            contentPadding = PaddingValues(horizontal = DesignTokens.Dimensions.PaddingLarge),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(episodes) { episode ->
                EpisodeCard(episode = episode, onClick = { onEpisodeClick(episode) })
            }
        }
    }
}

@Composable
fun EpisodeCard(episode: Episode, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(140.dp)
            .height(200.dp)
            .clickable { if (!episode.isLocked) onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.7f)
            ) {
                Image(
                    painter = painterResource(id = episode.imageRes),
                    contentDescription = episode.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                
                if (episode.isLocked) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.4f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFF333333).copy(alpha = 0.8f),
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Locked",
                                tint = Color.LightGray,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                }
            }
            
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.3f)
                    .padding(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Episode ${episode.number}",
                    fontSize = 11.sp,
                    color = Color.DarkGray,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = episode.title,
                    fontSize = 16.sp,
                    color = Color(0xFF1C533F),
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

data class Episode(
    val globalId: Int,
    val number: Int,
    val title: String,
    val imageRes: Int,
    val isLocked: Boolean
)

@Preview(showBackground = true)
@Composable
fun StoryScreenPreview() {
    com.example.raion.ui.theme.RaionTheme {
        StoryScreen()
    }
}
