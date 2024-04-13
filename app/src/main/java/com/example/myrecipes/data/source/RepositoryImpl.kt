package com.example.myrecipes.data.source

import android.util.Log
import com.example.myrecipes.data.Repository
import com.example.myrecipes.data.source.local.LocalRecipe
import com.example.myrecipes.data.source.local.RecipeDao
import com.example.myrecipes.data.source.network.NetworkDataSource
import com.example.myrecipes.data.source.network.NetworkRecipe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val dao: RecipeDao,
    private val networkData: NetworkDataSource
) : Repository {
    override suspend fun getAllRecipes(): Flow<List<LocalRecipe>> {
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
            networkData.getRecipes()

        } catch (e: Exception) {
            Log.d("RepositoryImpl","Exception "+e.message)
            null
        }
    }

    override suspend fun insertAllRecipes(recipe: List<LocalRecipe>) {
        dao.insertAll(recipe)
    }
}