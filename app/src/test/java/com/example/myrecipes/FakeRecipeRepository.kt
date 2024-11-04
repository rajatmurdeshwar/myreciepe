package com.example.myrecipes

import com.example.myrecipes.data.Repository
import com.example.myrecipes.data.source.Recipe
import com.example.myrecipes.data.source.RecipeSearchData
import com.example.myrecipes.data.source.RecipeWithDetails
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

    private val _savedRecipe = MutableStateFlow(LinkedHashMap<Int, RecipeWithDetails>())
    val savedRecipe: StateFlow<LinkedHashMap<Int, RecipeWithDetails>> = _savedRecipe.asStateFlow()

    private val observableRecipes: Flow<List<RecipeWithDetails>> = savedRecipe.map {
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
        return observableRecipes.first().map {
            it.recipe
        }
    }

    override suspend fun getRecipeById(recipeId: Int): Flow<RecipeWithDetails?> {
        return savedRecipe.map { recipes ->
            recipes.get(recipeId)
        }
    }

    override suspend fun getOnlineRecipes(): List<Recipe?> {
        if (shouldThrowError) {
            throw Exception("Test exception")
        }
        return observableRecipes.first().map {
            it.recipe
        }
    }

    override suspend fun insertAllRecipes(recipe: List<RecipeWithDetails>) {
        _savedRecipe.update { oldRecipe ->
            val newRecipe = LinkedHashMap(oldRecipe)
            for (recipee in recipe) {
                newRecipe[recipee.recipe.recipeId] = recipee
            }
            newRecipe
        }
    }

    override suspend fun insertRecipe(recipe: RecipeWithDetails) {
        _savedRecipe.update { oldRecipes ->
            val newRecipes = LinkedHashMap(oldRecipes)
            newRecipes[recipe.recipe.recipeId] = recipe
            newRecipes
        }
    }

    override suspend fun searchRecipe(recipeName: String): List<RecipeSearchData?> {
        if (shouldThrowError) {
            throw Exception("Test exception")
        }
        return observableRecipes.first()
            .filter { it.recipe.title.contains(recipeName, ignoreCase = true) }
            .map { RecipeSearchData(it.recipe.recipeId, it.recipe.title, "","") }
    }

    override suspend fun getOnlineRecipesWithTags(tags: String): List<Recipe?> {
        if (shouldThrowError) {
            throw Exception("Test exception")
        }
        return observableRecipes.first()
            .filter { it.recipe.category.split(",").contains(tags) } // Adjust to split tags by comma
            .map { it.recipe }
    }


    override suspend fun getRecipeDetailsById(id: Int): RecipeWithDetails? {
        if (shouldThrowError) {
            throw Exception("Test exception")
        }
        return savedRecipe.value[id]
    }
}