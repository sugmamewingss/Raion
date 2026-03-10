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
import com.example.raion.ui.features.auth.components.RegisterHeader
import com.example.raion.ui.features.auth.components.WaveBackground
import com.example.raion.ui.util.formatCompactNumber

// ============================================================
// Shared: Mission Header (matches Register style)
// ============================================================
@Composable
private fun MissionHeader(progress: Float, onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        RegisterHeader(
            progress = progress,
            showBackButton = true,
            onBackClick = onBack
        )
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
    onBack: () -> Unit,
    onMysteryBoxClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DesignTokens.Colors.CreamBackground)
            .verticalScroll(rememberScrollState())
    ) {

        Spacer(modifier = Modifier.height(32.dp))

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

        // Mission content area — varies by completion state
        Column(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            if (!state.isMissionComplete) {
                // === BELUM SELESAI: TrashSection + MysteryBox locked ===
                MissionTrashSection(
                    completedCount = state.scannedCount,
                    totalTarget = state.targetCount,
                    onNavigateNext = onStartCollecting
                )

                Spacer(modifier = Modifier.height(16.dp))

                MysteryBoxSection(isReady = false)
            } else {
                // === SUDAH 5/5: TruckSection + MysteryBox unlocked ===
                MissionTruckSection(
                    completedCount = state.scannedCount,
                    totalTarget = state.targetCount,
                    onFinishClick = onBack
                )

                Spacer(modifier = Modifier.height(16.dp))

                MysteryBoxSection(
                    isReady = true,
                    onClaimRewardClick = onMysteryBoxClick
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
    Box(modifier = Modifier.fillMaxSize()) {
        WaveBackground()
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
        MissionHeader(progress = 0.2f, onBack = onBack)

        Spacer(modifier = Modifier.weight(1f))

        // Dino cool (intro variant)
        Image(
            painter = painterResource(id = R.drawable.dino_cool),
            contentDescription = "Dino Cool",
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Title
        Text(
            text = "Ayo ceritakan aksi barumu!",
            color = Color(0xFF1B4D3E),
            fontWeight = FontWeight.ExtraBold,
            fontSize = 24.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Sudah siap menceritakan\naksi penyelamatan bumi\nhari ini?",
            color = Color.Black,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            lineHeight = 24.sp
        )

        Spacer(modifier = Modifier.weight(1f))

        // Neo-brutalism "Selanjutnya" button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .height(56.dp)
                .background(Color(0xFF2C4331), RoundedCornerShape(12.dp))
                .padding(bottom = 4.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF50B498), RoundedCornerShape(12.dp))
                    .border(1.dp, Color(0xFF2C4331), RoundedCornerShape(12.dp))
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onNext() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Selanjutnya",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        }

            Spacer(modifier = Modifier.height(32.dp))
        }
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
    Box(modifier = Modifier.fillMaxSize()) {
        WaveBackground()
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MissionHeader(progress = 0.4f, onBack = onBack)

            Spacer(modifier = Modifier.height(16.dp))

            DinoSpeechBubble(question = "Sampah jenis apa\nyang kamu buang\nhari ini?")

            Spacer(modifier = Modifier.height(24.dp))

            // Scrollable options
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                TrashItemCard(
                    imageRes = R.drawable.sampahorganik,
                    title = "Sampah Organik",
                    onClick = { onSelect("organik") }
                )
                TrashItemCard(
                    imageRes = R.drawable.sampahdaurulang,
                    title = "Sampah Daur Ulang",
                    onClick = { onSelect("daur_ulang") }
                )

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
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

    fun getSubtypeImage(subtype: String): Int = when (subtype) {
        "buah" -> R.drawable.sampahbuah
        "sayur" -> R.drawable.sampahsayur
        "sisa_makanan" -> R.drawable.sampahorganik
        "kaleng" -> R.drawable.sampahkaleng
        "kertas" -> R.drawable.sampahkertas
        "plastik" -> R.drawable.sampahplastik
        else -> R.drawable.sampahlainnya
    }

    Box(modifier = Modifier.fillMaxSize()) {
        WaveBackground()
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MissionHeader(progress = 0.6f, onBack = onBack)

            Spacer(modifier = Modifier.height(16.dp))

            DinoSpeechBubble(question = "$title jenis apa?")

            Spacer(modifier = Modifier.height(24.dp))

            // Scrollable options
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (filteredCategories.isNotEmpty()) {
                    filteredCategories.forEach { cat ->
                        TrashItemCard(
                            imageRes = getSubtypeImage(cat.subtype),
                            title = "Sampah ${cat.subtype.replace("_", " ").split(" ").joinToString(" ") { it.replaceFirstChar { c -> c.uppercase() } }}",
                            onClick = { onSelect(cat.subtype) }
                        )
                    }
                } else {
                    if (selectedType == "organik") {
                        TrashItemCard(R.drawable.sampahbuah, "Sampah Buah") { onSelect("buah") }
                        TrashItemCard(R.drawable.sampahsayur, "Sampah Sayur") { onSelect("sayur") }
                        TrashItemCard(R.drawable.sampahorganik, "Sisa Makanan") { onSelect("sisa_makanan") }
                    } else {
                        TrashItemCard(R.drawable.sampahkaleng, "Kaleng") { onSelect("kaleng") }
                        TrashItemCard(R.drawable.sampahkertas, "Kertas") { onSelect("kertas") }
                        TrashItemCard(R.drawable.sampahplastik, "Plastik") { onSelect("plastik") }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
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
    Box(modifier = Modifier.fillMaxSize()) {
        WaveBackground()
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MissionHeader(progress = 0.8f, onBack = onBack)

            Spacer(modifier = Modifier.height(16.dp))

            DinoSpeechBubble(question = "Kamu membuang sampah\ntersebut di tempat sampah\nyang terletak dimana?")

            Spacer(modifier = Modifier.height(24.dp))

            // Scrollable options
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    PhotoLocationCard(R.drawable.kantin, "Kantin", Modifier.weight(1f)) { onSelect("kantin") }
                    PhotoLocationCard(R.drawable.ruangkelas, "Ruang Kelas", Modifier.weight(1f)) { onSelect("ruang_kelas") }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    PhotoLocationCard(R.drawable.halaman, "Halaman", Modifier.weight(1f)) { onSelect("halaman") }
                    PhotoLocationCard(R.drawable.toilet, "Toilet", Modifier.weight(1f)) { onSelect("toilet") }
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun PhotoLocationCard(imageRes: Int, label: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Column(
        modifier = modifier
            .background(Color.White, RoundedCornerShape(12.dp))
            .border(1.dp, DesignTokens.Colors.OrangePrimary, RoundedCornerShape(12.dp))
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = label,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.2f)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(12.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .background(Color(0xFFFFF6ED), RoundedCornerShape(8.dp))
                .border(1.dp, DesignTokens.Colors.OrangePrimary, RoundedCornerShape(8.dp))
                .clip(RoundedCornerShape(8.dp))
                .clickable { onClick() }
                .padding(vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label,
                color = DesignTokens.Colors.OrangePrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
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

    Box(modifier = Modifier.fillMaxSize()) {
        WaveBackground()
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MissionHeader(progress = 1.0f, onBack = onBack)

            Spacer(modifier = Modifier.height(16.dp))

            DinoSpeechBubble(question = "Berapa jumlah sampah\nyang kamu buang?")

            Spacer(modifier = Modifier.height(24.dp))

            // Scrollable options
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.padding(32.dp),
                        color = DesignTokens.Colors.TealPrimary
                    )
                } else {
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

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun QuantityCard(label: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .background(Color.White, RoundedCornerShape(12.dp))
            .border(1.dp, DesignTokens.Colors.OrangePrimary, RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(vertical = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = DesignTokens.Colors.OrangePrimary,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
    }
}

// ============================================================
// Shared: Dino Avatar + Speech Bubble
// ============================================================
@Composable
private fun DinoSpeechBubble(question: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Dino avatar box
        Box(
            modifier = Modifier
                .size(86.dp)
                .background(Color.White, RoundedCornerShape(12.dp))
                .border(1.dp, Color(0xFF2C4331), RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.superdinohead),
                contentDescription = "Dino Avatar",
                modifier = Modifier
                    .size(71.dp)
                    .padding(2.dp),
                contentScale = ContentScale.Fit
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Speech bubble
        Box(
            modifier = Modifier
                .weight(1f)
                .background(Color(0xFFECA357), RoundedCornerShape(12.dp))
                .padding(16.dp)
        ) {
            Text(
                text = question,
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                lineHeight = 20.sp
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
            .background(DesignTokens.Colors.CreamBackground),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MissionHeader(progress = 0f, onBack = onExit)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

        if (state.isMissionComplete) {
            // === MISI SELESAI — Celebration ===
            Spacer(modifier = Modifier.height(16.dp))

            // Dino yeay with pow effects
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp),
                contentAlignment = Alignment.Center
            ) {
                // Pow left
                Image(
                    painter = painterResource(id = R.drawable.pow1),
                    contentDescription = null,
                    modifier = Modifier
                        .offset(x = (-80).dp, y = (-30).dp)
                        .size(60.dp),
                    contentScale = ContentScale.Fit
                )
                // Dino yeay
                Image(
                    painter = painterResource(id = R.drawable.dino_yeay),
                    contentDescription = "Dino Yeay",
                    modifier = Modifier.size(220.dp),
                    contentScale = ContentScale.Fit
                )
                // Pow right
                Image(
                    painter = painterResource(id = R.drawable.pow2),
                    contentDescription = null,
                    modifier = Modifier
                        .offset(x = 80.dp, y = 50.dp)
                        .size(70.dp),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Misi Selesai!",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 26.sp,
                color = Color(0xFF1B4D3E),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Reward badges with icons
            Text(
                text = "Hadiah Imbalan:",
                fontSize = 14.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // XP Badge with icon
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = DesignTokens.Colors.TealPrimary
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.level),
                            contentDescription = "XP",
                            modifier = Modifier.size(20.dp),
                            contentScale = ContentScale.Fit
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "+${state.totalGainedXp} XP",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 16.sp,
                            color = Color.White
                        )
                    }
                }

                // Coins Badge with icon
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(2.dp, Color(0xFFFFDF8D)),
                    color = Color.White
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.goldimg),
                            contentDescription = "Coins",
                            modifier = Modifier.size(20.dp),
                            contentScale = ContentScale.Fit
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "+${state.totalGainedCoins}",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 16.sp,
                            color = Color(0xFF8C6200)
                        )
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

            Spacer(modifier = Modifier.height(40.dp))

            // Neo-brutalism "Simpan Progres" button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .height(56.dp)
                    .background(Color(0xFF2C4331), RoundedCornerShape(12.dp))
                    .padding(bottom = 4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFF50B498), RoundedCornerShape(12.dp))
                        .border(1.dp, Color(0xFF2C4331), RoundedCornerShape(12.dp))
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onExit() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Simpan Progres",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
            }
        } else {
            // === BELUM SELESAI ===
            Spacer(modifier = Modifier.height(32.dp))

            // Dino not done yet
            Image(
                painter = painterResource(id = R.drawable.dinonotdoneyet),
                contentDescription = "Dino Belum Selesai",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Misi Kamu Belum\nSelesai Nih!",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 26.sp,
                color = Color(0xFF1B4D3E),
                textAlign = TextAlign.Center,
                lineHeight = 34.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Ayo selesaikan misi hari ini agar\nmendapat hadiah imbalan untuk aksi\nkerenmu!",
                color = Color.Black,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                lineHeight = 21.sp
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

            Spacer(modifier = Modifier.height(40.dp))

            // Neo-brutalism "Lanjutkan!" button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .height(56.dp)
                    .background(Color(0xFF2C4331), RoundedCornerShape(12.dp))
                    .padding(bottom = 4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFF50B498), RoundedCornerShape(12.dp))
                        .border(1.dp, Color(0xFF2C4331), RoundedCornerShape(12.dp))
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onContinue() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Lanjutkan!",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Outlined "Keluar" button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .height(52.dp)
                    .border(2.dp, Color(0xFF50B498), RoundedCornerShape(12.dp))
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onExit() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Keluar",
                    color = Color(0xFF50B498),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
    }
    }
}

