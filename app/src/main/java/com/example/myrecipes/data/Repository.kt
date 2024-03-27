package com.example.myrecipes.data

import com.example.myrecipes.data.source.local.LocalRecipe
import com.example.myrecipes.data.source.local.RecipeDao
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject


interface Repository {
    suspend fun getAllRecipes(): Flow<List<LocalRecipe>>
}

