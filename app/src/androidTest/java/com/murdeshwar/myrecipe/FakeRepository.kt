package com.murdeshwar.myrecipe

import com.murdeshwar.myrecipe.data.Repository
import com.murdeshwar.myrecipe.data.source.Ingredient
import com.murdeshwar.myrecipe.data.source.LoginUser
import com.murdeshwar.myrecipe.data.source.Recipe
import com.murdeshwar.myrecipe.data.source.RecipeSearchData
import com.murdeshwar.myrecipe.data.source.RecipeWithDetails
import com.murdeshwar.myrecipe.data.source.Step
import com.murdeshwar.myrecipe.data.source.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import java.lang.Exception

class FakeRepository: Repository {

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

    // Pre-populate with mock data for testing
    init {
        val mockRecipe = RecipeWithDetails(
            recipe = Recipe(1, "Recipe Title", "Description", "Vegetarian", "", "", 4, 80, 30, true, false, true),
            ingredients = listOf(
                Ingredient(1,1,"Tomato", 2.0, "cups"),
                Ingredient(1,2,"Onion", 1.0, "piece")
            ),
            steps = listOf(
                Step(1, "Chop the vegetables."),
                Step(2, "Cook the vegetables.")
            )
        )
        _savedRecipe.value[1] = mockRecipe
    }

    override suspend fun getLocalRecipes(): List<Recipe> {
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

    override suspend fun addRecipesToDb(recipe: Recipe) {
        // Simulate adding a recipe to the remote database
        _savedRecipe.update { oldRecipes ->
            val newRecipes = LinkedHashMap(oldRecipes)
            val recipeWithDetails = RecipeWithDetails(
                recipe = recipe,
                ingredients = emptyList(),
                steps = emptyList()
            )
            newRecipes[recipe.recipeId] = recipeWithDetails
            newRecipes
        }
    }

    override suspend fun userDetails(): User {
        TODO("Not yet implemented")
    }

    override suspend fun loginUser(user: LoginUser): String? {
        TODO("Not yet implemented")
    }

    override suspend fun signupUser(user: User) {
        TODO("Not yet implemented")
    }
}