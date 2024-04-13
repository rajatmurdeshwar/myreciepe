package com.example.myrecipes.data.source.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")
data class LocalRecipe(
    @PrimaryKey
    val id: Int,
    val title: String,
    val description: String,
    val category: String,
    val instructions: String,
    val itemImage: String,
    val healthScore: Int,
    val readyIn: Int,
    val servings: Int
)