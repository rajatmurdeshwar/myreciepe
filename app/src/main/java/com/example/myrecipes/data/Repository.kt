package com.example.myrecipes.data

import com.example.myrecipes.data.source.Recipe
import com.example.myrecipes.data.source.local.LocalRecipe
import com.example.myrecipes.data.source.local.RecipeDao
import com.example.myrecipes.data.source.network.NetworkRecipe
import com.example.myrecipes.data.source.network.RecipeSearchResult
import com.example.myrecipes.data.source.network.Recipes
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject


interface Repository {
    suspend fun getLocalRecipes(): List<Recipe>?

    suspend fun getRecipeById(recipeId: Int): Flow<LocalRecipe?>

    suspend fun getOnlineRecipes(): NetworkRecipe?

    suspend fun insertAllRecipes(recipe: List<LocalRecipe>)

    suspend fun searchRecipe(recipeName: String): RecipeSearchResult?
}

