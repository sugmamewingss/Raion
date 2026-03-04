package com.example.raion.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MasterTask(
    val id: String,
    val title: String,
    val description: String? = null,
    @SerialName("reward_xp") val rewardXp: Int,
    @SerialName("reward_coins") val rewardCoins: Int,
    @SerialName("task_type") val taskType: String,
    @SerialName("is_active") val isActive: Boolean
)

@Serializable
data class UserTask(
    val id: String,
    @SerialName("profile_id") val profileId: String,
    @SerialName("master_task_id") val masterTaskId: String,
    val status: String,
    @SerialName("completed_date") val completedDate: String
)
