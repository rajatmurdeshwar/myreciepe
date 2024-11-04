package com.example.myrecipes.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface RecipeDao  {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipe: LocalRecipe): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIngredients(ingredients: List<LocalIngredient>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSteps(steps: List<LocalStep>)

    @Transaction
    suspend fun insertAllRecipesWithDetails(recipesWithDetails: List<LocalRecipeWithDetails>) {
        recipesWithDetails.forEach { recipeWithDetails ->
            val recipeId = insertRecipe(recipeWithDetails.recipe).toInt()  // Insert recipe and get the ID
            // Insert ingredients associated with this recipe
            insertIngredients(recipeWithDetails.ingredients.map {
                it.copy(recipeId = recipeId)  // Set the foreign key before insertion
            })
            // Insert steps associated with this recipe
            insertSteps(recipeWithDetails.steps.map {
                it.copy(recipeId = recipeId)  // Set the foreign key before insertion
            })
        }
    }

    // Get all recipes with ingredients and steps
    @Transaction
    @Query("SELECT * FROM recipes")
    suspend fun getAllRecipes(): List<LocalRecipe>

    // Get a specific recipe by its ID
    @Transaction
    @Query("SELECT * FROM recipes WHERE id = :recipeId")
    suspend fun getRecipeById(recipeId: Int): RecipeWithIngredientsAndSteps

}