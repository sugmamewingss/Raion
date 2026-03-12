package com.example.raion.ui.features.story

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
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
    viewModel: StoryViewModel = hiltViewModel(),
    onNavigateToEpisode: (String) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    
    // State untuk memicu animasi dino ngintip
    var isPeeking by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf<String?>(null) } // State for Custom Dialog

    LaunchedEffect(Unit) {
        delay(800) // Jeda awal sebelum dino ngintip
        isPeeking = true
    }

    Box(modifier = Modifier.fillMaxSize()) {
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

            if (uiState.isLoading) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color(0xFFA87042))
                    }
                }
            } else if (uiState.error != null) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                        Text(text = "Error: ${uiState.error}", color = Color.Red)
                    }
                }
            } else {
                items(uiState.chapters) { chapter ->
                    ChapterSection(
                        chapterTitle = chapter.title,
                        episodes = chapter.episodes,
                        onEpisodeClick = { episode -> onNavigateToEpisode(episode.id) },
                        onLockedEpisodeClick = { episode ->
                            if (episode.contentImageUrl == null) {
                                dialogMessage = "EPISODE INI BELUM TERSEDIA"
                            } else {
                                dialogMessage = "BACA EPISODE SEBELUMNYA UNTUK MEMBUKA"
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
        
        // Dino Ngintip (Peeking Mascot)
        AnimatedVisibility(
            visible = isPeeking,
            enter = slideInHorizontally(
                initialOffsetX = { fullWidth -> fullWidth }, // Slide masuk penuh dari ujung luar kanan (+X)
                animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing)
            ),
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_dino_hi),
                contentDescription = "Peeking Dino",
                modifier = Modifier
                    .width(95.dp) // Ukuran diperkecil agar lebih proporsional dan tidak menutupi konten
                    .padding(end = 0.dp), // Mepet garis layar
                contentScale = ContentScale.Fit
            )
        }
        
        // Custom Dialog Pop-up
        if (dialogMessage != null) {
            androidx.compose.ui.window.Dialog(onDismissRequest = { dialogMessage = null }) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFFFEFDF1), // Latar krem
                    modifier = Modifier.padding(horizontal = 16.dp).wrapContentHeight()
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(24.dp).fillMaxWidth()
                    ) {
                        Text(
                            text = dialogMessage ?: "",
                            color = Color(0xFFECA05A), // Teks oranye
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 18.sp,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(
                            onClick = { dialogMessage = null },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFECA05A)),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.size(width = 120.dp, height = 48.dp)
                        ) {
                            Text("OK", color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
                        }
                    }
                }
            }
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
            painter = painterResource(id = R.drawable.img_supergobi_journey),
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
    episodes: List<UiEpisode>,
    onEpisodeClick: (UiEpisode) -> Unit,
    onLockedEpisodeClick: (UiEpisode) -> Unit
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
                EpisodeCard(
                    episode = episode,
                    onClick = { onEpisodeClick(episode) },
                    onLockedClick = { onLockedEpisodeClick(episode) }
                )
            }
        }
    }
}

@Composable
fun EpisodeCard(episode: UiEpisode, onClick: () -> Unit, onLockedClick: () -> Unit = {}) {
    Card(
        modifier = Modifier
            .width(140.dp)
            .height(200.dp)
            .clickable { 
                if (!episode.isLocked) onClick() 
                else onLockedClick()
            },
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
                if (episode.coverImageUrl != null) {
                    AsyncImage(
                        model = episode.coverImageUrl,
                        contentDescription = episode.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize().background(Color.LightGray),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "No Image", fontSize = 10.sp)
                    }
                }
                
                if (episode.isLocked) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.4f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
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
                            // Show "Coming Soon" if there's no content link
                            if (episode.contentImageUrl == null) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Surface(
                                    color = Color(0xFF1C533F).copy(alpha = 0.9f),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = "COMING SOON",
                                        color = Color.White,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
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

// Removed dummy data class Episode

// Note: Removing Preview for now because it requires a ViewModel or mock instance
/*
@Preview(showBackground = true)
@Composable
fun StoryScreenPreview() {
    com.example.raion.ui.theme.RaionTheme {
        StoryScreen()
    }
}
*/
