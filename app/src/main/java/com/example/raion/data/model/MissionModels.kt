package com.example.raion.data.model

/**
 * Steps dalam mission wizard flow.
 */
enum class MissionStep {
    JOURNEY,          // Landing: profil + progress + "Mulai Kumpulkan!"
    INTRO,            // Motivasi: "Ayo ceritakan aksi barumu!"
    SELECT_TYPE,      // Pilih: Organik / Daur Ulang
    SELECT_SUBTYPE,   // Pilih sub-tipe (dari waste_categories)
    SELECT_LOCATION,  // Pilih: Kantin / Ruang Kelas / Halaman / Toilet
    SELECT_QUANTITY,  // Pilih: 1-4 Buah + Lainnya
    RESULT            // Misi Selesai / Belum Selesai
}

/**
 * UI state untuk seluruh mission wizard.
 */
data class MissionUiState(
    val step: MissionStep = MissionStep.JOURNEY,
    val isLoading: Boolean = true,

    // Mission progress
    val scannedCount: Int = 0,
    val targetCount: Int = 5,
    val isMissionComplete: Boolean = false,

    // Wizard selections (reset per entry)
    val selectedType: String? = null,         // "organik" / "daur_ulang"
    val selectedSubtype: String? = null,      // "buah", "sayur", "kaleng", etc
    val selectedLocation: String? = null,
    val selectedQuantity: Int = 1,

    // Result from last RPC call
    val lastGainedXp: Int = 0,
    val lastGainedCoins: Int = 0,
    val totalGainedXp: Int = 0,
    val totalGainedCoins: Int = 0,

    // Reference data from DB
    val categories: List<WasteCategory> = emptyList(),

    // User info (for Journey header)
    val userName: String = "",
    val userLevel: Int = 1,
    val userXp: Int = 0,
    val userCoins: Int = 0,
    val userRank: Int = 5,
    val avatarUrl: String = "",

    val errorMessage: String? = null
)
