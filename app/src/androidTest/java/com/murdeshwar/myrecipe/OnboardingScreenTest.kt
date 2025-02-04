package com.murdeshwar.myrecipe

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.test.espresso.action.ViewActions.swipeLeft
import androidx.test.espresso.action.ViewActions.swipeRight
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.murdeshwar.myrecipe.ui.onboarding.OnBoardingScreen
import com.murdeshwar.myrecipe.ui.theme.MyRecipesTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
@HiltAndroidTest
class OnboardingScreenTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule(order = 2)
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()

    private val pages = listOf("Welcome", "Search & Filter Recipes", "Save & Customize") // Modify as per your onboarding pages


    private fun setOnBoardingScreen() {
        composeTestRule.setContent {
            MyRecipesTheme {
                OnBoardingScreen(onNavigateToHome = {})
            }
        }
    }

    @Test
    fun testOnboardingNextButtonCLickToGetStarted() {
        setOnBoardingScreen()

        // Verify first page content is displayed
        composeTestRule.onNodeWithText(pages[0]).assertIsDisplayed()

        // Verify "Back" button is NOT visible
        composeTestRule.onNodeWithText("Back").assertDoesNotExist()

        // Verify "Next" button is visible
        composeTestRule.onNodeWithText("Next").assertIsDisplayed()

    }

    @Test
    fun onboardingScreen_navigatesForwardWithNextButton() {
        setOnBoardingScreen()

        // Click "Next" to go to Page 2
        composeTestRule.onNodeWithText("Next").performClick()
        composeTestRule.onNodeWithText(pages[1]).assertIsDisplayed()

        // Click "Next" again to go to Page 3
        composeTestRule.onNodeWithText("Next").performClick()
        composeTestRule.onNodeWithText(pages[2]).assertIsDisplayed()
    }

    @Test
    fun onboardingScreen_navigatesBackwardWithBackButton() {
        setOnBoardingScreen()

        // Navigate to Page 2
        composeTestRule.onNodeWithText("Next").performClick()
        composeTestRule.onNodeWithText(pages[1]).assertIsDisplayed()

        // Now "Back" should be visible
        composeTestRule.onNodeWithText("Back").assertIsDisplayed()

        // Click "Back" to return to Page 1
        composeTestRule.onNodeWithText("Back").performClick()
        composeTestRule.onNodeWithText(pages[0]).assertIsDisplayed()
    }


    @Test
    fun onboardingScreen_swipeNavigatesPages() {
        composeTestRule.setContent {
            OnBoardingScreen(onNavigateToHome = {})
        }

        // Wait for the UI to stabilize
        composeTestRule.waitForIdle()

        // Assert that the pager is visible
        composeTestRule.onNodeWithTag("OnboardingPager").assertExists().assertIsDisplayed()

        // Perform swipe gesture to navigate pages
        composeTestRule.onNodeWithTag("OnboardingPager").performTouchInput {
            swipeLeft()
        }

        // Verify navigation (adjust expected conditions as needed)
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Back").assertIsDisplayed()
    }





}