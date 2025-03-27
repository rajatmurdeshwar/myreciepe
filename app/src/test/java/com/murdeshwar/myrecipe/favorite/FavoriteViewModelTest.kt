package com.murdeshwar.myrecipe.favorite

import com.google.common.truth.Truth.assertThat
import com.murdeshwar.myrecipe.FakeRecipeRepository
import com.murdeshwar.myrecipe.MainCoroutineRule
import com.murdeshwar.myrecipe.data.source.Ingredient
import com.murdeshwar.myrecipe.data.source.Recipe
import com.murdeshwar.myrecipe.data.source.RecipeWithDetails
import com.murdeshwar.myrecipe.data.source.Step
import com.murdeshwar.myrecipe.ui.favorite.FavoriteViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class FavoriteViewModelTest {

    // Subject under test
    private lateinit var favViewModel: FavoriteViewModel

    // Use a fake repository to be injected into the viewmodel
    private lateinit var recipeRepository: FakeRecipeRepository

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setupViewModel() = runTest {
        recipeRepository = FakeRecipeRepository()

        val recipe1 = RecipeWithDetails(
            recipe = Recipe(1, "Recipe 1", "", "Vegetarian", "", "", 0, 0, 0,false,false,false),
            ingredients = listOf(
                Ingredient(1,1,"Tomato", 2.0, "cups"),
                Ingredient(1,2,"Onion", 1.0, "piece")
            ),
            steps = listOf(
                Step(1, "Chop the vegetables."),
                Step(2, "Cook the vegetables.")
            )
        )

        val recipe2 = RecipeWithDetails(
            recipe = Recipe(0, "Recipe 2", "", "Non-Vegetarian", "", "", 0, 0, 0,false,false,false),
            ingredients = listOf(
                Ingredient(1,1,"Tomato", 2.0, "cups"),
                Ingredient(1,2,"Onion", 1.0, "piece")
            ),
            steps = listOf(
                Step(1, "Chop the vegetables."),
                Step(2, "Cook the vegetables.")
            )
        )

        val recipe3 = RecipeWithDetails(
            recipe = Recipe(3, "Recipe 3", "", "Vegetarian,Lunch", "", "", 0, 0, 0,false,false,false),
            ingredients = listOf(
                Ingredient(1,1,"Tomato", 2.0, "cups"),
                Ingredient(1,2,"Onion", 1.0, "piece")
            ),
            steps = listOf(
                Step(1, "Chop the vegetables."),
                Step(2, "Cook the vegetables.")
            )
        )

        // Insert recipes into the fake repository
        runBlocking {
            recipeRepository.insertRecipe(recipe1)
            recipeRepository.insertRecipe(recipe2)
            recipeRepository.insertRecipe(recipe3)
        }
        favViewModel = FavoriteViewModel(recipeRepository)
    }

    @Test
    fun `getLocalRecipes should update UI state with recipes when repository returns data`() = runTest {
        // Given: A list of local recipes

        // When: ViewModel fetches local recipes
        favViewModel.getLocalRecipes()

        // Then: UI state should be updated
        val state = favViewModel.recipeUiState.first()
        assertThat(state.emptyItems).isFalse()
        assertThat(state.items.size).isEqualTo(3)
    }

    @Test
    fun `getLocalRecipes should handle repository errors correctly`() = runTest {
        // Given: The repository is set to throw an error
        recipeRepository.setShouldThrowError(true)

        // When: ViewModel fetches local recipes
        favViewModel.getLocalRecipes()
        // Then: UI state should remain unchanged or indicate an error
        val state = favViewModel.recipeUiState.first()


        assertThat(state.items).isEmpty() // No items should be available
        assertThat(state.emptyItems).isTrue() // Should indicate empty state
    }

}