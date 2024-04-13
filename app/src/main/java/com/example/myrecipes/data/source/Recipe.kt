package com.example.myrecipes.data.source

data class Recipe(
    val id: Int =0,
    val title: String = "",
    val description: String = "",
    val category: String = "",
    val instructions: String = "",
    val itemImage: String ="",
    val healthScore: Int =0,
    val readyIn: Int=0,
    val servings: Int=0
)