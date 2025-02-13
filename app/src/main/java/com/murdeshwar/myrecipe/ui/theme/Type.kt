package com.murdeshwar.myrecipe.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.murdeshwar.myrecipe.R

// Set of Material typography styles to start with
val Typography = Typography(
    // For paragraph text (recipe descriptions, instructions)
    bodyLarge = TextStyle(
        fontFamily = FontFamily(Font(R.font.nunito_regular)), // Custom font
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
        color = Color(0xFFE0E0E0) // Light gray for dark mode
    ),

    // For recipe titles
    titleLarge = TextStyle(
        fontFamily = FontFamily(Font(R.font.playfair_display_bold)), // Elegant serif font
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = 30.sp,
        letterSpacing = 0.sp,
        color = Color.White // Bright contrast in dark mode
    ),

    // For category headings (e.g., Breakfast, Lunch, Dinner)
    headlineMedium = TextStyle(
        fontFamily = FontFamily(Font(R.font.montserrat_semibold)), // Modern sans-serif
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = 26.sp,
        letterSpacing = 0.15.sp,
        color = Color(0xFFFFD700) // Gold for emphasis
    ),

    // For button text (e.g., "Add to Favorites", "View Recipe")
    labelLarge = TextStyle(
        fontFamily = FontFamily(Font(R.font.roboto_medium)), // Clean and readable
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 1.sp, // Slight spacing for clarity
        color = Color.White
    ),

    // For ingredient lists
    bodyMedium = TextStyle(
        fontFamily = FontFamily(Font(R.font.nunito_light)),
        fontWeight = FontWeight.Light,
        fontSize = 14.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.25.sp,
        color = Color(0xFFBDBDBD) // Soft gray
    ),

    // For small captions (e.g., "Recipe by Chef John")
    labelSmall = TextStyle(
        fontFamily = FontFamily(Font(R.font.montserrat_light)),
        fontWeight = FontWeight.Light,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp,
        color = Color(0xFF6c757d) // Dimmed text for subtlety
    )
)
