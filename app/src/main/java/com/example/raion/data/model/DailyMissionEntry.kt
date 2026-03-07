package com.example.raion.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DailyMissionEntry(
    @SerialName("id")
    val id: String = "",
    @SerialName("user_id")
    val userId: String = "",
    @SerialName("scanned_count")
    val scannedCount: Int = 0,
    @SerialName("target_count")
    val targetCount: Int = 5,
    @SerialName("date_recorded")
    val dateRecorded: String = "",
    @SerialName("is_completed")
    val isCompleted: Boolean = false
)
