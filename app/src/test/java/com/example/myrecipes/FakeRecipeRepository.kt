package com.example.myrecipes

import com.example.myrecipes.data.Repository
import com.example.myrecipes.data.source.Recipe
import com.example.myrecipes.data.source.RecipeSearchData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import java.lang.Exception

class FakeRecipeRepository: Repository {

    private var shouldThrowError = false

    private val _savedRecipe = MutableStateFlow(LinkedHashMap<String, Recipe>())
    val savedRecipe: StateFlow<LinkedHashMap<String, Recipe>> = _savedRecipe.asStateFlow()

    private val observableRecipes: Flow<List<Recipe>> = savedRecipe.map {
        if (shouldThrowError) {
            throw Exception("Test exception")
        } else {
            it.values.toList()
        }
    }

    override suspend fun getLocalRecipes(): List<Recipe>? {
        TODO("Not yet implemented")
    }

    override suspend fun getRecipeById(recipeId: Int): Flow<Recipe?> {
        TODO("Not yet implemented")
    }

    override suspend fun getOnlineRecipes(): List<Recipe?> {
        TODO("Not yet implemented")
    }

    override suspend fun insertAllRecipes(recipe: List<Recipe>) {
        TODO("Not yet implemented")
    }

    override suspend fun insertRecipe(recipe: Recipe) {
        TODO("Not yet implemented")
    }

    override suspend fun searchRecipe(recipeName: String): List<RecipeSearchData?> {
        TODO("Not yet implemented")
    }

    override suspend fun getOnlineRecipesWithTags(tags: String): List<Recipe?> {
        TODO("Not yet implemented")
    }

    override suspend fun getRecipeDetailsById(id: Int): Recipe? {
        TODO("Not yet implemented")
    }
}