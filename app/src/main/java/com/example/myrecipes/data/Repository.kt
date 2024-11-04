package com.example.myrecipes.data

import com.example.myrecipes.data.source.Recipe
import com.example.myrecipes.data.source.RecipeSearchData
import com.example.myrecipes.data.source.RecipeWithDetails
import kotlinx.coroutines.flow.Flow



interface Repository {
    suspend fun getLocalRecipes(): List<Recipe>?

    suspend fun getRecipeById(recipeId: Int): Flow<RecipeWithDetails?>

    suspend fun getOnlineRecipes(): List<Recipe?>

    suspend fun insertAllRecipes(recipe: List<RecipeWithDetails>)

    suspend fun insertRecipe(recipe: RecipeWithDetails)

    suspend fun searchRecipe(recipeName: String): List<RecipeSearchData?>

    suspend fun getOnlineRecipesWithTags(tags: String): List<Recipe?>

    suspend fun getRecipeDetailsById(id: Int): RecipeWithDetails?
}

