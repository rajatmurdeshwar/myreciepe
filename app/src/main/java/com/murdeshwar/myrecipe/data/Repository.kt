package com.murdeshwar.myrecipe.data

import com.murdeshwar.myrecipe.data.source.LoginUser
import com.murdeshwar.myrecipe.data.source.Recipe
import com.murdeshwar.myrecipe.data.source.RecipeSearchData
import com.murdeshwar.myrecipe.data.source.RecipeWithDetails
import com.murdeshwar.myrecipe.data.source.User
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

    suspend fun addRecipesToDb(recipe: Recipe)

    suspend fun loginUser(user: LoginUser)

    suspend fun signupUser(user: User)
}

