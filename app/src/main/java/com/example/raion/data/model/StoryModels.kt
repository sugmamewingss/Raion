package com.example.raion.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StoryChapterDto(
    val id: String,
    @SerialName("chapter_number") val chapterNumber: Int,
    val title: String,
    @SerialName("is_active") val isActive: Boolean = true
)

@Serializable
data class StoryEpisodeDto(
    val id: String,
    @SerialName("chapter_id") val chapterId: String,
    @SerialName("episode_number") val episodeNumber: Int,
    val title: String,
    @SerialName("cover_image_url") val coverImageUrl: String? = null,
    @SerialName("content_image_url") val contentImageUrl: String? = null,
    @SerialName("is_premium") val isPremium: Boolean = false
)

@Serializable
data class UserStoryProgressDto(
    val id: String,
    @SerialName("user_id") val userId: String,
    @SerialName("episode_id") val episodeId: String,
    @SerialName("is_completed") val isCompleted: Boolean = false,
    @SerialName("unlocked_at") val unlockedAt: String? = null
)

@Serializable
data class StoryRewardResponse(
    val status: String,
    @SerialName("chapter_id") val chapterId: String? = null,
    @SerialName("chapter_is_fully_completed") val chapterIsFullyCompleted: Boolean = false,
    @SerialName("granted_xp") val grantedXp: Int = 0,
    @SerialName("granted_coins") val grantedCoins: Int = 0
)
