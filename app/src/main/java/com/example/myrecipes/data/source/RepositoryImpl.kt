package com.example.myrecipes.data.source

import android.util.Log
import com.example.myrecipes.data.Repository
import com.example.myrecipes.data.source.local.RecipeDao
import com.example.myrecipes.data.source.network.NetworkDataSource
import com.example.myrecipes.data.toExternal
import com.example.myrecipes.data.toLocal
import com.example.myrecipes.data.toRecipeSearchDataList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val dao: RecipeDao,
    private val networkData: NetworkDataSource
) : Repository {
    override suspend fun getLocalRecipes(): List<Recipe> {
        return withContext(Dispatchers.IO) {
            dao.getAllRecipes().toExternal()
        }
    }

    override suspend fun getRecipeById(recipeId: Int): Flow<Recipe?> {
        return withContext(Dispatchers.IO) {
            dao.getById(recipeId).map { localRecipe ->
                localRecipe?.toExternal()
            }
        }
    }

    override suspend fun getOnlineRecipes(): List<Recipe?> {
        return try {
            networkData.getRecipes(5).recipes.toExternal()

        } catch (e: Exception) {
            Log.e("RepositoryImpl","Exception "+e.message)
            emptyList<Recipe>()
        }
    }

    override suspend fun insertAllRecipes(recipe: List<Recipe>) {
        dao.insertAll(recipe.toLocal())
    }

    override suspend fun insertRecipe(recipe: Recipe) {
        dao.insertRecipe(recipe.toLocal())
    }

    override suspend fun searchRecipe(recipeName: String): List<RecipeSearchData?> {
        return try {
            networkData.searchRecipe(recipeName).results.toRecipeSearchDataList()

        } catch (e: Exception) {
            Log.e("RepositoryImpl","Exception "+e.message)
            emptyList<RecipeSearchData>()
        }
    }

    override suspend fun getOnlineRecipesWithTags(tags: String): List<Recipe?> {
        return try {
            networkData.getRecipesWithTags(10,tags).recipes.toExternal()

        } catch (e: Exception) {
            Log.e("RepositoryImpl","Exception "+e.message)
            emptyList<Recipe>()
        }
    }

    override suspend fun getRecipeDetailsById(id: Int): Recipe? {
        return try {
            networkData.getRecipeById(id).toExternal()
        } catch (e: Exception) {
            Log.e("RepositoryImpl","Exception "+e.message)
            null
        }
    }
}