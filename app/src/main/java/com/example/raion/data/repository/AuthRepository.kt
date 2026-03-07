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

            val userSession = supabase.auth.signUpWith(Email) {
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
            val fullName = user?.userMetadata?.get("full_name")?.jsonPrimitive?.content ?: "Sobat Raion"
            
            // Mengambil kata pertama (nama panggilan)
            fullName.split(" ").firstOrNull()?.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() } ?: "Sobat Raion"
        } catch (e: Exception) {
            "Sobat Raion"
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
                    order("total_xp", Order.DESCENDING)
                    limit(limit)
                }.decodeList<UserProfile>()
            Result.success(profiles)
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
}
