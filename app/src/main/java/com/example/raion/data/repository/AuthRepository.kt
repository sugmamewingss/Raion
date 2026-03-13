package com.example.raion.data.repository

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.rpc
import kotlinx.serialization.json.put
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject
import android.util.Log
import javax.inject.Singleton
import com.example.raion.data.model.MasterTask
import com.example.raion.data.model.UserProfile
import com.example.raion.data.model.UserTask
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order

/**
 * AuthRepository — menangani autentikasi + game data functions.
 */
@Singleton
class AuthRepository @Inject constructor(
    private val supabase: SupabaseClient
) {

    private fun generateDummyEmail(username: String): String {
        val safeUsername = username.replace(Regex("[^A-Za-z0-9]"), "").lowercase()
        return "$safeUsername@raion.app"
    }

    suspend fun checkUsernameAvailable(username: String): Result<Boolean> {
        return try {
            val response = supabase.postgrest.rpc(
                function = "check_username_available",
                parameters = buildJsonObject { put("target_username", username) }
            ).decodeAs<Boolean>()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun registerCustomUser(name: String, birthDate: String, username: String, password: String): Result<Unit> {
        return try {
            val dummyEmail = generateDummyEmail(username)

            val inputFormat = SimpleDateFormat("dd MMMM yyyy", Locale.forLanguageTag("id-ID"))
            val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            
            val parsedDate = try {
                val d = inputFormat.parse(birthDate)
                if (d != null) outputFormat.format(d) else birthDate
            } catch (e: Exception) {
                birthDate
            }

            supabase.auth.signUpWith(Email) {
                email = dummyEmail
                this.password = password

                this.data = buildJsonObject {
                    put("full_name", name)
                    put("birth_date", parsedDate)
                    put("username", username.lowercase())
                }
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun loginCustomUser(username: String, password: String): Result<Unit> {
        return try {
            val dummyEmail = generateDummyEmail(username)

            supabase.auth.signInWith(Email) {
                email = dummyEmail
                this.password = password
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun logout(): Result<Unit> {
        return try {
            supabase.auth.signOut()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getLoggedInUserName(): String {
        return try {
            val session = supabase.auth.currentSessionOrNull()
            val user = session?.user
            val fullName = user?.userMetadata?.get("full_name")?.jsonPrimitive?.content ?: "Sobat Gobi"
            
            fullName.split(" ").firstOrNull()?.replaceFirstChar { 
                if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() 
            } ?: "Sobat Gobi"
        } catch (e: Exception) {
            "Sobat Gobi"
        }
    }

    suspend fun getUserProfile(): Result<UserProfile> {
        return try {
            val session = supabase.auth.currentSessionOrNull()
            val userId = session?.user?.id ?: throw Exception("User not logged in")

            val profile = supabase.postgrest["profiles"]
                .select(Columns.ALL) {
                    filter { eq("id", userId) }
                }.decodeSingle<UserProfile>()

            Result.success(profile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateUserProfile(name: String, birthDate: String): Result<Unit> {
        return try {
            val session = supabase.auth.currentSessionOrNull()
            val userId = session?.user?.id ?: throw Exception("User not logged in")

            val inputFormat = SimpleDateFormat("dd MMMM yyyy", Locale.forLanguageTag("id-ID"))
            val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            
            val parsedDate = try {
                val d = inputFormat.parse(birthDate)
                if (d != null) outputFormat.format(d) else birthDate
            } catch (e: Exception) {
                birthDate
            }

            // 1. Update Auth user (metadata only, we cannot change dummy email without verification)
            supabase.auth.updateUser {
                data = buildJsonObject {
                    put("full_name", name)
                    put("birth_date", parsedDate)
                }
            }

            // 2. Update profiles table
            supabase.postgrest["profiles"]
                .update({
                    set("name", name)
                    set("birth_date", parsedDate)
                }) {
                    filter {
                        eq("id", userId)
                    }
                }

            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("AuthRepository", "updateUserProfile error", e)
            Result.failure(e)
        }
    }

    suspend fun updateDailyStreak(): Result<Unit> {
        return try {
            val session = supabase.auth.currentSessionOrNull()
            val userId = session?.user?.id ?: throw Exception("User not logged in")

            supabase.postgrest.rpc(
                function = "update_daily_streak",
                parameters = buildJsonObject { put("p_user_id", userId) }
            )
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getTopPlayerProfiles(limit: Long = 3): Result<List<UserProfile>> {
        return try {
            val profiles = supabase.postgrest["profiles"]
                .select(Columns.ALL) {
                    order("level", Order.DESCENDING)
                    order("total_xp", Order.DESCENDING)
                    order("id", Order.ASCENDING) // Earliest registration first as tiebreaker
                    limit(limit)
                }.decodeList<UserProfile>()
            Result.success(profiles)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserRank(): Result<Int> {
        return try {
            val session = supabase.auth.currentSessionOrNull()
            val userId = session?.user?.id ?: throw Exception("User not logged in")

            // Fetch all profiles ordered by level DESC, total_xp DESC, id ASC
            val allProfiles = supabase.postgrest["profiles"]
                .select(Columns.ALL) {
                    order("level", Order.DESCENDING)
                    order("total_xp", Order.DESCENDING)
                    order("id", Order.ASCENDING)
                }.decodeList<UserProfile>()

            val rank = allProfiles.indexOfFirst { it.id == userId } + 1
            Result.success(if (rank > 0) rank else allProfiles.size)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getIncompleteDailyTasks(): Result<List<MasterTask>> {
        return try {
            val session = supabase.auth.currentSessionOrNull()
            val userId = session?.user?.id ?: throw Exception("User not logged in")

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

    /**
     * Calculate max mission target based on user level.
     * Level 1-19: 10, Level 20-29: 15, Level 30-39: 20, etc.
     */
    private fun calculateMaxTarget(level: Int): Int {
        val bonus = ((level / 10) - 1).coerceAtLeast(0) * 5
        return 10 + bonus
    }

    /**
     * Record a trash disposal: increment scanned_count in daily_missions for today.
     * Also inserts a waste_entry record with the category (organic/inorganic).
     * Returns the updated scanned_count.
     */
    suspend fun recordTrashDisposal(quantity: Int, category: String = "organic"): Result<Int> {
        return try {
            val session = supabase.auth.currentSessionOrNull()
            val userId = session?.user?.id ?: throw Exception("User not logged in")

            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val todayStr = dateFormat.format(java.util.Date())

            // Get user level for max target calculation
            val profile = supabase.postgrest["profiles"]
                .select(Columns.ALL) {
                    filter { eq("id", userId) }
                }.decodeSingle<UserProfile>()
            val maxTarget = calculateMaxTarget(profile.level)

            // Check if there's already a daily_missions entry for today
            val existingEntries = supabase.postgrest["daily_missions"]
                .select(Columns.ALL) {
                    filter {
                        eq("user_id", userId)
                        eq("date_recorded", todayStr)
                    }
                }.decodeList<com.example.raion.data.model.DailyMissionEntry>()

            val missionRow: com.example.raion.data.model.DailyMissionEntry
            val newScannedCount: Int
            
            if (existingEntries.isNotEmpty()) {
                missionRow = existingEntries.first()
                newScannedCount = missionRow.scannedCount + quantity
                val isNowCompleted = newScannedCount >= maxTarget

                supabase.postgrest["daily_missions"]
                    .update({
                        set("scanned_count", newScannedCount)
                        set("target_count", maxTarget)
                        set("is_completed", isNowCompleted)
                    }) {
                        filter {
                            eq("id", missionRow.id)
                        }
                    }
            } else {
                newScannedCount = quantity
                val isNowCompleted = newScannedCount >= maxTarget

                missionRow = supabase.postgrest["daily_missions"]
                    .insert(buildJsonObject {
                        put("user_id", userId)
                        put("scanned_count", newScannedCount)
                        put("target_count", maxTarget)
                        put("date_recorded", todayStr)
                        put("is_completed", isNowCompleted)
                    }) {
                        select()
                    }.decodeSingle<com.example.raion.data.model.DailyMissionEntry>()
            }

            // Insert waste_entry record correctly using waste_type and mission_id
            val dbWasteType = if (category.lowercase() == "organic") "organik" else "anorganik"
            supabase.postgrest["waste_entries"]
                .insert(buildJsonObject {
                    put("user_id", userId)
                    put("mission_id", missionRow.id)
                    put("waste_type", dbWasteType)
                    put("quantity", quantity)
                    // waste_subtype and location optional if null
                })

            Result.success(newScannedCount)
        } catch (e: Exception) {
            Log.e("AuthRepository", "recordTrashDisposal error", e)
            Result.failure(e)
        }
    }

    /**
     * Get today's daily mission progress (scanned_count / target_count).
     * Target is calculated based on user's level.
     */
    suspend fun getDailyMissionProgress(): Result<Pair<Int, Int>> {
        return try {
            val session = supabase.auth.currentSessionOrNull()
            val userId = session?.user?.id ?: throw Exception("User not logged in")

            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val todayStr = dateFormat.format(java.util.Date())

            val profile = supabase.postgrest["profiles"]
                .select(Columns.ALL) {
                    filter { eq("id", userId) }
                }.decodeSingle<UserProfile>()
            val maxTarget = calculateMaxTarget(profile.level)

            val entries = supabase.postgrest["daily_missions"]
                .select(Columns.ALL) {
                    filter {
                        eq("user_id", userId)
                        eq("date_recorded", todayStr)
                    }
                }.decodeList<com.example.raion.data.model.DailyMissionEntry>()

            if (entries.isNotEmpty()) {
                val entry = entries.first()
                Result.success(Pair(entry.scannedCount, maxTarget))
            } else {
                Result.success(Pair(0, maxTarget))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Get today's organic and inorganic counts from waste_entries.
     * Returns Pair(organicCount, inorganicCount).
     */
    suspend fun getOrganicInorganicCounts(): Result<Pair<Int, Int>> {
        return try {
            val session = supabase.auth.currentSessionOrNull()
            val userId = session?.user?.id ?: throw Exception("User not logged in")

            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val todayStr = dateFormat.format(java.util.Date())

            @kotlinx.serialization.Serializable
            data class WasteEntrySimple(
                @kotlinx.serialization.SerialName("waste_type") val wasteType: String = "",
                @kotlinx.serialization.SerialName("quantity") val quantity: Int = 0
            )

            // Since we only want today's count, and waste_entries lacks date_recorded,
            // we first get today's mission_id.
            var organicTotal = 0
            var inorganicTotal = 0
            
            val todayMission = supabase.postgrest["daily_missions"]
                .select(Columns.list("id")) {
                    filter {
                        eq("user_id", userId)
                        eq("date_recorded", todayStr)
                    }
                }.decodeList<com.example.raion.data.model.DailyMissionEntry>()
                
            if (todayMission.isNotEmpty()) {
                val missionId = todayMission.first().id
                val entries = supabase.postgrest["waste_entries"]
                    .select(Columns.list("waste_type", "quantity")) {
                        filter {
                            eq("mission_id", missionId)
                        }
                    }.decodeList<WasteEntrySimple>()

                organicTotal = entries.filter { it.wasteType == "organik" }.sumOf { it.quantity }
                inorganicTotal = entries.filter { it.wasteType == "anorganik" || it.wasteType == "daur_ulang" }.sumOf { it.quantity }
            }

            Result.success(Pair(organicTotal, inorganicTotal))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Award mission completion rewards: +85 XP and +100 coins.
     * Handles level-up logic (100 XP per level).
     */
    suspend fun completeMissionRewards(): Result<Unit> {
        return try {
            val session = supabase.auth.currentSessionOrNull()
            val userId = session?.user?.id ?: throw Exception("User not logged in")

            val profile = supabase.postgrest["profiles"]
                .select(Columns.ALL) {
                    filter { eq("id", userId) }
                }.decodeSingle<UserProfile>()

            val newTotalXp = profile.totalXp + 85
            val newCoins = profile.coins + 100
            val newLevel = (newTotalXp / 100) + 1

            supabase.postgrest["profiles"]
                .update({
                    set("total_xp", newTotalXp)
                    set("coins", newCoins)
                    set("level", newLevel)
                }) {
                    filter {
                        eq("id", userId)
                    }
                }

            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("AuthRepository", "completeMissionRewards error", e)
            Result.failure(e)
        }
    }

    /**
     * Reset today's daily mission progress to 0.
     * Called after claiming mystery box rewards.
     */
    suspend fun resetDailyMissionProgress(): Result<Unit> {
        return kotlinx.coroutines.withContext(kotlinx.coroutines.NonCancellable) {
            try {
                val session = supabase.auth.currentSessionOrNull()
                val userId = session?.user?.id ?: throw Exception("User not logged in")

                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val todayStr = dateFormat.format(java.util.Date())

                supabase.postgrest["daily_missions"]
                    .update({
                        set("scanned_count", 0)
                        set("is_completed", false)
                    }) {
                        filter {
                            eq("user_id", userId)
                            eq("date_recorded", todayStr)
                        }
                    }

                Result.success(Unit)
            } catch (e: Exception) {
                Log.e("AuthRepository", "resetDailyMissionProgress error", e)
                Result.failure(e)
            }
        }
    }
}
