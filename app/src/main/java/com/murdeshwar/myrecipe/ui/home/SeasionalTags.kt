package com.murdeshwar.myrecipe.ui.home

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.format.DateTimeFormatter

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

    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentSeasonalTag(): String {
        val currentDate = LocalDate.now()
        val currentMonthDay = currentDate.format(DateTimeFormatter.ofPattern("MM-dd"))

        // Check for specific festivals/holidays
        seasonalMap[currentMonthDay]?.let {
            return it
        }

        // Default to seasons
        return when (currentDate.monthValue) {
            in 3..5 -> "spring"
            in 6..8 -> "summer"
            in 9..11 -> "autumn"
            else -> "winter"
        }
    }
}