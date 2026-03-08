package com.example.raion.ui.features.mission

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.zIndex
import androidx.compose.foundation.Canvas
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.raion.R
import com.example.raion.data.model.MissionUiState
import com.example.raion.data.model.WasteCategory
import com.example.raion.ui.theme.DesignTokens
import com.example.raion.ui.util.formatCompactNumber

// ============================================================
// Shared: Back Arrow Top Bar
// ============================================================
@Composable
private fun MissionTopBar(onBack: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, start = 4.dp)
    ) {
        IconButton(onClick = onBack) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.Black
            )
        }
    }
}

// Shared: Progress bar for wizard steps (2-5)
@Composable
private fun StepProgressBar(currentStep: Int, totalSteps: Int = 4) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        repeat(totalSteps) { i ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(6.dp)
                    .clip(CircleShape)
                    .background(
                        if (i < currentStep) DesignTokens.Colors.OrangePrimary
                        else Color(0xFFE0E0E0)
                    )
            )
        }
    }
}

// Shared: Gobi character image
@Composable
private fun GobiImage(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.dino_yeay),
        contentDescription = "Gobi",
        modifier = modifier.size(200.dp),
        contentScale = ContentScale.Fit
    )
}

// Shared: Selection card
@Composable
private fun SelectionCard(
    label: String,
    isSelected: Boolean = false,
    onClick: () -> Unit
) {
    val borderColor = if (isSelected) DesignTokens.Colors.OrangePrimary else Color(0xFFE0E0E0)
    val bgColor = if (isSelected) Color(0xFFFFF3E6) else Color.White

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(2.dp, borderColor),
        color = bgColor
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

// ============================================================
// STEP 0: Journey Landing
// ============================================================
@Composable
fun JourneyContent(
    state: MissionUiState,
    onStartCollecting: () -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DesignTokens.Colors.CreamBackground)
            .verticalScroll(rememberScrollState())
    ) {
        MissionTopBar(onBack = onBack)

        // Profile header card
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(IntrinsicSize.Min)
        ) {
            // Avatar card — full height, avatar on top, divider, LEVEL at bottom
            Surface(
                modifier = Modifier
                    .width(110.dp)
                    .fillMaxHeight(),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.5.dp, DesignTokens.Colors.TealPrimary.copy(alpha = 0.4f)),
                color = Color.White
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Avatar top section
                    Image(
                        painter = painterResource(id = R.drawable.dinoprofile),
                        contentDescription = "Avatar",
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(8.dp),
                        contentScale = ContentScale.Fit
                    )
                    // Divider
                    HorizontalDivider(
                        color = DesignTokens.Colors.TealPrimary.copy(alpha = 0.3f),
                        thickness = 1.dp
                    )
                    // LEVEL section at bottom
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(DesignTokens.Colors.TealPrimary)
                            .padding(vertical = 5.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "LEVEL ${state.userLevel}",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 11.sp,
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Right side: Name, Class, Badges, XP bar
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Name
                Text(
                    text = state.userName,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 17.sp,
                    color = Color.Black,
                    maxLines = 1
                )
                // Class info
                Text(
                    text = "3 - Sekolah Dasar",
                    fontSize = 12.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(2.dp))

                // Badge pills row
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Peringkat pill
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, DesignTokens.Colors.TealPrimary),
                        color = Color.Transparent
                    ) {
                        Text(
                            text = "Peringkat 5",
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = DesignTokens.Colors.TealPrimary
                        )
                    }
                    // Achieve pill
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, DesignTokens.Colors.TealPrimary),
                        color = Color.Transparent
                    ) {
                        Text(
                            text = "Si Paling Tertib",
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = DesignTokens.Colors.TealPrimary
                        )
                    }
                    // Coins pill
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFFFFDF8D),
                        border = BorderStroke(1.dp, Color(0xFFE5C87A))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("🪙", fontSize = 10.sp)
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = formatCompactNumber(state.userCoins),
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                                color = Color(0xFF8C6200)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))

                // XP progress bar (orange like home)
                val xpProgress = (state.userXp % 100) / 100f
                val xpCurrent = state.userXp % 100
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(10.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE8E8E8)),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(xpProgress.coerceIn(0.02f, 1f))
                                .background(DesignTokens.Colors.OrangePrimary, CircleShape)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "$xpCurrent/100 XP",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.DarkGray
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // GOBI BERAKSI Banner (clip=false for cape overflow)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .graphicsLayer(clip = false)
        ) {
            // Card background + border (drawn separately so image can overflow)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        1.dp,
                        DesignTokens.Colors.TealPrimary.copy(alpha = 0.4f),
                        RoundedCornerShape(20.dp)
                    )
                    .background(Color.White, RoundedCornerShape(20.dp))
            ) {
                Column {
                    // Header dark bar
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Color(0xFF33534A),
                                RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                            )
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "GOBI BERAKSI",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 18.sp,
                            color = Color.White
                        )
                    }

                    // Content area: speech bubble (left) + space for Gobi (right)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .defaultMinSize(minHeight = 160.dp)
                            .padding(start = 16.dp, top = 24.dp, bottom = 32.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Left side: spark + speech bubble
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            // Spark/kilat effect
                            Image(
                                painter = painterResource(id = R.drawable.spark_left),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(28.dp)
                                    .offset(x = 4.dp),
                                contentScale = ContentScale.Fit
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            // Speech bubble with tail
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                // Bubble body
                                Surface(
                                    shape = RoundedCornerShape(14.dp),
                                    color = DesignTokens.Colors.OrangePrimary
                                ) {
                                    Text(
                                        text = if (state.isMissionComplete) "WoHoo!!!" else "Ayo!",
                                        modifier = Modifier.padding(
                                            horizontal = 20.dp,
                                            vertical = 10.dp
                                        ),
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 18.sp,
                                        color = Color.White
                                    )
                                }
                                // Tail triangle pointing right (toward Gobi)
                                Canvas(
                                    modifier = Modifier.size(width = 14.dp, height = 16.dp)
                                ) {
                                    drawPath(
                                        path = Path().apply {
                                            moveTo(0f, size.height * 0.2f)
                                            lineTo(size.width, size.height * 0.5f)
                                            lineTo(0f, size.height * 0.8f)
                                            close()
                                        },
                                        color = DesignTokens.Colors.OrangePrimary
                                    )
                                }
                            }
                        }

                        // Right spacer for Gobi image area
                        Spacer(modifier = Modifier.width(140.dp))
                    }
                }
            }

            // Gobi superhero image — overlaps outside the card
            Image(
                painter = painterResource(id = R.drawable.supergobi),
                contentDescription = "Super Gobi",
                modifier = Modifier
                    .size(240.dp)
                    .align(Alignment.BottomEnd)
                    .offset(x = (-5).dp, y = 30.dp)
                    .zIndex(1f)
                    .graphicsLayer(clip = false),
                contentScale = ContentScale.Fit
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Progress card
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, DesignTokens.Colors.TealPrimary),
            color = Color.White
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Progress count
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, DesignTokens.Colors.TealPrimary),
                    color = Color.White
                ) {
                    Text(
                        text = "${state.scannedCount}/${state.targetCount}",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color.Black
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = if (state.isMissionComplete) "Sampah berhasil terkumpul!"
                    else "Ayo kumpulkan sampahmu!",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = if (state.isMissionComplete) DesignTokens.Colors.TealPrimary else DesignTokens.Colors.OrangePrimary
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Action button
        if (!state.isMissionComplete) {
            Button(
                onClick = onStartCollecting,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(52.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = DesignTokens.Colors.TealPrimary
                )
            ) {
                Text(
                    text = "Mulai Kumpulkan!",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp,
                    color = Color.White
                )
            }
        } else {
            Button(
                onClick = onBack,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(52.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = DesignTokens.Colors.TealPrimary
                )
            ) {
                Text(
                    text = "Selesai!",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}

// ============================================================
// STEP 1: Intro
// ============================================================
@Composable
fun IntroContent(
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DesignTokens.Colors.CreamBackground),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MissionTopBar(onBack = onBack)
        StepProgressBar(currentStep = 0)

        Spacer(modifier = Modifier.weight(1f))

        GobiImage()

        Spacer(modifier = Modifier.height(24.dp))

        // Speech bubble
        Surface(
            modifier = Modifier.padding(horizontal = 40.dp),
            shape = RoundedCornerShape(16.dp),
            color = DesignTokens.Colors.OrangePrimary
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Ayo ceritakan aksi barumu!",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Sudah siap menceritakan aksi penyelamatan bumi hari ini?",
                    fontSize = 13.sp,
                    color = Color.White.copy(alpha = 0.9f),
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onNext,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .height(52.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = DesignTokens.Colors.TealPrimary)
        ) {
            Text("Selanjutnya", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

// ============================================================
// STEP 2: Select Waste Type
// ============================================================
@Composable
fun SelectTypeContent(
    onSelect: (String) -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DesignTokens.Colors.CreamBackground),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MissionTopBar(onBack = onBack)
        StepProgressBar(currentStep = 1)

        Spacer(modifier = Modifier.height(16.dp))

        GobiImage(modifier = Modifier.size(150.dp))

        // Speech bubble question
        Surface(
            modifier = Modifier.padding(horizontal = 32.dp),
            shape = RoundedCornerShape(16.dp),
            color = DesignTokens.Colors.OrangePrimary
        ) {
            Text(
                text = "Sampah jenis apa yang kamu buang hari ini?",
                modifier = Modifier.padding(16.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Options
        Column(
            modifier = Modifier.padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SelectionCard(label = "🍃  Sampah Organik") { onSelect("organik") }
            SelectionCard(label = "♻️  Sampah Daur Ulang") { onSelect("daur_ulang") }
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}

// ============================================================
// STEP 3: Select Subtype
// ============================================================
@Composable
fun SelectSubtypeContent(
    categories: List<WasteCategory>,
    selectedType: String,
    onSelect: (String) -> Unit,
    onBack: () -> Unit
) {
    val filteredCategories = categories.filter { it.wasteType == selectedType }
    val title = if (selectedType == "organik") "Sampah Organik" else "Sampah Daur Ulang"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DesignTokens.Colors.CreamBackground),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MissionTopBar(onBack = onBack)
        StepProgressBar(currentStep = 2)

        Spacer(modifier = Modifier.height(16.dp))

        GobiImage(modifier = Modifier.size(150.dp))

        Surface(
            modifier = Modifier.padding(horizontal = 32.dp),
            shape = RoundedCornerShape(16.dp),
            color = DesignTokens.Colors.OrangePrimary
        ) {
            Text(
                text = "$title jenis apa?",
                modifier = Modifier.padding(16.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Column(
            modifier = Modifier.padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (filteredCategories.isNotEmpty()) {
                filteredCategories.forEach { cat ->
                    val emoji = when (cat.subtype) {
                        "buah" -> "🍎"
                        "sayur" -> "🥬"
                        "sisa_makanan" -> "🍚"
                        "kaleng" -> "🥫"
                        "kertas" -> "📄"
                        "plastik" -> "🧴"
                        else -> "📦"
                    }
                    SelectionCard(label = "$emoji  Sampah ${cat.subtype.replaceFirstChar { it.uppercase() }}") {
                        onSelect(cat.subtype)
                    }
                }
            } else {
                // Fallback if categories not loaded
                if (selectedType == "organik") {
                    SelectionCard(label = "🍎  Sampah Buah") { onSelect("buah") }
                    SelectionCard(label = "🥬  Sampah Sayur") { onSelect("sayur") }
                    SelectionCard(label = "🍚  Sisa Makanan") { onSelect("sisa_makanan") }
                } else {
                    SelectionCard(label = "🥫  Kaleng") { onSelect("kaleng") }
                    SelectionCard(label = "📄  Kertas") { onSelect("kertas") }
                    SelectionCard(label = "🧴  Plastik") { onSelect("plastik") }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}

// ============================================================
// STEP 4: Select Location
// ============================================================
@Composable
fun SelectLocationContent(
    onSelect: (String) -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DesignTokens.Colors.CreamBackground),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MissionTopBar(onBack = onBack)
        StepProgressBar(currentStep = 3)

        Spacer(modifier = Modifier.height(16.dp))

        GobiImage(modifier = Modifier.size(150.dp))

        Surface(
            modifier = Modifier.padding(horizontal = 32.dp),
            shape = RoundedCornerShape(16.dp),
            color = DesignTokens.Colors.OrangePrimary
        ) {
            Text(
                text = "Di tempat sampah yang terletak dimana?",
                modifier = Modifier.padding(16.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 2x2 Grid
        Column(
            modifier = Modifier.padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                LocationCard("🍽️", "Kantin", Modifier.weight(1f)) { onSelect("kantin") }
                LocationCard("📚", "Ruang Kelas", Modifier.weight(1f)) { onSelect("ruang_kelas") }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                LocationCard("🌳", "Halaman", Modifier.weight(1f)) { onSelect("halaman") }
                LocationCard("🚻", "Toilet", Modifier.weight(1f)) { onSelect("toilet") }
            }
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
private fun LocationCard(emoji: String, label: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Surface(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(2.dp, Color(0xFFE0E0E0)),
        color = Color.White
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(emoji, fontSize = 28.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = label,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
        }
    }
}

// ============================================================
// STEP 5: Select Quantity
// ============================================================
@Composable
fun SelectQuantityContent(
    isLoading: Boolean,
    onSelect: (Int) -> Unit,
    onBack: () -> Unit
) {
    var customQuantity by remember { mutableStateOf("") }
    var showCustomInput by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DesignTokens.Colors.CreamBackground),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MissionTopBar(onBack = onBack)
        StepProgressBar(currentStep = 4)

        Spacer(modifier = Modifier.height(16.dp))

        GobiImage(modifier = Modifier.size(150.dp))

        Surface(
            modifier = Modifier.padding(horizontal = 32.dp),
            shape = RoundedCornerShape(16.dp),
            color = DesignTokens.Colors.OrangePrimary
        ) {
            Text(
                text = "Berapa jumlah sampah yang kamu buang?",
                modifier = Modifier.padding(16.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.padding(32.dp),
                color = DesignTokens.Colors.TealPrimary
            )
        } else {
            // Quantity options: 1-4
            Column(
                modifier = Modifier.padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    QuantityCard("1 Buah", Modifier.weight(1f)) { onSelect(1) }
                    QuantityCard("2 Buah", Modifier.weight(1f)) { onSelect(2) }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    QuantityCard("3 Buah", Modifier.weight(1f)) { onSelect(3) }
                    QuantityCard("4 Buah", Modifier.weight(1f)) { onSelect(4) }
                }

                // Custom input
                if (showCustomInput) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = customQuantity,
                            onValueChange = { customQuantity = it.filter { c -> c.isDigit() } },
                            label = { Text("Jumlah") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Button(
                            onClick = {
                                val qty = customQuantity.toIntOrNull()
                                if (qty != null && qty > 0) onSelect(qty)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = DesignTokens.Colors.TealPrimary),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("OK", fontWeight = FontWeight.Bold)
                        }
                    }
                } else {
                    SelectionCard(label = "✏️  Lainnya") { showCustomInput = true }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
private fun QuantityCard(label: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Surface(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(2.dp, Color(0xFFE0E0E0)),
        color = Color.White
    ) {
        Box(
            modifier = Modifier.padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.Black
            )
        }
    }
}

// ============================================================
// STEP 6: Result
// ============================================================
@Composable
fun ResultContent(
    state: MissionUiState,
    onContinue: () -> Unit,
    onExit: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DesignTokens.Colors.CreamBackground)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MissionTopBar(onBack = onExit)

        Spacer(modifier = Modifier.height(16.dp))

        if (state.isMissionComplete) {
            // === MISI SELESAI ===
            Text(
                text = "Misi Selesai!",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 28.sp,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(16.dp))

            GobiImage()

            Spacer(modifier = Modifier.height(20.dp))

            // Reward badges
            Text(
                text = "Hadiah Imbalan:",
                fontSize = 14.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // XP Badge
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = DesignTokens.Colors.TealPrimary
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "+${state.totalGainedXp} XP",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 16.sp,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("↑", fontSize = 14.sp, color = Color.White)
                    }
                }

                // Coins Badge
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(2.dp, Color(0xFFFFDF8D)),
                    color = Color.White
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "+${state.totalGainedCoins}",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 16.sp,
                            color = Color(0xFF8C6200)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("🪙", fontSize = 14.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Fun fact
            Column(
                modifier = Modifier.padding(horizontal = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Tahukah Kamu?",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 20.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Sampah organik bisa diolah menjadi kompos yang menyuburkan tanaman.",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Keren Sekali ya!",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = DesignTokens.Colors.OrangePrimary
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = onExit,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .height(52.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DesignTokens.Colors.TealPrimary)
            ) {
                Text("Simpan Progres", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
            }
        } else {
            // === BELUM SELESAI ===
            Spacer(modifier = Modifier.height(40.dp))

            Image(
                painter = painterResource(id = R.drawable.dino_tanya),
                contentDescription = "Gobi bingung",
                modifier = Modifier.size(200.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Misi Kamu Belum\nSelesai Nih!",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 24.sp,
                color = Color.Black,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Ayo selesaikan misi hari ini agar\nmendapat hadiah imbalan untuk aksi kerenmu!",
                fontSize = 14.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp)
            )

            // Show error message if RPC failed
            if (state.errorMessage != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFFFEBEE)
                ) {
                    Text(
                        text = "⚠️ ${state.errorMessage}",
                        fontSize = 12.sp,
                        color = Color(0xFFB71C1C),
                        modifier = Modifier.padding(12.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Continue button
            Button(
                onClick = onContinue,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .height(52.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DesignTokens.Colors.TealPrimary)
            ) {
                Text("Lanjutkan!", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Exit button
            OutlinedButton(
                onClick = onExit,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .height(52.dp),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, DesignTokens.Colors.TealPrimary)
            ) {
                Text(
                    "Keluar",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp,
                    color = DesignTokens.Colors.TealPrimary
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}
