package com.murdeshwar.myrecipe.details

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.murdeshwar.myrecipe.FakeRepository
import com.murdeshwar.myrecipe.HiltTestActivity
import com.murdeshwar.myrecipe.data.Repository
import com.murdeshwar.myrecipe.ui.details.RecipeDetailScreen
import com.murdeshwar.myrecipe.ui.details.RecipeDetailsViewModel
import com.murdeshwar.myrecipe.ui.theme.MyRecipesTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
@MediumTest
@HiltAndroidTest
class RecipeDetailsScreenTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()

    private lateinit var viewModel: RecipeDetailsViewModel

    @Inject
    lateinit var repository: Repository

    @Before
    fun init() {
        hiltRule.inject()
        repository = FakeRepository()
        viewModel = RecipeDetailsViewModel(repository)
    }

    private fun setRecipeDetailsScreen() {
        composeTestRule.setContent {
            MyRecipesTheme {
                RecipeDetailScreen(
                    recipeId = 1,
                    onBack = {},
                    viewModel = viewModel
                )
            }
        }
    }

    @Test
    fun recipeDetailScreen_displaysRecipeDetails() {
        // Set the screen content
        setRecipeDetailsScreen()

        // Assert loading state is displayed initially
        //composeTestRule.onNodeWithTag("loading_indicator").assertExists()

        // Assert Recipe title is displayed
        //composeTestRule.onNodeWithText("Recipe Title").assertExists()

        // Assert Summary section
        composeTestRule.onNodeWithText("Summary").assertExists()
        composeTestRule.onNodeWithText("Description").assertExists()

        // Assert Ingredients section
        //composeTestRule.onNodeWithText("Tomato: 2.0 cups").assertExists()
        //composeTestRule.onNodeWithText("Onion: 1.0 piece").assertExists()

        // Assert Instructions section
        composeTestRule.onNodeWithText("Instructions").assertExists()
        composeTestRule.onNodeWithText("1. Chop the vegetables.").assertExists()
        composeTestRule.onNodeWithText("2. Cook the vegetables.").assertExists()

        // Click Floating Action Button
        composeTestRule.onNodeWithTag("fab_save_recipe").performClick()

        // Ensure Snackbar is shown
        composeTestRule.onNodeWithText("Recipe successfully saved! 🎉")
            .assertExists()
    }


}