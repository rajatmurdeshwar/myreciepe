package com.murdeshwar.myrecipe.data.source

import com.murdeshwar.myrecipe.data.Repository
import com.murdeshwar.myrecipe.data.source.local.RecipeDao
import com.murdeshwar.myrecipe.data.source.network.LoginResponse
import com.murdeshwar.myrecipe.data.source.network.NetworkDataSource
import com.murdeshwar.myrecipe.data.source.network.NewRecipeApiService
import com.murdeshwar.myrecipe.data.tUioExternal
import com.murdeshwar.myrecipe.data.toExternal
import com.murdeshwar.myrecipe.data.toLocal
import com.murdeshwar.myrecipe.data.toRecipeSearchDataList
import com.murdeshwar.myrecipe.data.toUiExternal
import com.murdeshwar.myrecipe.data.toUiLocal
import com.murdeshwar.myrecipe.di.FoodApi
import com.murdeshwar.myrecipe.di.LocalApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import org.json.JSONObject
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
            Timber.tag("RepositoryImpl").e("Exception %s", e.message)
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
                val error = response.errorBody()?.string() ?: "Unknown error"

                Timber.tag("RepositoryImpl").e("Failed to add recipe. Error: $error, Code: ${response.code()}")
            }
        } catch (e: Exception) {
            Timber.tag("RepositoryImpl").e("Exception: ${e.message}")
        }

    }

    override suspend fun loginUser(user: LoginUser): String {
        return try {
            val response = localApi.login(user)
            if (response.isSuccessful) {
                val loginResponse = response.body()
                if (loginResponse != null) {
                    Timber.tag("RepositoryImpl").i("Login successful: ${loginResponse.token}")
                    loginResponse.token // Return the token
                } else {
                    Timber.tag("RepositoryImpl").e("Empty response body")
                    throw Exception("Login failed: Empty response body")
                }
            } else {
                val error = response.errorBody()?.string() ?: "Unknown error"
                Timber.tag("RepositoryImpl").e("Login failed: $error")
                throw Exception("Login failed: $error")
            }
        } catch (e: Exception) {
            Timber.tag("RepositoryImpl").e("Exception occurred during login: ${e.message}")
            throw e
        }
    }


    override suspend fun signupUser(user: User) {
        try {
            val response = localApi.signup(user)
            if (response.isSuccessful) {
                Timber.tag("RepositoryImpl").i("User registered successfully")
            } else {
                val error = response.errorBody()?.string() ?: "Unknown error"
                Timber.tag("RepositoryImpl").e("Signup failed: $error")
            }
        } catch (e: Exception) {
            Timber.tag("RepositoryImpl").e("Exception occurred during signup: ${e.message}")
        }
    }
}