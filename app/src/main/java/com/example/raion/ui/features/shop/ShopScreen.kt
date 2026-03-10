package com.example.raion.ui.features.shop

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.raion.R
import com.example.raion.data.model.PointShopItem
import com.example.raion.data.model.ShopCategory
import com.example.raion.data.model.UserInventoryItem
import com.example.raion.ui.theme.DesignTokens
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import com.example.raion.ui.util.formatCompactNumber

@Composable
fun ShopScreen(
    currentCoins: Int,
    currentLevel: Int,
    currentAvatarUrl: String, // Source of truth from ViewModel
    categories: List<ShopCategory>,
    items: List<PointShopItem>,
    inventory: List<UserInventoryItem>,
    onPurchase: (PointShopItem) -> Unit,
    onEquip: (PointShopItem) -> Unit
) {
    var selectedCategory by remember { mutableStateOf<ShopCategory?>(null) }
    var inspectedItem by remember { mutableStateOf<PointShopItem?>(null) }
    
    // Internal optimistic state for the avatar URLs so the UI updates instantly
    var optimisticAvatarUrl by remember(currentAvatarUrl) { mutableStateOf(currentAvatarUrl) }
    var activePreviewAvatar by remember(optimisticAvatarUrl) { mutableStateOf(if (optimisticAvatarUrl.isNotEmpty()) optimisticAvatarUrl else "https://nnloirkwladlazxgpgrm.supabase.co/storage/v1/object/public/avatars/dino_default.png") }
    
    // Internal optimistic state for inventory and coins
    var optimisticInventory by remember(inventory) { mutableStateOf(inventory) }
    var optimisticCoins by remember(currentCoins) { mutableStateOf(currentCoins) }
    
    LaunchedEffect(categories) {
        if (selectedCategory == null && categories.isNotEmpty()) {
            selectedCategory = categories.first()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DesignTokens.Colors.CreamBackground)
    ) {
        Spacer(modifier = Modifier.height(16.dp)) // Top padding for status bar area
        
        ShopTopBar(coins = optimisticCoins)
        
        Spacer(modifier = Modifier.height(16.dp))
        
        CharacterShowcase(
            avatarUrl = activePreviewAvatar,
            modifier = Modifier.weight(0.45f)
        )
        
        ShopCategoryTabs(
            categories = categories,
            selectedCategory = selectedCategory,
            onCategorySelected = { selectedCategory = it }
        )
        
        ShopItemGrid(
            category = selectedCategory,
            items = items,
            inventory = optimisticInventory,
            currentLevel = currentLevel,
            currentAvatarUrl = optimisticAvatarUrl, // Use optimistic state here
            onItemSelected = { item ->
                val isOwned = item.isDefault || optimisticInventory.any { it.itemId == item.itemId }
                if (isOwned) {
                    onEquip(item)
                    optimisticAvatarUrl = item.avatarUrl // Optimistic UI update instantly flags border
                    activePreviewAvatar = item.avatarUrl // Optimistic UI update instantly previews hero
                } else {
                    inspectedItem = item
                }
            },
            modifier = Modifier.weight(0.55f)
        )
        
        // Extra space for bottom navigation bar
        Spacer(modifier = Modifier.height(100.dp))
        
        // Dialog Konfirmasi Beli / Pakai
        inspectedItem?.let { item ->
            val isOwned = item.isDefault || optimisticInventory.any { it.itemId == item.itemId }
            val isEquipped = item.avatarUrl == optimisticAvatarUrl
            val canAfford = optimisticCoins >= item.price
            
            AlertDialog(
                onDismissRequest = { 
                    inspectedItem = null
                    // Reset avatar preview to actual equipped avatar when closing inspector
                    activePreviewAvatar = optimisticAvatarUrl 
                },
                title = { Text(text = item.name, fontWeight = FontWeight.Bold) },
                text = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                        SubcomposeAsyncImage(
                            model = item.imageUrl,
                            contentDescription = item.name,
                            modifier = Modifier.size(100.dp),
                            contentScale = ContentScale.Fit,
                            loading = {
                                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = DesignTokens.Colors.OrangePrimary, strokeWidth = 2.dp)
                                }
                            },
                            error = {
                                Image(painter = painterResource(id = R.drawable.clothes1), contentDescription = null, contentScale = ContentScale.Fit)
                            }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        if (isEquipped) {
                            Text(text = "Barang ini sedang kamu pakai sekarang.", textAlign = TextAlign.Center)
                        } else if (isOwned) {
                            Text(text = "Barang ini sudah ada di lemarimu. Apakah kamu ingin memakainya sekarang?", textAlign = TextAlign.Center)
                        } else {
                            Text(text = "Harga: 🪙 ${formatCompactNumber(item.price)}\nKoin Kamu: 🪙 ${formatCompactNumber(optimisticCoins)}", textAlign = TextAlign.Center)
                            if (!canAfford) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Wah, koinmu belum cukup untuk membeli barang ini.",
                                    color = Color.Red,
                                    fontSize = 12.sp,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                },
                confirmButton = {
                    if (!isEquipped) {
                        Button(
                            onClick = {
                                if (isOwned) {
                                    onEquip(item)
                                    optimisticAvatarUrl = item.avatarUrl // Optimistic instantly flags border
                                    activePreviewAvatar = item.avatarUrl // Update UI immediately
                                    inspectedItem = null
                                } else if (canAfford) {
                                    onPurchase(item)
                                    // Beli berhasil (assumed for Optimism):
                                    optimisticInventory = optimisticInventory + UserInventoryItem(
                                        id = "temp_${System.currentTimeMillis()}", // dummy ID
                                        userId = "temp",
                                        itemId = item.itemId,
                                        purchasedAt = ""
                                    )
                                    optimisticCoins -= item.price
                                    optimisticAvatarUrl = item.avatarUrl 
                                    activePreviewAvatar = item.avatarUrl
                                    inspectedItem = null
                                }
                            },colors = ButtonDefaults.buttonColors(
                                containerColor = DesignTokens.Colors.OrangePrimary
                            ),
                            enabled = isOwned || canAfford // Disable buy if not enough coins
                        ) {
                            Text(if (isOwned) "Pakai Baju" else "Beli (🪙 ${formatCompactNumber(item.price)})")
                        }
                    }
                },
                dismissButton = {
                    TextButton(onClick = { 
                        inspectedItem = null 
                        activePreviewAvatar = currentAvatarUrl // Revert preview
                    }) {
                        Text("Tutup", color = Color.Gray)
                    }
                }
            )
            
            // Saat di-inspect, ganti wajah dinosaurus utama dengan avatar pakaian ini (Live Preview)
            LaunchedEffect(item) {
                if (item.avatarUrl.isNotEmpty()) {
                    activePreviewAvatar = item.avatarUrl
                }
            }
        }
    }
}

@Composable
fun ShopTopBar(coins: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .padding(horizontal = DesignTokens.Dimensions.PaddingLarge),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Title (Left Aligned)
        Column {
            Text(
                text = "Toko",
                fontSize = 16.sp,
                color = Color.DarkGray,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "Gobi",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 28.sp,
                color = Color.Black
            )
        }

        // Right side: Coins and Share
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Coin indicator
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .background(Color(0xFFFFDF8D), RoundedCornerShape(8.dp))
                    .border(1.dp, Color(0xFFE5C87A), RoundedCornerShape(8.dp))
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Text("🪙", fontSize = 14.sp)
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = formatCompactNumber(coins),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF8C6200)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Share Icon
            IconButton(onClick = { /* TODO: Share action */ }) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Share",
                    tint = Color.Black
                )
            }
        }
    }
}

@Composable
fun CharacterShowcase(
    avatarUrl: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .border(width = 1.dp, color = Color.Black.copy(alpha = 0.2f)) // Subtle border
    ) {
        // Nature background
        Image(
            painter = painterResource(id = R.drawable.bg_shop),
            contentDescription = "Character Showcase",
            contentScale = ContentScale.Crop,
            alignment = Alignment.BottomCenter,
            modifier = Modifier.fillMaxSize()
        )
        
        // Full Character Image (Sprite Replacement via URL)
        val imageToLoad = if (avatarUrl.isNotEmpty()) avatarUrl else "https://nnloirkwladlazxgpgrm.supabase.co/storage/v1/object/public/avatars/dino_default.png"
        
        SubcomposeAsyncImage(
            model = imageToLoad,
            contentDescription = "Gobi Character",
            modifier = Modifier
                .size(200.dp) // Adjusted size for full character
                .align(Alignment.Center),
            contentScale = ContentScale.Fit,
            loading = {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(modifier = Modifier.size(40.dp), color = DesignTokens.Colors.OrangePrimary, strokeWidth = 3.dp)
                }
            },
            error = {
                Image(painter = painterResource(id = R.drawable.dinoprofile), contentDescription = null, contentScale = ContentScale.Fit)
            }
        )
    }
}

@Composable
fun ShopCategoryTabs(
    categories: List<ShopCategory>,
    selectedCategory: ShopCategory?,
    onCategorySelected: (ShopCategory) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFEADBCE)) // Warm beige background
            .border(width = 1.dp, color = Color.Black.copy(alpha = 0.2f))
            .padding(vertical = 12.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Handle empty categories gracefully
        if (categories.isEmpty()) {
            Text("Memuat kategori...", color = Color.Gray, fontSize = 12.sp)
        }

        categories.forEach { category ->
            CategoryTabItem(
                category = category,
                isSelected = selectedCategory?.id == category.id,
                onClick = { onCategorySelected(category) }
            )
        }
    }
}

@Composable
fun CategoryTabItem(
    category: ShopCategory,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val bgColor = if (isSelected) Color(0xFFD49A6A) else Color.Transparent // Orange brown for selected
    
    Box(
        modifier = Modifier
            .size(60.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .run {
                if (isSelected) border(2.dp, Color(0xFFB57B4A), RoundedCornerShape(12.dp))
                else this
            }
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        SubcomposeAsyncImage(
            model = category.iconUrl,
            contentDescription = category.name,
            modifier = Modifier.size(36.dp),
            contentScale = ContentScale.Fit,
            loading = {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(modifier = Modifier.size(16.dp), color = DesignTokens.Colors.OrangePrimary, strokeWidth = 2.dp)
                }
            },
            error = {
                Image(painter = painterResource(id = R.drawable.cap1), contentDescription = null, contentScale = ContentScale.Fit)
            }
        )
    }
}

@Composable
fun ShopItemGrid(
    category: ShopCategory?,
    items: List<PointShopItem>,
    inventory: List<UserInventoryItem>,
    currentLevel: Int,
    currentAvatarUrl: String,
    onItemSelected: (PointShopItem) -> Unit,
    modifier: Modifier = Modifier
) {
    // Filter item berdasarkan kategori yang sedang dipilih (jika ada), lalu urutkan
    val displayItems = items
        .filter { it.categoryId == category?.id }
        .sortedWith(compareBy({ it.minLevel }, { it.price }))
        .let { filtered ->
            if (filtered.isEmpty()) {
                listOf(
                    PointShopItem(
                        itemId = "dummy_coming_soon_${category?.id}",
                        categoryId = category?.id ?: "",
                        name = "Akan Datang",
                        price = 0,
                        imageUrl = "",
                        avatarUrl = "",
                        isActive = false,
                        minLevel = 1,
                        isDefault = false
                    )
                )
            } else {
                filtered
            }
        }

    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(displayItems) { item ->
            // Cek status kepemilikan (default atau sudah dibeli) dan validasi level
            val isOwned = item.isDefault || inventory.any { it.itemId == item.itemId }
            val isEquipped = item.avatarUrl == currentAvatarUrl && item.avatarUrl.isNotEmpty()
            val isLevelLocked = currentLevel < item.minLevel

            ShopItemCard(
                item = item,
                isOwned = isOwned,
                isEquipped = isEquipped,
                isLevelLocked = isLevelLocked,
                onClick = { onItemSelected(item) }
            )
        }
    }
}

@Composable
fun ShopItemCard(
    item: PointShopItem, 
    isOwned: Boolean,
    isEquipped: Boolean,
    isLevelLocked: Boolean,
    onClick: () -> Unit
) {
    val isAvailable = item.isActive && !isLevelLocked
    val cardBg = if (isAvailable) Color(0xFFF9F9F9) else Color(0xFFE0E0E0)
    val cardShadow = if (isAvailable) Color(0xFFD4D4D4) else Color(0xFFC0C0C0)
    
    // Determine the border color based on equipped state
    val borderColor = if (isEquipped) DesignTokens.Colors.OrangePrimary else Color.Transparent
    val borderWidth = if (isEquipped) 3.dp else 0.dp
    
    Box(
        modifier = Modifier
            .aspectRatio(1f) // Square card
            .clip(RoundedCornerShape(16.dp))
            .background(cardShadow) // Outer shadow block
            .border(width = borderWidth, color = borderColor, shape = RoundedCornerShape(16.dp))
            .clickable(enabled = isAvailable) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        // Inner lifted button
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 6.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(cardBg),
            contentAlignment = Alignment.Center
        ) {
            if (!item.isActive) {
            // Coming Soon State
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Checkroom,
                    contentDescription = "Coming Soon",
                    tint = Color.Gray,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Coming\nSoon",
                    color = Color.DarkGray,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    lineHeight = 16.sp
                )
            }
        } else if (isLevelLocked) {
            // Level Locked State
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Locked",
                    tint = Color.Gray,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Buka di\nLv. ${item.minLevel}",
                    color = Color.DarkGray,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    lineHeight = 16.sp
                )
            }
        } else {
            // Available State (Tampil Item Sepenuhnya)
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                // Background item image
                SubcomposeAsyncImage(
                    model = item.imageUrl,
                    contentDescription = item.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop, // Fill entire card
                    loading = {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.Gray, strokeWidth = 2.dp)
                        }
                    },
                    error = {
                        Image(painter = painterResource(id = R.drawable.clothes1), contentDescription = null, contentScale = ContentScale.Crop)
                    }
                )
                
                // Dim overlay for better text readability if needed
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.05f)) // Very subtle overlay
                )
                
                // Price Tag (Only shown if NOT owned)
                if (!isOwned) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 8.dp)
                            .wrapContentSize()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White.copy(alpha = 0.9f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text("🪙", fontSize = 12.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = formatCompactNumber(item.price),
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 13.sp,
                            color = Color.Black
                        )
                    }
                }
            } // Close Inner Lifted Box
        } // Close Main Button Surface Box
    } // Close Parent Shadow Box
}
}
