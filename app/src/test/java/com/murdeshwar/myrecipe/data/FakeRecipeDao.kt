package com.murdeshwar.myrecipe.data

import com.murdeshwar.myrecipe.data.source.local.LocalIngredient
import com.murdeshwar.myrecipe.data.source.local.LocalRecipe
import com.murdeshwar.myrecipe.data.source.local.LocalStep
import com.murdeshwar.myrecipe.data.source.local.RecipeDao
import com.murdeshwar.myrecipe.data.source.local.RecipeWithIngredientsAndSteps

class FakeRecipeDao(): RecipeDao {

    override suspend fun insertRecipe(recipe: LocalRecipe): Long {
        TODO("Not yet implemented")
    }

    override suspend fun insertIngredients(ingredients: List<LocalIngredient>) {
        TODO("Not yet implemented")
    }

    override suspend fun insertSteps(steps: List<LocalStep>) {
        TODO("Not yet implemented")
    }

    override suspend fun getAllRecipes(): List<LocalRecipe> {
        TODO("Not yet implemented")
    }

    override suspend fun getRecipeById(recipeId: Int): RecipeWithIngredientsAndSteps {
        TODO("Not yet implemented")
    }
}