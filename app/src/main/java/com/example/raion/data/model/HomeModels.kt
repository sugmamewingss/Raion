package com.example.raion.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// === Daily Mission System ===

@Serializable
data class DailyMissionTracker(
    val id: String,
    @SerialName("user_id") val userId: String,
    @SerialName("scanned_count") val scannedCount: Int = 0,
    @SerialName("target_count") val targetCount: Int = 5,
    @SerialName("date_recorded") val dateRecorded: String,
    @SerialName("is_completed") val isCompleted: Boolean = false
)

@Serializable
data class DailyMissionTrackerInsert(
    @SerialName("user_id") val userId: String,
    @SerialName("scanned_count") val scannedCount: Int = 0,
    @SerialName("target_count") val targetCount: Int = 5,
    @SerialName("date_recorded") val dateRecorded: String,
    @SerialName("is_completed") val isCompleted: Boolean = false
)

// UI representation for active mission card
data class ActiveMission(
    val title: String = "Membuang sampah",
    val currentProgress: Int,
    val targetProgress: Int = 5
)

// === Waste Entry System (Mission Wizard) ===

@Serializable
data class WasteCategory(
    val id: String,
    @SerialName("waste_type") val wasteType: String,
    val subtype: String,
    @SerialName("image_url") val imageUrl: String? = null,
    @SerialName("sort_order") val sortOrder: Int = 0
)

@Serializable
data class WasteEntryResponse(
    val status: String = "logged",                 // "logged", "mission_complete", "already_completed"
    @SerialName("scanned_count") val scannedCount: Int = 0,
    @SerialName("target_count") val targetCount: Int = 5,
    @SerialName("is_completed") val isCompleted: Boolean = false,
    @SerialName("gained_xp") val gainedXp: Int = 0,
    @SerialName("gained_coins") val gainedCoins: Int = 0
)

// === Content ===

@Serializable
data class EduArticle(
    @SerialName("article_id") val articleId: String,
    val tag: String,
    val title: String,
    val subtitle: String,
    @SerialName("image_url") val imageUrl: String? = null,
    @SerialName("order_index") val orderIndex: Int = 0
)

@Serializable
data class LeaderboardUser(
    @SerialName("user_id") val userId: String,
    @SerialName("display_name") val displayName: String,
    @SerialName("avatar_id") val avatarId: Int,
    @SerialName("current_level") val currentLevel: Int,
    @SerialName("current_xp") val currentXp: Int,
    val rank: Long
)

@Serializable
data class PointShopItem(
    @SerialName("item_id") val itemId: String,
    @SerialName("category_id") val categoryId: String? = null,
    val name: String,
    @SerialName("image_url") val imageUrl: String,
    @SerialName("avatar_url") val avatarUrl: String = "",
    val price: Int,
    @SerialName("min_level") val minLevel: Int = 1,
    @SerialName("is_active") val isActive: Boolean = true,
    @SerialName("is_default") val isDefault: Boolean = false
)

@Serializable
data class ShopCategory(
    val id: String,
    val name: String,
    @SerialName("icon_url") val iconUrl: String,
    @SerialName("sort_order") val sortOrder: Int = 0
)

@Serializable
data class UserInventoryItem(
    val id: String,
    @SerialName("user_id") val userId: String,
    @SerialName("item_id") val itemId: String,
    @SerialName("purchased_at") val purchasedAt: String
)
