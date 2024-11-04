package com.example.myrecipes.Home

import androidx.annotation.StringRes
import androidx.compose.material3.Surface
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.myrecipes.HiltTestActivity
import com.example.myrecipes.R
import com.example.myrecipes.data.Repository
import com.example.myrecipes.data.source.Recipe
import com.example.myrecipes.data.source.RecipeWithDetails
import com.example.myrecipes.home.HomeScreen
import com.example.myrecipes.home.HomeViewModel
import com.example.myrecipes.ui.theme.MyRecipesTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.verify
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
@MediumTest
@HiltAndroidTest
@OptIn(ExperimentalCoroutinesApi::class)
class RecipeHomeScreenTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()
    private val activity get() = composeTestRule.activity

    @Inject
    lateinit var repository: Repository


    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun showVeganRecipes() = runTest {
        // Add 2 vegan recipes and 1 non-vegan recipe
        repository.apply {
            insertRecipe(
                RecipeWithDetails(
                    recipe = Recipe(1, "Vegan Recipe 1", "Description 1", "Category 1", "Instructions 1", "image_url_1", 80, 20, 4, vegan = true, glutenFree = true, dairyFree = true),
                    ingredients = emptyList(),
                    steps = emptyList()
                )
            )
            insertRecipe(
                RecipeWithDetails(
                    recipe = Recipe(2, "Vegan Recipe 2", "Description 2", "Category 2", "Instructions 2", "image_url_2", 90, 30, 2, vegan = true, glutenFree = false, dairyFree = true),
                    ingredients = emptyList(),
                    steps = emptyList()
                )
            )
            insertRecipe(
                RecipeWithDetails(
                    recipe = Recipe(3, "Non-Vegan Recipe", "Description 3", "Category 3", "Instructions 3", "image_url_3", 60, 25, 3, vegan = false, glutenFree = true, dairyFree = false),
                    ingredients = emptyList(),
                    steps = emptyList()
                )
            )
        }

        // Set content with the screen or composable to be tested
        setContent(HomeViewModel(repository))

        // Filter to show only vegan recipes
        //openFilterAndSelectOption(R.string.menu_refresh) // Assume this opens the filter for vegan recipes
        composeTestRule.onNodeWithContentDescription(activity.getString(R.string.menu_refresh))
            .performClick()
        composeTestRule.waitForIdle()
        // Verify vegan recipes are shown, non-vegan is not
        composeTestRule.onNodeWithText("Vegan Recipe 1").assertIsDisplayed()
        composeTestRule.onNodeWithText("Vegan Recipe 2").assertIsDisplayed()
        composeTestRule.onNodeWithText("Non-Vegan Recipe").assertDoesNotExist()
    }

    @Test
    fun testHomeScreenDisplaysRecipes() = runTest {
        // Set up mock data for ViewModel
        repository.apply {
            insertRecipe(
                RecipeWithDetails(
                    recipe = Recipe(
                        1,
                        "Recipe 1",
                        "Description 1",
                        "Category 1",
                        "Instructions 1",
                        "image_url_1",
                        80,
                        20,
                        4,
                        vegan = true,
                        glutenFree = true,
                        dairyFree = true
                    ),
                    ingredients = emptyList(),
                    steps = emptyList()
                )
            )
            insertRecipe(
                RecipeWithDetails(
                    recipe = Recipe(
                        2,
                        "Recipe 2",
                        "Description 2",
                        "Category 2",
                        "Instructions 2",
                        "image_url_2",
                        90,
                        30,
                        2,
                        vegan = true,
                        glutenFree = false,
                        dairyFree = true
                    ),
                    ingredients = emptyList(),
                    steps = emptyList()
                )
            )
        }

        // Launch the composable under test
        setContent(HomeViewModel(repository))

        composeTestRule.onNodeWithContentDescription(activity.getString(R.string.menu_saved))
            .performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Recipe 1").assertIsDisplayed()
        composeTestRule.onNodeWithText("Recipe 2").assertIsDisplayed()
    }


    private fun setContent(viewModel: HomeViewModel) {
        composeTestRule.setContent {
            MyRecipesTheme {
                Surface {
                    HomeScreen(
                        viewModel = viewModel,
                        onRecipeClick = {},
                        onSearchButtonClick = {}
                    )
                }
            }
        }
    }

    private fun openFilterAndSelectOption(@StringRes option: Int) {
        composeTestRule.onNodeWithContentDescription(activity.getString(R.string.menu_refresh))
            .performClick()
        composeTestRule.onNodeWithText(activity.getString(option)).assertIsDisplayed()
        composeTestRule.onNodeWithText(activity.getString(option)).performClick()
    }
}