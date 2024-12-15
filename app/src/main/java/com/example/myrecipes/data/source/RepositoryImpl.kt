package com.example.myrecipes.data.source

import android.util.Log
import com.example.myrecipes.data.Repository
import com.example.myrecipes.data.source.local.RecipeDao
import com.example.myrecipes.data.source.network.NetworkDataSource
import com.example.myrecipes.data.source.network.NewRecipeApiService
import com.example.myrecipes.data.tUioExternal
import com.example.myrecipes.data.toExternal
import com.example.myrecipes.data.toLocal
import com.example.myrecipes.data.toRecipeSearchDataList
import com.example.myrecipes.data.toUiExternal
import com.example.myrecipes.data.toUiLocal
import com.example.myrecipes.di.FoodApi
import com.example.myrecipes.di.LocalApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val dao: RecipeDao,
    @FoodApi private val foodApi: NetworkDataSource,
    @LocalApi private val localApi: NewRecipeApiService
) : Repository {
    override suspend fun getLocalRecipes(): List<Recipe> {
        return withContext(Dispatchers.IO) {
            dao.getAllRecipes().tUioExternal()
        }
    }

    override suspend fun getRecipeById(recipeId: Int): Flow<RecipeWithDetails?> = flow {
        val localRecipeWithDetails = dao.getRecipeById(recipeId)
        val recipeWithDetails = localRecipeWithDetails?.let {
            RecipeWithDetails(
                recipe = it.recipe.toUiExternal(),
                ingredients = it.ingredients.map { localIngredient ->
                    Ingredient(
                        ingredientsId = localIngredient.ingredientId,
                        originalName = localIngredient.originalName,
                        amount = localIngredient.amount,
                        unit = localIngredient.unit
                    )
                },
                steps = it.steps.map { localStep ->
                    Step(
                        number = localStep.number,
                        step = localStep.step
                    )
                }
            )
        }
        emit(recipeWithDetails)
    }.flowOn(Dispatchers.IO)


    override suspend fun getOnlineRecipes(): List<Recipe?> {
        return try {
            foodApi.getRecipes(5).recipes.toUiLocal()

        } catch (e: Exception) {
            Log.e("RepositoryImpl","Exception "+e.message)
            emptyList<Recipe>()
        }
    }

    override suspend fun insertAllRecipes(recipe: List<RecipeWithDetails>) {
        dao.insertAllRecipesWithDetails(recipe.toLocal())
    }

    override suspend fun insertRecipe(recipe: RecipeWithDetails) {

        dao.insertRecipe(recipe.toLocal().recipe)
        dao.insertIngredients(recipe.toLocal().ingredients)
        dao.insertSteps(recipe.toLocal().steps)
    }

    override suspend fun searchRecipe(recipeName: String): List<RecipeSearchData?> {
        return try {
            foodApi.searchRecipe(recipeName).results.toRecipeSearchDataList()

        } catch (e: Exception) {
            Timber.tag("RepositoryImpl").e("Exception %s", e.message)
            emptyList<RecipeSearchData>()
        }
    }

    override suspend fun getOnlineRecipesWithTags(tags: String): List<Recipe?> {
        return try {
            foodApi.getRecipesWithTags(10,tags).recipes.toUiLocal()

        } catch (e: Exception) {
            Timber.tag("RepositoryImpl").e("Exception %s", e.message)
            emptyList<Recipe>()
        }
    }

    override suspend fun getRecipeDetailsById(id: Int): RecipeWithDetails? {
        return try {
            foodApi.getRecipeById(id).toLocal().toExternal()
        } catch (e: Exception) {
            Timber.tag("RepositoryImpl").e("Exception %s", e.message)
            null
        }
    }

    override suspend fun addRecipesToDb(recipe: Recipe) {
        try {
            val response = localApi.addRecipe(recipe)
            if (response.isSuccessful) {
                Timber.tag("RepositoryImpl").i("Recipe added successfully")
            } else {
                Timber.tag("RepositoryImpl").e("Failed to add recipe: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            Timber.tag("RepositoryImpl").e("Exception: ${e.message}")
        }

    }
}