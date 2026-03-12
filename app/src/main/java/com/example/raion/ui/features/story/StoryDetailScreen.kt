package com.example.raion.ui.features.story

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.raion.R
import com.example.raion.ui.theme.DesignTokens
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import kotlinx.coroutines.launch

@Composable
fun StoryDetailScreen(
    episodeId: String,
    viewModel: StoryViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onNextLevel: (String) -> Unit, // pass next episode ID
    onPreviousLevel: (String) -> Unit = {}, // pass prev episode ID
    onFinish: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    var commentText by remember { mutableStateOf("") }
    var isSubmitting by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    
    // Popup State & Navigation Deferring
    var rewardPopupData by remember { mutableStateOf<com.example.raion.data.model.StoryRewardResponse?>(null) }
    var pendingNavigation by remember { mutableStateOf<(() -> Unit)?>(null) }
    
    // Find the current episode
    var currentEpisode: UiEpisode? = null
    var currentChapter: UiChapter? = null
    var nextEpisodeId: String? = null
    var prevEpisodeId: String? = null
    
    uiState.chapters.forEach { chapter ->
        val epIndex = chapter.episodes.indexOfFirst { it.id == episodeId }
        if (epIndex != -1) {
            currentEpisode = chapter.episodes[epIndex]
            currentChapter = chapter
            
            // Safe prev/next inside the same chapter
            // Stop at beginning of the chapter (no cross-chapter back-navigation)
            if (epIndex > 0) {
                prevEpisodeId = chapter.episodes[epIndex - 1].id
            } else {
                prevEpisodeId = null
            }
            
            if (epIndex < chapter.episodes.size - 1) {
                nextEpisodeId = chapter.episodes[epIndex + 1].id
            } else {
                // To stop at the end of the chapter and show "Selesai", we simply set this to null.
                nextEpisodeId = null
            }
        }
    }
    
    val episode = currentEpisode
    if (episode == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = Color(0xFFA87042))
        }
        return
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DesignTokens.Colors.CreamBackground)
    ) {
        // Scrollable Content
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(
                top = 64.dp + WindowInsets.statusBars.asPaddingValues().calculateTopPadding(),
                bottom = 32.dp
            )
        ) {
            // Header Judul Cerita
            item {
                StoryHeader(
                    chapterTitle = currentChapter?.title ?: "Bab",
                    episodeTitle = episode.title,
                    episodeSubtitle = "Episode ${episode.number}"
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Gambar Komik Panajang (Satu Gambar Utuh)
            item {
                if (episode.contentImageUrl != null) {
                    AsyncImage(
                        model = episode.contentImageUrl,
                        contentDescription = "Komik Cerita ${episode.title}",
                        contentScale = ContentScale.FillWidth, // Biar lebarnya nyoba nge-fit layer
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    Box(
                        modifier = Modifier.fillMaxWidth().height(400.dp).background(Color.LightGray),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Belum Ada Komik", fontSize = 16.sp, color = Color.DarkGray)
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                BottomActionsArea(
                    hasNextEpisode = nextEpisodeId != null,
                    hasPreviousEpisode = prevEpisodeId != null,
                    isSubmitting = isSubmitting,
                    onNextLevel = {
                        coroutineScope.launch {
                            isSubmitting = true
                            val result = viewModel.markEpisodeCompleted(episode.id)
                            isSubmitting = false
                            if (result != null && result.grantedXp > 0) {
                                rewardPopupData = result
                                pendingNavigation = { nextEpisodeId?.let { onNextLevel(it) } }
                            } else {
                                nextEpisodeId?.let { onNextLevel(it) }
                            }
                        }
                    },
                    onPreviousLevel = { prevEpisodeId?.let { onPreviousLevel(it) } },
                    onFinish = {
                        coroutineScope.launch {
                            isSubmitting = true
                            val result = viewModel.markEpisodeCompleted(episode.id)
                            isSubmitting = false
                            if (result != null && result.grantedXp > 0) {
                                rewardPopupData = result
                                pendingNavigation = { onFinish() }
                            } else {
                                onFinish()
                            }
                        }
                    },
                    commentText = commentText,
                    onCommentChanged = { commentText = it }
                )
            }
        }

        // Floating Back Button Overlay
        TopAppBarArea(
            modifier = Modifier.align(Alignment.TopStart),
            onNavigateBack = onNavigateBack
        )
        
        // Popup Overlay Render
        rewardPopupData?.let { reward ->
            StoryCompletePopup(
                xp = reward.grantedXp,
                coins = reward.grantedCoins,
                onDismiss = {
                    rewardPopupData = null
                    pendingNavigation?.invoke()
                    pendingNavigation = null
                }
            )
        }
    }
}

@Composable
fun TopAppBarArea(modifier: Modifier = Modifier, onNavigateBack: () -> Unit) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                top = 16.dp + WindowInsets.statusBars.asPaddingValues().calculateTopPadding(),
                start = DesignTokens.Dimensions.PaddingMedium,
                end = DesignTokens.Dimensions.PaddingMedium
            )
    ) {
        // Use Surface with CircleShape or standard IconButton for proper circular ripple
        Surface(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .size(44.dp),
            shape = CircleShape,
            color = Color.White.copy(alpha = 0.5f), // Semi-transparent backing so it's visible over comics
            onClick = onNavigateBack
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Kembali",
                    tint = Color(0xFFA87042),
                    modifier = Modifier.size(28.dp) // The actual drawn Arrow icon size
                )
            }
        }
    }
}

@Composable
fun StoryHeader(
    chapterTitle: String,
    episodeTitle: String,
    episodeSubtitle: String
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = chapterTitle,
            color = Color(0xFFA87042),
            fontWeight = FontWeight.ExtraBold,
            fontSize = 20.sp
        )
        Text(
            text = episodeTitle,
            color = Color(0xFF1C533F),
            fontWeight = FontWeight.ExtraBold,
            fontSize = 32.sp
        )
        Text(
            text = episodeSubtitle,
            color = Color(0xFF1C533F),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
    }
}

@Composable
fun BottomActionsArea(
    hasNextEpisode: Boolean,
    hasPreviousEpisode: Boolean,
    isSubmitting: Boolean,
    onNextLevel: () -> Unit,
    onPreviousLevel: () -> Unit,
    onFinish: () -> Unit,
    commentText: String,
    onCommentChanged: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = DesignTokens.Dimensions.PaddingLarge)
    ) {
        // Baris Tombol Navigasi Bawah
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween, // Pisahkan tombol ke kiri dan kanan
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Tombol Kembali (Hanya tampil jika ada episode sebelumnya)
            if (hasPreviousEpisode) {
                Button(
                    onClick = onPreviousLevel,
                    modifier = Modifier.width(130.dp).height(44.dp),
                    shape = RoundedCornerShape(50),
                    contentPadding = PaddingValues(0.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1C533F))
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Kembali",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
            } else {
                Spacer(modifier = Modifier.width(130.dp)) // Penyeimbang jika tombol kiri kosong
            }

            // Tombol Lanjut / Selesai
            Button(
                onClick = if (hasNextEpisode) onNextLevel else onFinish,
                modifier = Modifier.width(130.dp).height(44.dp),
                shape = RoundedCornerShape(50),
                contentPadding = PaddingValues(0.dp),
                enabled = !isSubmitting,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1C533F))
            ) {
                if (isSubmitting) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = if (hasNextEpisode) "Lanjut" else "Selesai",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Label Komentar
        Text(
            text = "Komentar",
            color = Color.Black,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 20.sp
        )
        
        Spacer(modifier = Modifier.height(8.dp))

        // Input Komentar dengan Underline custom
        TextField(
            value = commentText,
            onValueChange = onCommentChanged,
            placeholder = { 
                Text(
                    text = "Tulisan Komentar", 
                    color = Color.Gray.copy(alpha = 0.8f)
                ) 
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Gray,
                unfocusedIndicatorColor = Color.LightGray,
                cursorColor = Color(0xFF1C533F)
            ),
            singleLine = true
        )
    }
}

@Composable
fun StoryCompletePopup(
    xp: Int,
    coins: Int,
    onDismiss: () -> Unit
) {
    androidx.compose.ui.window.Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Bab Selesai!",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFFA87042)
                )
                Spacer(modifier = Modifier.height(16.dp))
                
                // Maskot
                Image(
                    painter = painterResource(id = R.drawable.img_dino_daily),
                    contentDescription = "Gobi Congratulates",
                    modifier = Modifier.size(140.dp),
                    contentScale = ContentScale.Fit
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Hebat sekali! Kumpulan episode ini sudah tamat. Sebagai hadiah menamatkan satu babak cerita, ini untukmu!",
                    fontSize = 14.sp,
                    color = Color.DarkGray,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    lineHeight = 20.sp
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Badge XP
                    Box(
                        modifier = Modifier
                            .background(Color(0xFFD9F1FF), RoundedCornerShape(8.dp))
                            .border(1.dp, Color(0xFF8BB5ED), RoundedCornerShape(8.dp))
                            .padding(horizontal = 16.dp, vertical = 10.dp)
                    ) {
                        Text("+$xp XP", color = Color(0xFF2C84C7), fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    
                    // Badge Coins
                    Box(
                        modifier = Modifier
                            .background(Color(0xFFFFECB3), RoundedCornerShape(8.dp))
                            .border(1.dp, Color(0xFFDCA855), RoundedCornerShape(8.dp))
                            .padding(horizontal = 16.dp, vertical = 10.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("+$coins ", color = Color(0xFFD69400), fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
                            Image(
                                painter = painterResource(id = R.drawable.ic_gold),
                                contentDescription = "Coins",
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                Button(
                    onClick = onDismiss,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = DesignTokens.Colors.TealPrimary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    Text("Terima Kasih!", color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
                }
            }
        }
    }
}

