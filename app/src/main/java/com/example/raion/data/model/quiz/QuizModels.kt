package com.example.raion.data.model.quiz

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QuizChapter(
    val id: String,
    @SerialName("chapter_number") val chapterNumber: Int,
    val title: String,
    val description: String?,
    @SerialName("is_active") val isActive: Boolean
)

@Serializable
data class QuizEpisode(
    val id: String,
    @SerialName("chapter_id") val chapterId: String,
    @SerialName("episode_number") val episodeNumber: Int,
    val title: String,
    @SerialName("reward_xp") val rewardXp: Int,
    @SerialName("reward_coins") val rewardCoins: Int,
    @SerialName("is_active") val isActive: Boolean
)

/**
 * Maps to the quiz_questions table
 */
@Serializable
data class QuizQuestionDto(
    val id: String,
    @SerialName("episode_id") val episodeId: String,
    @SerialName("question_text") val questionText: String,
    val options: List<String>,
    @SerialName("correct_answer_index") val correctAnswerIndex: Int,
    val explanation: String?,
    @SerialName("image_url") val imageUrl: String?,
    @SerialName("order_index") val orderIndex: Int
)

@Serializable
data class UserQuizProgress(
    val id: String,
    @SerialName("user_id") val userId: String,
    @SerialName("episode_id") val episodeId: String,
    val score: Int,
    @SerialName("is_passed") val isPassed: Boolean,
    @SerialName("completed_at") val completedAt: String
)

@Serializable
data class UserChapterProgress(
    val id: String,
    @SerialName("user_id") val userId: String,
    @SerialName("chapter_id") val chapterId: String,
    @SerialName("is_completed") val isCompleted: Boolean,
    @SerialName("completed_at") val completedAt: String
)

@Serializable
data class QuizResultResponse(
    val status: String,
    @SerialName("is_passed") val isPassed: Boolean = false,
    @SerialName("gained_xp") val gainedXp: Int,
    @SerialName("gained_coins") val gainedCoins: Int,
    @SerialName("total_correct_answers") val totalCorrectAnswers: Int
)
