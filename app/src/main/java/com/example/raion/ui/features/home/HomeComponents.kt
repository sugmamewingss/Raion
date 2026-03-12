package com.example.raion.ui.features.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import coil.compose.SubcomposeAsyncImage
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.ui.draw.shadow
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontStyle
import com.example.raion.R
import com.example.raion.data.model.ActiveMission
import com.example.raion.data.model.EduArticle
import com.example.raion.data.model.UserProfile
import com.example.raion.data.model.PointShopItem
import com.example.raion.ui.theme.DesignTokens
import com.example.raion.ui.util.formatCompactNumber

@Composable
fun TopProfileSection(
    userName: String,
    streak: Int,
    isActive: Boolean,
    progressText: String,
    progressRatio: Float,
    coins: Int,
    level: Int,
    avatarUrl: String = "",
    isMissionCompletedToday: Boolean = false
) {
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp)) {
        // BARIS UTAMA: Profil (Kiri) & Lencana (Kanan)
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Kontainer Kiri: Avatar + Info (Teks & Streak)
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Avatar
                Box(
                    modifier = Modifier.size(65.dp),
                    contentAlignment = Alignment.Center
                ) {
                    val imageToLoad = if (avatarUrl.isNotEmpty()) avatarUrl else "https://nnloirkwladlazxgpgrm.supabase.co/storage/v1/object/public/avatars/dino_default.png"
                    
                    SubcomposeAsyncImage(
                        model = imageToLoad,
                        contentDescription = "Avatar",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .border(1.dp, Color.LightGray, CircleShape),
                        contentScale = ContentScale.Crop,
                        loading = {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = DesignTokens.Colors.OrangePrimary,
                                strokeWidth = 2.dp
                            )
                        },
                        error = {
                            Image(
                                painter = painterResource(id = R.drawable.img_dino_default),
                                contentDescription = "Avatar default",
                                modifier = Modifier.fillMaxSize().clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        }
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))

                // Info
                Column(verticalArrangement = Arrangement.Center) {
                    Text(
                        text = buildAnnotatedString {
                            append("Selamat datang ")
                            withStyle(SpanStyle(fontWeight = FontWeight.ExtraBold)) {
                                append(userName)
                            }
                            append("!")
                        },
                        fontSize = 15.sp,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    
                    // Baris untuk Badge di bawah nama
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // 1. Kotak Streak (Api)
                        val streakColor = if (isMissionCompletedToday) Color(0xFFFF5722) else Color(0xFFC0B8B0) // Oranye menyala vs Abu redup
                        val streakBgColor = if (isMissionCompletedToday) Color(0xFFFFEBE3) else Color(0xFFF1F0E9)
                        val streakBorderColor = if (isMissionCompletedToday) Color(0xFFFFCCBB) else Color(0xFFE4DFD8)
                        
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, streakBorderColor),
                            color = streakBgColor
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_fire_streak),
                                    contentDescription = "Streak",
                                    modifier = Modifier.height(14.dp),
                                    contentScale = ContentScale.Fit,
                                    colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(streakColor)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = streak.toString(),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = streakColor
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.width(8.dp))
                        
                        // 2. Status Pill (Online/Active)
                        val activeColor = if (isActive) Color(0xFF4CAF50) else Color.Gray // Hijau Online
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, activeColor.copy(alpha = 0.5f)),
                            color = activeColor.copy(alpha = 0.1f)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(activeColor)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (isActive) "Aktif" else "Offline",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = activeColor
                                )
                            }
                        }
                    }
                }
            }

            // Kontainer Kanan: Lencana Buatan Sendiri (Custom Shapes)
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // 1. Lencana Koin (Kotak Kuning dengan Ikon Koin)
                Box(
                    modifier = Modifier
                        .height(34.dp)
                        .padding(horizontal = 4.dp)
                        .background(Color(0xFFFFDF8D), RoundedCornerShape(8.dp))
                        .border(1.dp, Color(0xFFE5C87A), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(horizontal = 10.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_gold),
                            contentDescription = "Coin",
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            formatCompactNumber(coins),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF8C6200) // Warna cokelat gelap
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // 2. Lencana Level (Bentuk Perisai Abstrak Oranye)
                Box(
                    modifier = Modifier
                        .width(52.dp)
                        .height(34.dp),
                    contentAlignment = Alignment.Center
                ) {
                    val orangeColor = DesignTokens.Colors.OrangePrimary
                    val shadowColor = Color.LightGray
                    val strokeColor = Color(0xFFC05900) // Garis luar perisai lebih gelap
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
                        // Draw shadow
                        drawPath(path, color = shadowColor, alpha = 0.5f)
                        // Draw shape
                        drawPath(path, color = orangeColor)
                        // Draw stroke
                        drawPath(path, color = strokeColor, style = androidx.compose.ui.graphics.drawscope.Stroke(width = 1.dp.toPx()))
                    }
                    Text(
                        text = buildAnnotatedString {
                            withStyle(SpanStyle(fontSize = 7.sp)) {
                                append("LEVEL\n")
                            }
                            withStyle(SpanStyle(fontSize = 12.sp)) {
                                append(level.toString())
                            }
                        },
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        lineHeight = 11.sp,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(top = 2.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // BARIS BAWAH: Zona Progress Bar & Teks Inline
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Progress Bar Tipis
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(8.dp)
                    .background(Color(0xFFEEEEEE), CircleShape)
                    .clip(CircleShape),
                contentAlignment = Alignment.CenterStart
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(progressRatio.coerceIn(0f, 1f))
                        .background(DesignTokens.Colors.OrangePrimary, CircleShape)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            // Teks Progress di kanan bar
            Text(
                text = progressText,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray
            )
        }
    }
}

// --- KOMPONEN: Kartu Misi Bertumpuk ---
@Composable
fun ActiveMissionCard(mission: ActiveMission?, onStartMission: () -> Unit = {}) {
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp)) {
        // Teks Header Luar
        Text(
            text = "Misi Gobi",
            fontSize = 18.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, DesignTokens.Colors.BrandPrimary),
            color = Color.White
        ) {
            Column {
                // Bagian Atas: Info Misi & Gambar Overlap
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Content Kiri (Tulisan)
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, top = 20.dp, bottom = 12.dp, end = 120.dp) // Beri ruang End untuk maskot
                    ) {
                        Text(
                            text = "Misi yang belum terselesaikan :",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.Black
                        )
                        
                        Spacer(modifier = Modifier.height(10.dp))
                        
                        if (mission == null) {
                            Text(
                                text = "Hore! Kamu sudah menyelesaikan semua misi hari ini.",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = DesignTokens.Colors.TextSecondary
                            )
                            Spacer(modifier = Modifier.height(30.dp))
                        } else {
                            // Point Bullet Misi
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier.size(6.dp).clip(CircleShape).background(DesignTokens.Colors.BrandPrimary)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = mission.title,
                                    fontSize = 12.sp,
                                    color = Color.Black
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Progress Kapsul (Cream/Orange)
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .background(Color(0xFFFFF1E4), RoundedCornerShape(6.dp))
                                    .border(1.dp, Color(0xFFFFD1B3), RoundedCornerShape(6.dp))
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_trash), // Fallback, you can change this to trash bin icon later
                                    contentDescription = "Trash",
                                    tint = DesignTokens.Colors.BrandSecondary,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "${mission.currentProgress}/${mission.targetProgress}",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = DesignTokens.Colors.BrandSecondary
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Teks deskripsi italic (sesuai desain)
                            Text(
                                text = "Buanglah sampah pada tempatnya ya!",
                                fontSize = 10.sp,
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                fontWeight = FontWeight.Bold,
                                color = Color.Gray
                            )
                        }
                    }

                    // Gambar Karakter Gobi Boba / Tong Sampah Dinamis (ditengah vertical)
                    val trashIconId = if (mission == null) {
                        R.drawable.img_trash_bin_5 // Jika tidak ada misi (selesai semua), tong penuh
                    } else {
                        when (mission.currentProgress) {
                            0 -> R.drawable.img_trash_bin_0
                            1 -> R.drawable.img_trash_bin_1
                            2 -> R.drawable.img_trash_bin_2
                            3 -> R.drawable.img_trash_bin_3
                            4 -> R.drawable.img_trash_bin_4
                            else -> R.drawable.img_trash_bin_5
                        }
                    }

                    Image(
                        painter = painterResource(id = trashIconId),
                        contentDescription = "Mascot Trash Progress",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .width(80.dp)
                            .height(100.dp)
                            .align(Alignment.CenterEnd)
                            .offset(x = (-12).dp)
                    )
                }

                // Bagian Bawah: Batang Gelap (Footer)
                val footerBgColor = Color(0xFF33534A) // Menyerupai BrandDark / Teal pekat
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = footerBgColor,
                            shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
                        )
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Kumpulkan sampahmu disini, Sobat Gobi!",
                        fontSize = 11.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    )

                    Button(
                        onClick = onStartMission,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = DesignTokens.Colors.BrandDark
                        ),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp, pressedElevation = 0.dp),
                        border = BorderStroke(1.dp, Color.Black.copy(alpha = 0.20f)),
                        contentPadding = PaddingValues(0.dp),
                        modifier = Modifier
                            .height(32.dp)
                            .width(100.dp),
                        shape = RoundedCornerShape(50)
                    ) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            // Inner shadow (bevel) at bottom — same as auth buttons
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .fillMaxWidth()
                                    .height(4.dp)
                                    .background(Color.Black.copy(alpha = 0.08f))
                            )
                            // Text centered
                            Text(
                                text = "Mulai Misi!",
                                modifier = Modifier.align(Alignment.Center),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = DesignTokens.Colors.BrandDark
                            )
                        }
                    }
                }
            }
        }
    }
}

// --- KOMPONEN: Quick Nav Menu ---
@Composable
fun QuickNavMenu(onItemClick: (Int) -> Unit = {}) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        QuickNavItem(
            label = "Detail Misi",
            iconRes = R.drawable.ic_magnifying_glass,
            bgColor = Color(0xFFD7BFFD),        // Lilac pastel
            borderColor = Color(0xFFB89EDE),     // Darker lilac
            onClick = { onItemClick(0) }
        )
        QuickNavItem(
            label = "Buku Harian",
            iconRes = R.drawable.img_daily_books,
            bgColor = Color(0xFF8DD8C4),         // Teal soft
            borderColor = Color(0xFF6BBFAB),     // Darker teal
            onClick = { onItemClick(1) }
        )
        QuickNavItem(
            label = "Tantangan Jenius",
            iconRes = R.drawable.ic_genuine_task,
            bgColor = Color(0xFFFFB3B6),         // Pink pastel
            borderColor = Color(0xFFE8969A),     // Darker pink
            onClick = { onItemClick(2) }
        )
    }
}

@Composable
fun QuickNavItem(
    label: String,
    iconRes: Int,
    bgColor: Color,
    borderColor: Color,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Surface(
            modifier = Modifier.size(100.dp),
            shape = RoundedCornerShape(26.dp),
            color = bgColor,
            border = BorderStroke(2.dp, borderColor)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                // Inner shadow (bevel) at bottom — raised/timbul effect
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .height(6.dp)
                        .background(Color.Black.copy(alpha = 0.08f))
                )
                // Icon centered
                Image(
                    painter = painterResource(id = iconRes),
                    contentDescription = label,
                    modifier = Modifier
                        .size(52.dp)
                        .align(Alignment.Center),
                    contentScale = ContentScale.Fit
                )
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}

// --- KOMPONEN: Carousel Artikel Edukasi ---
@Composable
fun EducationalCarousel(articles: List<EduArticle>) {
    if (articles.isEmpty()) return

    val pagerState = rememberPagerState(pageCount = { articles.size })

    // Card frame — raw Box for zero hidden padding
    val cardShape = RoundedCornerShape(DesignTokens.Dimensions.CornerRadiusMedium)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp)
            .clip(cardShape)
            .background(Color.White, cardShape)
            .border(1.dp, DesignTokens.Colors.TealPrimary, cardShape)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // HorizontalPager — hanya isi (gambar + teks) yang bergeser
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) { page ->
                val article = articles[page]

                Column(modifier = Modifier.fillMaxSize()) {
                    // Image section
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp)
                    ) {
                        val imageToLoad = if (!article.imageUrl.isNullOrEmpty()) article.imageUrl else "https://nnloirkwladlazxgpgrm.supabase.co/storage/v1/object/public/avatars/dino_default.png"
                        
                        coil.compose.SubcomposeAsyncImage(
                            model = imageToLoad,
                            contentDescription = "Banner Edukasi",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop,
                            loading = {
                                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(30.dp),
                                        color = DesignTokens.Colors.TealPrimary,
                                        strokeWidth = 2.dp
                                    )
                                }
                            },
                            error = {
                                Image(
                                    painter = painterResource(id = R.drawable.img_trash_1),
                                    contentDescription = "Fallback Banner",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        )
                        // Tag badge
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(start = 12.dp, bottom = 10.dp)
                                .background(Color(0xFF3D3D4E), RoundedCornerShape(50))
                                .padding(horizontal = 14.dp, vertical = 5.dp)
                        ) {
                            Text(
                                text = article.tag,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                        }
                    }

                    // Text section
                    Column(
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 14.dp)
                    ) {
                        Text(
                            text = buildAnnotatedString {
                                val splitTxt = article.title.split(" ")
                                if (splitTxt.size > 1) {
                                    withStyle(SpanStyle(color = Color.Black)) {
                                        append(splitTxt[0] + " ")
                                    }
                                    withStyle(SpanStyle(
                                        color = DesignTokens.Colors.TealPrimary,
                                        fontStyle = FontStyle.Italic
                                    )) {
                                        append(splitTxt.drop(1).dropLast(1).joinToString(" "))
                                    }
                                    val lastWord = splitTxt.last()
                                    if (!lastWord.first().isLetter()) {
                                        append(lastWord)
                                    } else {
                                        withStyle(SpanStyle(color = DesignTokens.Colors.TealPrimary, fontStyle = FontStyle.Italic)) {
                                            append(" $lastWord")
                                        }
                                    }
                                } else {
                                    append(article.title)
                                }
                            },
                            fontSize = 17.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.Black
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = article.subtitle,
                            fontSize = 11.sp,
                            color = DesignTokens.Colors.TextSecondary,
                            lineHeight = 16.sp,
                            maxLines = 3
                        )
                    }
                }
            }

            // Pagination dots — FIXED
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 16.dp, bottom = 12.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(articles.size) { i ->
                    val dotColor = if (pagerState.currentPage == i) DesignTokens.Colors.TealPrimary else Color.LightGray
                    val dotWidth = if (pagerState.currentPage == i) 20.dp else 6.dp
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 2.dp)
                            .clip(CircleShape)
                            .background(dotColor)
                            .height(6.dp)
                            .width(dotWidth)
                    )
                }
            }
        }
    }
}

// --- KOMPONEN: Leaderboard Papan Peringkat ---
@Composable
fun LeaderboardSection(leaderboard: List<UserProfile>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        // Section Header
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .border(1.dp, DesignTokens.Colors.TealPrimary, RoundedCornerShape(8.dp))
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.BarChart,
                    contentDescription = "Leaderboard",
                    tint = Color.Black
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Papan Peringkat",
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.Black
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Rank Items
        leaderboard.forEachIndexed { index, user ->
            RankItemCard(user = user, rank = index + 1)
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun RankItemCard(user: UserProfile, rank: Int) {
    val bgColor = when (rank) {
        1 -> Color(0xFF3D6B5E)                // Dark teal
        2 -> DesignTokens.Colors.OrangePrimary // Soft orange
        3 -> Color(0xFF8B8B8B)                // Medium gray
        else -> Color.LightGray
    }

    val achieveText = when (rank) {
        1 -> "Si paling bersih"
        2 -> "Si paling rajin"
        3 -> "Si paling rapi"
        else -> "Pejuang Bumi"
    }

    val rankStr = when (rank) {
        1 -> "1st"
        2 -> "2nd"
        3 -> "3th"
        else -> "${rank}th"
    }

    val avatarSize = 80.dp
    val cardHeight = avatarSize
    val cardShape = RoundedCornerShape(
        topStart = cardHeight / 2,    // Circular left, follows avatar
        bottomStart = cardHeight / 2,
        topEnd = 16.dp,               // Normal rounded right
        bottomEnd = 16.dp
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(cardHeight)
            .clip(cardShape)
    ) {
        // Top half — colored
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f)
                .align(Alignment.TopCenter)
                .background(bgColor)
        )
        // Bottom half — white
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f)
                .align(Alignment.BottomCenter)
                .background(Color.White)
        )

        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar inside card, at left
            Box(modifier = Modifier.size(avatarSize)) {
                // Gray circle background
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFE8E8E8), CircleShape)
                )
                val fallbackUrl = "https://nnloirkwladlazxgpgrm.supabase.co/storage/v1/object/public/avatars/dino_default.png"
                val imageToLoad = if (!user.currentAvatarUrl.isNullOrEmpty()) user.currentAvatarUrl else fallbackUrl
                
                SubcomposeAsyncImage(
                    model = imageToLoad,
                    contentDescription = "Rank $rank Avatar",
                    modifier = Modifier
                        .size(avatarSize - 6.dp)
                        .align(Alignment.Center)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop,
                    loading = {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = DesignTokens.Colors.OrangePrimary,
                                strokeWidth = 2.dp
                            )
                        }
                    },
                    error = {
                        Image(
                            painter = painterResource(id = R.drawable.img_dino_default),
                            contentDescription = "Rank $rank Avatar Default",
                            modifier = Modifier.fillMaxSize().clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }
                )
                // Level badge — bottom-right, shield shape
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .offset(x = (-2).dp, y = (-4).dp)
                        .size(26.dp),
                    contentAlignment = Alignment.Center
                ) {
                    val orangeColor = DesignTokens.Colors.OrangePrimary
                    val strokeColor = Color(0xFFC05900)
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
                        text = user.level.toString(),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Text content
            Column(modifier = Modifier.weight(1f)) {
                // Top: Name + XP
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 64.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = (user.name.takeIf { it.isNotBlank() } ?: user.username).split(" ").first(),
                        fontSize = 17.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        maxLines = 1,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${user.totalXp} XP",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                // Bottom: Achieve
                Text(
                    text = buildAnnotatedString {
                        append("Achieve: ")
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(achieveText)
                        }
                    },
                    fontSize = 11.sp,
                    color = Color.Black
                )
            }
        }

        // Rank circle — straddling boundary between colored & white
        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .offset(x = (-12).dp)
                .size(52.dp)
                .background(Color.White, CircleShape)
                .border(2.dp, bgColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = rankStr,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = bgColor
            )
        }
    }
}

// --- KOMPONEN: Toko Poin (Point Shop) ---
@Composable
fun PointShopSection(shopItems: List<PointShopItem>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        // Section Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.White)
                        .border(1.dp, DesignTokens.Colors.TealPrimary, RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Storefront,
                        contentDescription = "Shop",
                        tint = Color.Black
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Tukar Poin",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.Black
                )
            }
            Text(
                text = "Lihat Semua",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = DesignTokens.Colors.TextSecondary
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Horizontal Scrollable Shop Catalog
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(end = 16.dp) // padding to avoid exact cutoffs at the edge
        ) {
            items(shopItems) { item ->
                ShopItemCard(item = item)
            }
            
            // Dummy Data Fallback (untuk testing bila backend kosong)
            if (shopItems.isEmpty()) {
                items(3) { index ->
                    ShopItemCard(
                        item = PointShopItem(
                            itemId = "dummy_$index",
                            name = "Merchandise Gobi ${index+1}",
                            imageUrl = "dummy_url",
                            price = (index+1) * 10
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun ShopItemCard(item: PointShopItem) {
    Box(
        modifier = Modifier
            .width(140.dp)
            .height(160.dp)
            .background(Color.White, RoundedCornerShape(16.dp))
            .border(1.dp, DesignTokens.Colors.LightGrayBorder, RoundedCornerShape(16.dp))
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Bagian Gambar Baju/Merchandise (Top Box)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(
                        Color(0xFFFAFAFA),
                        RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                coil.compose.SubcomposeAsyncImage(
                    model = item.imageUrl,
                    contentDescription = item.name,
                    modifier = Modifier.size(80.dp),
                    contentScale = ContentScale.Fit,
                    loading = {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp), color = DesignTokens.Colors.TealPrimary, strokeWidth = 2.dp)
                    },
                    error = {
                        Image(
                            painter = painterResource(id = R.drawable.img_dino_face),
                            contentDescription = item.name,
                            modifier = Modifier.size(80.dp),
                            contentScale = ContentScale.Fit
                        )
                    }
                )
                // Love Icon top right
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(24.dp)
                        .background(Color.White, CircleShape)
                        .border(1.dp, Color(0xFFE0E0E0), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.FavoriteBorder,
                        contentDescription = "Like",
                        tint = Color.Gray,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }

            // Bagian Bawah Hijau (Deskripsi dan Harga)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        DesignTokens.Colors.TealPrimary,
                        RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
                    )
                    .padding(vertical = 8.dp, horizontal = 12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF4A9C83), RoundedCornerShape(8.dp)) // Darker green inner box
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_gold),
                            contentDescription = "Coin",
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = com.example.raion.ui.util.formatCompactNumber(item.price),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
                // Garis putih kecil di bawah tombol sebagai ornamen (Opsional)
                Box(
                    modifier = Modifier
                        .width(40.dp)
                        .height(2.dp)
                        .background(Color.White, RoundedCornerShape(50))
                        .align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}
