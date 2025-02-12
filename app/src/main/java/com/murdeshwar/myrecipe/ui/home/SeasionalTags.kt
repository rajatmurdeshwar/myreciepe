package com.murdeshwar.myrecipe.ui.home

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object SeasonalTags {
    private val seasonalMap = mapOf(
        // Festivals and Holidays
        "01-01" to "new-year", // New Year
        "02-14" to "valentines-day", // Valentine's Day
        "10-31" to "halloween", // Halloween
        "12-25" to "christmas", // Christmas

        // Seasons
        "03-01" to "spring", // Spring (March to May)
        "06-01" to "summer", // Summer (June to August)
        "09-01" to "autumn", // Autumn (September to November)
        "12-01" to "winter" // Winter (December to February)
    )

    fun getCurrentSeasonalTag(): String {
        val calendar = Calendar.getInstance()
        val formatter = SimpleDateFormat("MM-dd", Locale.getDefault())
        val currentMonthDay = formatter.format(calendar.time)

        // Check for specific festivals/holidays
        seasonalMap[currentMonthDay]?.let {
            return it
        }

        // Default to seasons
        return when (calendar.get(Calendar.MONTH) + 1) {
            in 3..5 -> "spring"
            in 6..8 -> "summer"
            in 9..11 -> "autumn"
            else -> "winter"
        }
    }
}