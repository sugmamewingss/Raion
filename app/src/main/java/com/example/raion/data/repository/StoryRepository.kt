package com.example.raion.data.repository

import com.example.raion.data.model.StoryChapterDto
import com.example.raion.data.model.StoryEpisodeDto
import com.example.raion.data.model.UserStoryProgressDto
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.rpc
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.serialization.Serializable
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StoryRepository @Inject constructor(
    private val supabase: SupabaseClient
) {
    private val _refreshTrigger = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val refreshTrigger = _refreshTrigger.asSharedFlow()

    private fun getCurrentUserId(): String? {
        return supabase.auth.currentUserOrNull()?.id
    }

    suspend fun getActiveChapters(): Result<List<StoryChapterDto>> {
        return try {
            val chapters = supabase.postgrest["story_chapters"]
                .select(Columns.ALL) {
                    filter { eq("is_active", true) }
                    order("chapter_number", Order.ASCENDING)
                }.decodeList<StoryChapterDto>()
            Result.success(chapters)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAllEpisodes(): Result<List<StoryEpisodeDto>> {
        return try {
            val episodes = supabase.postgrest["story_episodes"]
                .select(Columns.ALL) {
                    order("chapter_id", Order.ASCENDING)
                    order("episode_number", Order.ASCENDING)
                }.decodeList<StoryEpisodeDto>()
            Result.success(episodes)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserProgress(): Result<List<UserStoryProgressDto>> {
        return try {
            val userId = getCurrentUserId() ?: throw Exception("User not logged in")
            val progressList = supabase.postgrest["user_story_progress"]
                .select(Columns.ALL) {
                    filter { eq("user_id", userId) }
                }.decodeList<UserStoryProgressDto>()
            android.util.Log.d("StoryRepo", "Fetched progress: $progressList")
            Result.success(progressList)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @Serializable
    private data class InsertProgress(
        val user_id: String,
        val episode_id: String,
        val is_completed: Boolean
    )

    @kotlinx.serialization.Serializable
    private data class StoryRpcParams(
        @kotlinx.serialization.SerialName("p_user_id") val userId: String,
        @kotlinx.serialization.SerialName("p_episode_id") val episodeId: String,
        @kotlinx.serialization.SerialName("p_earned_xp") val earnedXp: Int,
        @kotlinx.serialization.SerialName("p_earned_coins") val earnedCoins: Int,
        @kotlinx.serialization.SerialName("p_date") val dateStr: String
    )

    suspend fun markEpisodeCompleted(episodeId: String): Result<com.example.raion.data.model.StoryRewardResponse?> {
        return try {
            val userId = getCurrentUserId() ?: throw Exception("User not logged in")
            
            // Siapkan Argumen SQL RPC yang mana sudah mem-bundel Upsert Progress dan Reward
            val params = StoryRpcParams(
                userId = userId,
                episodeId = episodeId,
                earnedXp = 35,
                earnedCoins = 10,
                dateStr = java.time.LocalDate.now().toString()
            )
            // Call RPC to log the history for Diary and get Rewards
            var rewardData: com.example.raion.data.model.StoryRewardResponse? = null
            try {
                val dbResult = supabase.postgrest.rpc("log_story_history", params)
                rewardData = dbResult.decodeAs<com.example.raion.data.model.StoryRewardResponse>()
            } catch (e: Exception) {
                android.util.Log.e("StoryRepo", "Failed logging story history: ${e.message}")
            }
            
            _refreshTrigger.tryEmit(Unit)
            Result.success(rewardData)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
