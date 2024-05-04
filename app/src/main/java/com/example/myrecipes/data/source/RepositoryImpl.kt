package com.example.myrecipes.data.source

import android.util.Log
import com.example.myrecipes.data.Repository
import com.example.myrecipes.data.source.local.LocalRecipe
import com.example.myrecipes.data.source.local.RecipeDao
import com.example.myrecipes.data.source.network.NetworkDataSource
import com.example.myrecipes.data.source.network.NetworkRecipe
import com.example.myrecipes.data.source.network.RecipeSearchResult
import com.example.myrecipes.data.source.network.Recipes
import com.example.myrecipes.data.toExternal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val dao: RecipeDao,
    private val networkData: NetworkDataSource
) : Repository {
    override suspend fun getLocalRecipes(): List<LocalRecipe> {
        return withContext(Dispatchers.IO) {
            dao.getAllRecipes()
        }
    }

    override suspend fun getRecipeById(recipeId: Int): Flow<LocalRecipe?> {
        return withContext(Dispatchers.IO) {
            dao.getById(recipeId)
        }
    }

    override suspend fun getOnlineRecipes(): NetworkRecipe? {
        return try {
            networkData.getRecipes(5)

        } catch (e: Exception) {
            Log.e("RepositoryImpl","Exception "+e.message)
            null
        }
    }

    override suspend fun insertAllRecipes(recipe: List<LocalRecipe>) {
        dao.insertAll(recipe)
    }

    override suspend fun insertRecipe(recipe: LocalRecipe) {
        dao.insertRecipe(recipe)
    }

    override suspend fun searchRecipe(recipeName: String): RecipeSearchResult? {
        return try {
            networkData.searchRecipe(recipeName)

        } catch (e: Exception) {
            Log.e("RepositoryImpl","Exception "+e.message)
            null
        }
    }

    override suspend fun getOnlineRecipesWithTags(tags: String): NetworkRecipe? {
        return try {
            networkData.getRecipesWithTags(10,tags)

        } catch (e: Exception) {
            Log.e("RepositoryImpl","Exception "+e.message)
            null
        }
    }

    override suspend fun getRecipeDetailsById(id: Int): Recipes? {
        return try {
            networkData.getRecipeById(id)
        } catch (e: Exception) {
            Log.e("RepositoryImpl","Exception "+e.message)
            null
        }
    }
}