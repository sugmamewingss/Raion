package com.example.raion.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserProfile(
    @SerialName("id")
    val id: String,
    @SerialName("username")
    val username: String,
    @SerialName("name")
    val name: String,
    @SerialName("birth_date")
    val birthDate: String? = null,
    @SerialName("total_xp")
    val totalXp: Int = 0,
    @SerialName("level")
    val level: Int = 1,
    @SerialName("coins")
    val coins: Int = 0,
    @SerialName("lifetime_coins")
    val lifetimeCoins: Int = 0,
    @SerialName("current_streak")
    val currentStreak: Int = 0,
    @SerialName("highest_streak")
    val highestStreak: Int = 0,
    @SerialName("last_active_date")
    val lastActiveDate: String? = null,
    @SerialName("last_mission_completed_date")
    val lastMissionCompletedDate: String? = null,
    @SerialName("current_avatar_url")
    val currentAvatarUrl: String? = null,
    @SerialName("equipped_item_id")
    val equippedItemId: String? = null,
    @SerialName("total_correct_answers")
    val totalCorrectAnswers: Int = 0
)
