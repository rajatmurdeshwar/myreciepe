package com.murdeshwar.myrecipe.data.source.local

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
    val servings: Int,
    val vegan: Boolean?,
    val glutenFree: Boolean?,
    val dairyFree: Boolean?
)

data class LocalRecipeWithDetails(
    val recipe: LocalRecipe,
    val ingredients: List<LocalIngredient>,
    val steps: List<LocalStep>
)