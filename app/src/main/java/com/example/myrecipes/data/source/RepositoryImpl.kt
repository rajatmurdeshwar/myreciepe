package com.example.myrecipes.data.source

import com.example.myrecipes.data.Repository
import com.example.myrecipes.data.source.local.LocalRecipe
import com.example.myrecipes.data.source.local.RecipeDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val dao: RecipeDao,
) : Repository {
    override suspend fun getAllRecipes(): Flow<List<LocalRecipe>> {
        return withContext(Dispatchers.IO) {
            dao.getAllRecipes()
        }
    }

}