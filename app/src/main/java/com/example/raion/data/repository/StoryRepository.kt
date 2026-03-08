package com.example.raion.data.repository

import com.example.raion.data.model.StoryChapterDto
import com.example.raion.data.model.StoryEpisodeDto
import com.example.raion.data.model.UserStoryProgressDto
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.put
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

    suspend fun markEpisodeCompleted(episodeId: String): Result<Unit> {
        return try {
            val userId = getCurrentUserId() ?: throw Exception("User not logged in")
            
            val payload = InsertProgress(
                user_id = userId,
                episode_id = episodeId,
                is_completed = true
            )

            // Upsert (Insert or Update if exists, based on unique user_id + episode_id constraint)
            android.util.Log.d("StoryRepo", "Executing Upsert for: $payload")
            supabase.postgrest["user_story_progress"].upsert(payload) {
                onConflict = "user_id, episode_id"
            }
            
            _refreshTrigger.tryEmit(Unit)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
