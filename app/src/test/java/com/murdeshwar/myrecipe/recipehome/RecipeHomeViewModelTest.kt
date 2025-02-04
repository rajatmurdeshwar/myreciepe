package com.murdeshwar.myrecipe.recipehome

import com.murdeshwar.myrecipe.MainCoroutineRule
import com.murdeshwar.myrecipe.FakeRecipeRepository
import com.murdeshwar.myrecipe.data.source.Recipe
import com.murdeshwar.myrecipe.data.source.RecipeWithDetails
import com.murdeshwar.myrecipe.ui.home.HomeViewModel
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class RecipeHomeViewModelTest {

    // Subject under test
    private lateinit var recipeViewModel: HomeViewModel

    // Use a fake repository to be injected into the viewmodel
    private lateinit var recipeRepository: FakeRecipeRepository

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setupViewModel() = runTest{
        recipeRepository = FakeRecipeRepository()

        val recipe1 = RecipeWithDetails(
            recipe = Recipe(1, "Recipe 1", "", "Vegetarian", "", "", 0, 0, 0,false,false,false),
            ingredients = listOf(/* Add ingredient items if required */),
            steps = listOf(/* Add step items if required */)
        )

        val recipe2 = RecipeWithDetails(
            recipe = Recipe(0, "Recipe 2", "", "Non-Vegetarian", "", "", 0, 0, 0,false,false,false),
            ingredients = listOf(/* Add ingredient items if required */),
            steps = listOf(/* Add step items if required */)
        )

        val recipe3 = RecipeWithDetails(
            recipe = Recipe(3, "Recipe 3", "", "Vegetarian,Lunch", "", "", 0, 0, 0,false,false,false),
            ingredients = listOf(/* Add ingredient items if required */),
            steps = listOf(/* Add step items if required */)
        )

        // Insert RecipeWithDetails instances asynchronously
        recipeRepository.insertRecipe(recipe1)
        recipeRepository.insertRecipe(recipe2)
        recipeRepository.insertRecipe(recipe3)

        recipeViewModel = HomeViewModel(recipeRepository)
    }
    @Test
    fun loadAllLocalRecipesFromRepository_loadingTogglesAndDataLoaded() = runTest {

        recipeViewModel.refreshList()

        //assertThat(recipeViewModel.recipeUiState.first().isLoading).isTrue()

        advanceUntilIdle()

        //assertThat(recipeViewModel.recipeUiState.first().isLoading).isFalse()

        assertThat(recipeViewModel.recipeUiState.first().items).hasSize(3)

    }

    @Test
    fun testGetLocalRecipesWithError() = runTest {

        recipeRepository.setShouldThrowError(true)
        recipeViewModel.refreshList()

        assertThat(recipeViewModel.recipeUiState.first().isLoading).isFalse()

        assertThat(recipeViewModel.recipeUiState.first().items).isEmpty()
        assertThat(recipeViewModel.recipeUiState.first().userMessage).isEqualTo("Error loading recipes")
    }

    @Test
    fun getOnlineRecipesWithTags_loadingTogglesAndDataLoaded() = runTest {
        // Given a tag to filter recipes
        val tag = "Vegetarian"

        // When fetching online recipes with a specific tag
        recipeViewModel.getOnlineRecipesWithTags(tag)

        // Check that the loading state is true immediately after calling getOnlineRecipesWithTags
        assertThat(recipeViewModel.recipeUiState.first().isLoading).isTrue()

        // Wait for all tasks to complete
        advanceUntilIdle()

        // Check that the loading state is false after data is loaded
        assertThat(recipeViewModel.recipeUiState.first().isLoading).isFalse()

        // Check that the items contain the correct recipes with the given tag
        val filteredRecipes = recipeViewModel.recipeUiState.first().items
        assertThat(filteredRecipes).hasSize(2)
        assertThat(filteredRecipes).containsExactly(
            Recipe(1, "Recipe 1", "", "Vegetarian", "", "", 0, 0, 0,false,false,false),
            Recipe(3, "Recipe 3", "", "Vegetarian,Lunch", "", "", 0, 0, 0,false,false,false)
        )
    }


}