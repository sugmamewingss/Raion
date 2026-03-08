package com.example.raion.ui.features.story

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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

@Composable
fun StoryDetailScreen(
    chapterTitle: String = "Bab 1",
    episodeTitle: String = "Si Trex",
    episodeSubtitle: String = "Episode 1",
    imageRes: Int = R.drawable.bab_one_eps_one,
    hasNextEpisode: Boolean = true,
    hasPreviousEpisode: Boolean = false,
    onNavigateBack: () -> Unit,
    onNextLevel: () -> Unit,
    onPreviousLevel: () -> Unit = {},
    onFinish: () -> Unit = {}
) {
    var commentText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DesignTokens.Colors.CreamBackground)
    ) {
        // Sticky Top App Bar
        TopAppBarArea(onNavigateBack = onNavigateBack)

        // Scrollable Content
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            // Header Judul Cerita
            item {
                StoryHeader(
                    chapterTitle = chapterTitle,
                    episodeTitle = episodeTitle,
                    episodeSubtitle = episodeSubtitle
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Gambar Komik Panajang (Satu Gambar Utuh)
            item {
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = "Komik Cerita $episodeTitle",
                    contentScale = ContentScale.FillWidth, // Biar lebarnya nyoba nge-fit layer
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Aksi Bawah & Komentar
            item {
                BottomActionsArea(
                    hasNextEpisode = hasNextEpisode,
                    hasPreviousEpisode = hasPreviousEpisode,
                    onNextLevel = onNextLevel,
                    onPreviousLevel = onPreviousLevel,
                    onFinish = onFinish,
                    commentText = commentText,
                    onCommentChanged = { commentText = it }
                )
            }
        }
    }
}

@Composable
fun TopAppBarArea(onNavigateBack: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = DesignTokens.Dimensions.PaddingMedium, vertical = 16.dp),
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Kembali",
            tint = Color(0xFFA87042),
            modifier = Modifier
                .size(32.dp)
                .clickable { onNavigateBack() }
                .align(Alignment.CenterStart)
        )
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
                    modifier = Modifier.height(44.dp),
                    shape = RoundedCornerShape(50),
                    contentPadding = PaddingValues(horizontal = 24.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1C533F))
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp).padding(end = 6.dp)
                    )
                    Text(
                        text = "Kembali",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
            } else {
                Spacer(modifier = Modifier.width(44.dp)) // Penyeimbang jika tombol kiri kosong
            }

            // Tombol Lanjut / Selesai
            Button(
                onClick = if (hasNextEpisode) onNextLevel else onFinish,
                modifier = Modifier.height(44.dp),
                shape = RoundedCornerShape(50),
                contentPadding = PaddingValues(horizontal = 24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1C533F))
            ) {
                Text(
                    text = if (hasNextEpisode) "Lanjut" else "Selesai",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    modifier = Modifier.padding(end = 6.dp)
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
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

@Preview(showBackground = true)
@Composable
fun StoryDetailScreenPreview() {
    com.example.raion.ui.theme.RaionTheme {
        StoryDetailScreen(
            onNavigateBack = {},
            onNextLevel = {}
        )
    }
}
