package com.example.myrecipes.recipehome

import com.example.myrecipes.MainCoroutineRule
import com.example.myrecipes.FakeRecipeRepository
import com.example.myrecipes.data.source.Recipe
import com.example.myrecipes.home.HomeViewModel
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertThrows
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
    fun setupViewModel() {
        recipeRepository = FakeRecipeRepository()
        val recipe1 = Recipe(0,"Recipe 1","","tag1","","",0,0,0)
        val recipe2 = Recipe(1,"Recipe 2","","tag3","","",0,0,0)
        val recipe3 = Recipe(2,"Recipe 3","","tag1","","",0,0,0)

        runBlocking {
            recipeRepository.insertRecipe(recipe1)
            recipeRepository.insertRecipe(recipe2)
            recipeRepository.insertRecipe(recipe3)
        }

        recipeViewModel = HomeViewModel(recipeRepository)
    }
    @Test
    fun loadAllLocalRecipesFromRepository_loadingTogglesAndDataLoaded() = runTest {

        recipeViewModel.refreshList()

        assertThat(recipeViewModel.recipeUiState.first().isLoading).isTrue()

        advanceUntilIdle()

        assertThat(recipeViewModel.recipeUiState.first().isLoading).isFalse()

        assertThat(recipeViewModel.recipeUiState.first().items).hasSize(3)

    }

    @Test
    fun testGetLocalRecipesWithError() = runTest {

        recipeRepository.setShouldThrowError(true)
        recipeViewModel.refreshList()

        assertThat(recipeViewModel.recipeUiState.first().isLoading).isFalse()

        assertThat(recipeViewModel.recipeUiState.first().items).isEmpty()
        //assertThat(recipeViewModel.recipeUiState.first().userMessage).isEqualTo(R.string.)
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
            Recipe(1, "Recipe 1", "", "Vegetarian", "", "Vegetarian", 0, 0, 0),
            Recipe(3, "Recipe 3", "", "Vegetarian,Lunch", "", "Vegetarian,Lunch", 0, 0, 0)
        )
    }


}