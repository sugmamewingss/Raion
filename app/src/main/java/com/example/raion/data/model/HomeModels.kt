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
    val status: String,                          // "logged", "mission_complete", "already_completed"
    @SerialName("scanned_count") val scannedCount: Int,
    @SerialName("target_count") val targetCount: Int,
    @SerialName("is_completed") val isCompleted: Boolean,
    @SerialName("gained_xp") val gainedXp: Int,
    @SerialName("gained_coins") val gainedCoins: Int
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
    val name: String,
    @SerialName("image_url") val imageUrl: String,
    val price: Int
)
