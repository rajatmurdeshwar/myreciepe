package com.murdeshwar.myrecipe.ui.onboarding

import androidx.annotation.DrawableRes
import com.murdeshwar.myrecipe.R

data class Page(
    val title: String,
    val description:String,
    @DrawableRes val image : Int,
)

val pages = listOf(
    Page(
        "Welcome",
        "Your Personal Recipe Book, Discover, Cook, and Enjoy Delicious Recipes!",
        R.drawable.recipe_book_onboard
    ),
    Page(
        "Search & Filter Recipes",
        "Find the best recipes for every occasion!",
        R.drawable.bookmarks_onboard
    ),
    Page(
        "Save & Customize",
        "Save your favorite recipes and get personalized recommendations!",
        R.drawable.chef_onboard
    )
)