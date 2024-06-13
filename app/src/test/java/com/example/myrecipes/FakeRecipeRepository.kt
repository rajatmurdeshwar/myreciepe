package com.example.myrecipes

import com.example.myrecipes.data.Repository
import com.example.myrecipes.data.source.Recipe
import com.example.myrecipes.data.source.RecipeSearchData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import java.lang.Exception

class FakeRecipeRepository: Repository {

    private var shouldThrowError = false

    private val _savedRecipe = MutableStateFlow(LinkedHashMap<Int, Recipe>())
    val savedRecipe: StateFlow<LinkedHashMap<Int, Recipe>> = _savedRecipe.asStateFlow()

    private val observableRecipes: Flow<List<Recipe>> = savedRecipe.map {
        if (shouldThrowError) {
            throw Exception("Test exception")
        } else {
            it.values.toList()
        }
    }

    fun setShouldThrowError(value: Boolean) {
        shouldThrowError = value
    }

    override suspend fun getLocalRecipes(): List<Recipe>? {
        if (shouldThrowError) {
            throw Exception("Test exception")
        }
        return observableRecipes.first()
    }

    override suspend fun getRecipeById(recipeId: Int): Flow<Recipe?> {
        return savedRecipe.map { recipes ->
            recipes[recipeId]
        }
    }

    override suspend fun getOnlineRecipes(): List<Recipe?> {
        if (shouldThrowError) {
            throw Exception("Test exception")
        }
        return observableRecipes.first()
    }

    override suspend fun insertAllRecipes(recipe: List<Recipe>) {
        _savedRecipe.update { oldRecipe ->
            val newRecipe = LinkedHashMap(oldRecipe)
            for (recipee in recipe) {
                newRecipe[recipee.id] = recipee
            }
            newRecipe
        }
    }

    override suspend fun insertRecipe(recipe: Recipe) {
        _savedRecipe.update { oldRecipes ->
            val newRecipes = LinkedHashMap(oldRecipes)
            newRecipes[recipe.id] = recipe
            newRecipes
        }
    }

    override suspend fun searchRecipe(recipeName: String): List<RecipeSearchData?> {
        if (shouldThrowError) {
            throw Exception("Test exception")
        }
        return observableRecipes.first()
            .filter { it.title.contains(recipeName, ignoreCase = true) }
            .map { RecipeSearchData(it.id, it.title, "","") }
    }

    override suspend fun getOnlineRecipesWithTags(tags: String): List<Recipe?> {
        if (shouldThrowError) {
            throw Exception("Test exception")
        }
        return observableRecipes.first()
            .filter { it.category.contains(tags) }
    }

    override suspend fun getRecipeDetailsById(id: Int): Recipe? {
        if (shouldThrowError) {
            throw Exception("Test exception")
        }
        return savedRecipe.value[id]
    }
}