package com.example.raion.data.repository

import android.util.Log
import com.example.raion.data.model.ActiveMission
import com.example.raion.data.model.DailyMissionTracker
import com.example.raion.data.model.DailyMissionTrackerInsert
import com.example.raion.data.model.EduArticle
import com.example.raion.data.model.LeaderboardUser
import com.example.raion.data.model.MasterTask
import com.example.raion.data.model.PointShopItem
import com.example.raion.data.model.ShopCategory
import com.example.raion.data.model.UserProfile
import com.example.raion.data.model.UserInventoryItem
import com.example.raion.data.model.UserTask
import com.example.raion.data.model.WasteCategory
import com.example.raion.data.model.WasteEntryResponse
import com.example.raion.data.model.quiz.QuizChapter
import com.example.raion.data.model.quiz.QuizEpisode
import com.example.raion.data.model.quiz.QuizQuestionDto
import com.example.raion.data.model.quiz.QuizResultResponse
import com.example.raion.data.model.quiz.UserChapterProgress
import com.example.raion.data.model.quiz.UserQuizProgress
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.rpc
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

/**
 * HomeRepository — Semua data "game" ada di sini.
 * Profile, missions, leaderboard, articles, shop, waste logging, streak, tasks.
 */
@Singleton
class HomeRepository @Inject constructor(
    private val supabase: SupabaseClient
) {

    private fun getCurrentUserId(): String? {
        return supabase.auth.currentUserOrNull()?.id
    }

    // =========================================================================
    // Profile
    // =========================================================================

    suspend fun getUserProfile(): Result<UserProfile> {
        return try {
            val userId = getCurrentUserId() ?: throw Exception("User not logged in")
            val profile = supabase.postgrest["profiles"]
                .select(Columns.ALL) {
                    filter { eq("id", userId) }
                }.decodeSingle<UserProfile>()
            Result.success(profile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // =========================================================================
    // Daily Streak
    // =========================================================================

    suspend fun updateDailyStreak(): Result<Unit> {
        return try {
            val userId = getCurrentUserId() ?: throw Exception("User not logged in")
            val todayStr = java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ISO_LOCAL_DATE)
            supabase.postgrest.rpc(
                function = "update_daily_streak",
                parameters = buildJsonObject {
                    put("p_user_id", userId)
                    put("p_date", todayStr)
                }
            )
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // =========================================================================
    // Daily Missions
    // =========================================================================

    suspend fun getActiveMissions(): Result<List<ActiveMission>> {
        return try {
            val userId = getCurrentUserId() ?: throw Exception("User not authenticated")
            val todayStr = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)

            val dailyMissionResponse = supabase.postgrest["daily_missions"]
                .select(Columns.ALL) {
                    filter { 
                        eq("user_id", userId) 
                        eq("date_recorded", todayStr)
                    }
                }.decodeList<DailyMissionTracker>()

            val currentMissionTracker = if (dailyMissionResponse.isEmpty()) {
                val newTracker = DailyMissionTrackerInsert(
                    userId = userId,
                    scannedCount = 0,
                    targetCount = 5,
                    dateRecorded = todayStr,
                    isCompleted = false
                )
                
                Log.d("HomeRepo", "Inserting new daily mission for $todayStr")
                supabase.postgrest["daily_missions"]
                    .insert(newTracker) { select() }
                    .decodeSingle<DailyMissionTracker>()
            } else {
                dailyMissionResponse.first()
            }
            
            if (currentMissionTracker.isCompleted) {
                Result.success(emptyList()) 
            } else {
                Result.success(
                    listOf(
                        ActiveMission(
                            title = "Membuang sampah",
                            currentProgress = currentMissionTracker.scannedCount,
                            targetProgress = currentMissionTracker.targetCount
                        )
                    )
                )
            }
            
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getCompletedMissionsCount(): Result<Int> {
        return try {
            val userId = getCurrentUserId() ?: throw Exception("User not authenticated")
            
            @kotlinx.serialization.Serializable
            data class MissionIdRow(val id: String)
            
            val missions = supabase.postgrest["daily_missions"]
                .select(Columns.list("id")) {
                    filter { 
                        eq("user_id", userId) 
                        eq("is_completed", true)
                    }
                }.decodeList<MissionIdRow>()
            Result.success(missions.size)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // =========================================================================
    // Waste Entry (Mission Wizard)
    // =========================================================================

    suspend fun getDailyHistory(dateStr: String): Result<com.example.raion.data.model.DailyHistory> {
        return try {
            val userId = getCurrentUserId() ?: throw Exception("User not authenticated")
            
            @kotlinx.serialization.Serializable
            data class MissionRow(
                val id: String,
                @kotlinx.serialization.SerialName("is_completed") val isCompleted: Boolean = false
            )
            
            val dailyMission = supabase.postgrest["daily_missions"]
                .select(Columns.list("id", "is_completed")) {
                    filter {
                        eq("user_id", userId)
                        eq("date_recorded", dateStr)
                    }
                }.decodeList<MissionRow>()
                
            var missionId: String? = null
            var isCompleted = false
            
            if (dailyMission.isNotEmpty()) {
                val mission = dailyMission.first()
                missionId = mission.id
                isCompleted = mission.isCompleted
            }
            
            var missionsCompleted = 0
            var xpGained = 0
            var coinsGained = 0
            
            if (missionId != null) {
                @kotlinx.serialization.Serializable
                data class WasteEntrySimple(
                    @kotlinx.serialization.SerialName("quantity") val quantity: Int = 0
                )
    
                val entries = supabase.postgrest["waste_entries"]
                    .select(Columns.list("quantity")) {
                        filter {
                            eq("mission_id", missionId)
                        }
                    }.decodeList<WasteEntrySimple>()
                    
                val totalQuantity = entries.sumOf { it.quantity }
                xpGained += totalQuantity * 10
                coinsGained += totalQuantity * 2
            }
            
            if (isCompleted) {
                missionsCompleted = 1
                xpGained += 50
                coinsGained += 10
            }
            
            @kotlinx.serialization.Serializable
            data class QuizHistoryRow(
                @kotlinx.serialization.SerialName("earned_xp") val earnedXp: Int = 0,
                @kotlinx.serialization.SerialName("earned_coins") val earnedCoins: Int = 0
            )
            
            // Fetch Quiz History for Diary Stats
            val quizHistory = try {
                supabase.postgrest["user_quiz_history"]
                    .select(Columns.list("earned_xp", "earned_coins")) {
                        filter {
                            eq("user_id", userId)
                            eq("date_recorded", dateStr)
                        }
                    }.decodeList<QuizHistoryRow>()
            } catch (e: Exception) {
                Log.e("HomeRepository", "Failed fetching quiz history: ${e.message}")
                emptyList()
            }
                
            val quizzesCompleted = quizHistory.size
            xpGained += quizHistory.sumOf { it.earnedXp }
            coinsGained += quizHistory.sumOf { it.earnedCoins }
            
            // Fetch Story History for Diary Stats
            @kotlinx.serialization.Serializable
            data class StoryHistoryRow(
                @kotlinx.serialization.SerialName("earned_xp") val earnedXp: Int = 0,
                @kotlinx.serialization.SerialName("earned_coins") val earnedCoins: Int = 0
            )
            
            val storyHistory = try {
                supabase.postgrest["user_story_history"]
                    .select(Columns.list("earned_xp", "earned_coins")) {
                        filter {
                            eq("user_id", userId)
                            eq("date_recorded", dateStr)
                        }
                    }.decodeList<StoryHistoryRow>()
            } catch (e: Exception) {
                Log.e("HomeRepository", "Failed fetching story history: ${e.message}")
                emptyList()
            }
                
            val modulesCompleted = storyHistory.size
            xpGained += storyHistory.sumOf { it.earnedXp }
            coinsGained += storyHistory.sumOf { it.earnedCoins }
            
            Result.success(com.example.raion.data.model.DailyHistory(
                date = dateStr, 
                missionsCompleted = missionsCompleted, 
                quizzesCompleted = quizzesCompleted, 
                modulesCompleted = modulesCompleted,
                xpGained = xpGained, 
                coinsGained = coinsGained
            ))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getWasteCategories(): Result<List<WasteCategory>> {
        return try {
            val categories = supabase.postgrest["waste_categories"]
                .select(Columns.ALL) {
                    order("waste_type", Order.ASCENDING)
                    order("sort_order", Order.ASCENDING)
                }.decodeList<WasteCategory>()
            Result.success(categories)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun logWasteEntry(
        wasteType: String,
        wasteSubtype: String,
        location: String,
        quantity: Int
    ): Result<WasteEntryResponse> {
        return try {
            val userId = getCurrentUserId() ?: throw Exception("User not authenticated")
            val todayStr = java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ISO_LOCAL_DATE)
            val response = supabase.postgrest.rpc(
                function = "log_waste_entry",
                parameters = buildJsonObject {
                    put("p_user_id", userId)
                    put("p_waste_type", wasteType)
                    put("p_waste_subtype", wasteSubtype)
                    put("p_location", location)
                    put("p_quantity", quantity)
                    put("p_date", todayStr)
                }
            ).decodeAs<WasteEntryResponse>()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // =========================================================================
    // Leaderboard
    // =========================================================================

    suspend fun getTopPlayerProfiles(limit: Long = 3): Result<List<UserProfile>> {
        return try {
            val profiles = supabase.postgrest["profiles"]
                .select(Columns.ALL) {
                    order("total_xp", Order.DESCENDING)
                    limit(limit)
                }.decodeList<UserProfile>()
            Result.success(profiles)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // =========================================================================
    // Tasks (Master Tasks system)
    // =========================================================================

    suspend fun getIncompleteDailyTasks(): Result<List<MasterTask>> {
        return try {
            val userId = getCurrentUserId() ?: throw Exception("User not logged in")
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val todayStr = dateFormat.format(java.util.Date())

            val activeTasks = supabase.postgrest["master_tasks"]
                .select(Columns.ALL) {
                    filter { eq("is_active", true) }
                }.decodeList<MasterTask>()
                
            val userTasksToday = supabase.postgrest["user_tasks"]
                .select(Columns.ALL) {
                    filter { 
                        eq("profile_id", userId) 
                        eq("completed_date", todayStr)
                    }
                }.decodeList<UserTask>()
                
            val completedTaskIds = userTasksToday.map { it.masterTaskId }
            val incompleteTasks = activeTasks.filter { it.id !in completedTaskIds }
            
            Result.success(incompleteTasks)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // =========================================================================
    // Content
    // =========================================================================

    suspend fun getEducationalArticles(): Result<List<EduArticle>> {
        return try {
            val articles = supabase.postgrest["edu_articles"]
                .select { order("order_index", Order.ASCENDING) }
                .decodeList<EduArticle>()
            Result.success(articles)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getPointShopItems(): Result<List<PointShopItem>> {
        return try {
            val items = supabase.postgrest["point_shop"]
                .select()
                .decodeList<PointShopItem>()
            Result.success(items)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getShopCategories(): Result<List<ShopCategory>> {
        return try {
            val categories = supabase.postgrest["shop_categories"]
                .select { order("sort_order", Order.ASCENDING) }
                .decodeList<ShopCategory>()
            Result.success(categories)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getUserInventory(): Result<List<UserInventoryItem>> {
        return try {
            val userId = getCurrentUserId() ?: throw Exception("User not logged in")
            val inventory = supabase.postgrest["user_inventory"]
                .select { filter { eq("user_id", userId) } }
                .decodeList<UserInventoryItem>()
            Result.success(inventory)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun buyShopItem(itemId: String): Result<Unit> {
        return try {
            val userId = getCurrentUserId() ?: throw Exception("User not logged in")
            supabase.postgrest.rpc(
                function = "buy_shop_item",
                parameters = buildJsonObject {
                    put("p_user_id", userId)
                    put("p_item_id", itemId)
                }
            )
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun equipShopItem(itemId: String): Result<Unit> {
        return try {
            val userId = getCurrentUserId() ?: throw Exception("User not logged in")
            supabase.postgrest.rpc(
                function = "equip_shop_item",
                parameters = buildJsonObject {
                    put("p_user_id", userId)
                    put("p_item_id", itemId)
                }
            )
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    // --- QUIZ FEATURES ---
    
    suspend fun getQuizChapters(): Result<List<QuizChapter>> {
        return try {
            val chapters = supabase.postgrest["quiz_chapters"]
                .select {
                    filter { eq("is_active", true) }
                }.decodeList<QuizChapter>()
                
            Result.success(chapters.sortedBy { it.chapterNumber })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getQuizEpisodes(chapterId: String): Result<List<QuizEpisode>> {
        return try {
            val episodes = supabase.postgrest["quiz_episodes"]
                .select {
                    filter { 
                        eq("chapter_id", chapterId)
                        eq("is_active", true) 
                    }
                }.decodeList<QuizEpisode>()
                
            Result.success(episodes.sortedBy { it.episodeNumber })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getQuizQuestions(episodeId: String): Result<List<QuizQuestionDto>> {
        return try {
            val questions = supabase.postgrest["quiz_questions"]
                .select {
                    filter { eq("episode_id", episodeId) }
                }.decodeList<QuizQuestionDto>()
                
            Result.success(questions.sortedBy { it.orderIndex })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getTotalQuestionsByChapter(chapterId: String): Result<Int> {
        return try {
            // Kita bisa mengambil semua episode dalam chapter, lalu mengambil semua pertanyaannya
            val episodes = supabase.postgrest["quiz_episodes"]
                .select() {
                    filter { 
                        eq("chapter_id", chapterId)
                        eq("is_active", true) 
                    }
                }.decodeList<QuizEpisode>() // Fetch all to satisfy the properties
                
            var totalCount = 0
            for (ep in episodes) {
                // select (count) doesn't have an exact wrapper in this sdk so we decode to a list
                val countRes = supabase.postgrest["quiz_questions"]
                    .select() {
                        filter { eq("episode_id", ep.id) }
                    }.decodeList<QuizQuestionDto>()
                totalCount += countRes.size
            }
            Result.success(totalCount)
        } catch (e: Exception) {
            Log.e("HomeRepository", "Failed counting questions: ${e.message}")
            Result.failure(e)
        }
    }

    suspend fun getUserQuizProgress(userId: String): Result<List<UserQuizProgress>> {
        return try {
            val progress = supabase.postgrest["user_quiz_progress"]
                .select {
                    filter { eq("user_id", userId) }
                }.decodeList<UserQuizProgress>()
                
            Result.success(progress)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getUserChapterProgress(userId: String): Result<List<UserChapterProgress>> {
        return try {
            val progress = supabase.postgrest["user_chapter_progress"]
                .select {
                    filter { eq("user_id", userId) }
                }.decodeList<UserChapterProgress>()
                
            Result.success(progress)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun submitQuizResult(episodeId: String, correctAnswersCount: Int): Result<QuizResultResponse> {
        return try {
            val userId = getCurrentUserId() ?: throw Exception("User not authenticated")
            
            @kotlinx.serialization.Serializable
            data class RpcParams(
                @kotlinx.serialization.SerialName("p_user_id") val userId: String,
                @kotlinx.serialization.SerialName("p_episode_id") val episodeId: String,
                @kotlinx.serialization.SerialName("p_correct_answers_count") val correctAnswersCount: Int,
                @kotlinx.serialization.SerialName("p_date") val dateStr: String
            )
            
            val params = RpcParams(userId, episodeId, correctAnswersCount, java.time.LocalDate.now().toString())
            
            val response = supabase.postgrest.rpc("submit_quiz_result", params)
            val result = response.decodeAs<QuizResultResponse>()
            
            Result.success(result)
        } catch (e: Exception) {
            Log.e("HomeRepository", "Failed submitting quiz: ${e.message}")
            Result.failure(e)
        }
    }
}
