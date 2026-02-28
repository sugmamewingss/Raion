package com.example.raion.data.repository

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.rpc
import kotlinx.serialization.json.put
import kotlinx.serialization.json.buildJsonObject
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

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
}
