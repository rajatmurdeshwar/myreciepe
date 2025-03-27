package com.murdeshwar.myrecipe.recipeDetails

import com.google.common.truth.Truth.assertThat
import com.murdeshwar.myrecipe.FakeRecipeRepository
import com.murdeshwar.myrecipe.MainCoroutineRule
import com.murdeshwar.myrecipe.data.source.Recipe
import com.murdeshwar.myrecipe.data.source.RecipeWithDetails
import com.murdeshwar.myrecipe.ui.details.RecipeDetailsViewModel
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class RecipeDetailsViewModelTest {

    private lateinit var viewModel: RecipeDetailsViewModel

    private lateinit var recipeRepository: FakeRecipeRepository

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setupViewModel() = runTest{
        recipeRepository = FakeRecipeRepository()

        val recipe1 = RecipeWithDetails(
            recipe = Recipe(1, "Recipe 1", "", "Vegetarian", "", "", 6, 40, 4,false,false,false),
            ingredients = listOf(/* Add ingredient items if required */),
            steps = listOf(/* Add step items if required */)
        )

        // Insert RecipeWithDetails instances asynchronously
        recipeRepository.insertRecipe(recipe1)

        viewModel = RecipeDetailsViewModel(recipeRepository)
    }

    @Test
    fun `getRecipeByID should update state when repository has data`() = runTest {
        // Act: Fetch recipe by ID
        viewModel.getRecipeByID(1)

        // Assert: Check if ViewModel updates state correctly
        advanceUntilIdle()

        assertThat(viewModel.recipe.first().recipeWithDetails?.recipe?.recipeId).isEqualTo(1)
        assertThat(viewModel.recipe.first().isLoading).isFalse()
    }

    @Test
    fun `saveRecipeToRemoteAndLocal should update userMessage on success`() = runTest {
        val recipe = RecipeWithDetails(
            recipe = Recipe(3, "New Recipe", "", "Vegan", "", "", 0, 0, 0, false, false, false),
            ingredients = listOf(),
            steps = listOf()
        )

        // Act: Save the recipe
        viewModel.saveRecipeToRemoteAndLocal(recipe)

        // Assert: Ensure success message is set
        advanceUntilIdle()
        assertThat(viewModel.userMessage.first()).isEqualTo("Recipe successfully saved! 🎉")
    }

    @Test
    fun `saveRecipeToRemoteAndLocal should update userMessage on failure`() = runTest {
        val recipe = RecipeWithDetails(
            recipe = Recipe(4, "Error Recipe", "", "Dessert", "", "", 0, 0, 0, false, false, false),
            ingredients = listOf(),
            steps = listOf()
        )

        // Simulate an error in repository
        recipeRepository.setShouldThrowError(true)

        // Act: Attempt to save recipe
        viewModel.saveRecipeToRemoteAndLocal(recipe)

        // Assert: Ensure error message is set
        advanceUntilIdle()
        assertThat(viewModel.userMessage.first()).isEqualTo("Failed to save recipe. Please try again.")
    }

    @Test
    fun `clearUserMessage should reset userMessage`() = runTest {
        viewModel.clearUserMessage()
        assertNull(viewModel.userMessage.value)
    }

}