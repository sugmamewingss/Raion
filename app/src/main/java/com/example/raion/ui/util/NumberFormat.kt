package com.example.raion.ui.util

/**
 * Formats a number with compact notation (1000 → 1k, 1500 → 1.5k, 10000 → 10k, 1000000 → 1M).
 * Removes trailing ".0" for clean output (e.g., 2000 → 2k, not 2.0k).
 */
fun formatCompactNumber(value: Int): String {
    return when {
        value >= 1_000_000 -> {
            val m = value / 1_000_000.0
            if (m % 1.0 == 0.0) "${m.toInt()}M" else "${"%.1f".format(m)}M"
        }
        value >= 1_000 -> {
            val k = value / 1_000.0
            if (k % 1.0 == 0.0) "${k.toInt()}k" else "${"%.1f".format(k)}k"
        }
        else -> value.toString()
    }
}
