package com.murdeshwar.myrecipe

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.murdeshwar.myrecipe.ui.theme.MyRecipesTheme
import com.murdeshwar.myrecipe.ui.user.LoginScreen
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
@HiltAndroidTest
class LonginSignupScreenTest {


    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule(order = 2)
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()

    private fun setLoginSignupScreen() {
        composeTestRule.setContent {
            MyRecipesTheme {
                LoginScreen(onLoginSuccess = {}, onSignUpSuccess = {})
            }
        }
    }

    @Test
    fun loginScreen_displaysCorrectly() {
        setLoginSignupScreen()

        composeTestRule.onNodeWithText("Username").assertExists().assertIsDisplayed()
        composeTestRule.onNodeWithText("Password").assertExists().assertIsDisplayed()
        composeTestRule.onNodeWithText("Log In").assertExists().assertIsDisplayed()
    }

    @Test
    fun signupScreen_displaysCorrectly() {
        setLoginSignupScreen()

        composeTestRule.onNodeWithText("Don't have an account? Sign Up").performClick()

        // Verify SignUp fields
        composeTestRule.onNodeWithText("Name").assertExists().assertIsDisplayed()
        composeTestRule.onNodeWithText("Phone").assertExists().assertIsDisplayed()
        composeTestRule.onNodeWithText("City").assertExists().assertIsDisplayed()
        composeTestRule.onNodeWithText("Email").assertExists().assertIsDisplayed()
        composeTestRule.onNodeWithText("Password").assertExists().assertIsDisplayed()
        composeTestRule.onNodeWithText("Submit").assertExists().assertIsDisplayed()
    }

    @Test
    fun loginScreen_performLogin() {
        setLoginSignupScreen()

        composeTestRule.onNodeWithText("Username").performTextInput("testuser")
        composeTestRule.onNodeWithText("Password").performTextInput("password")
        composeTestRule.onNodeWithText("Log In").performClick()

        // Verify if login triggered
        composeTestRule.waitForIdle()
    }

    @Test
    fun signupScreen_performSignUp() {
        setLoginSignupScreen()

        composeTestRule.onNodeWithText("Don't have an account? Sign Up").performClick()

        composeTestRule.onNodeWithText("Name").performTextInput("John Doe")
        composeTestRule.onNodeWithText("Phone").performTextInput("1234567890")
        composeTestRule.onNodeWithText("City").performTextInput("New York")
        composeTestRule.onNodeWithText("Email").performTextInput("john.doe@example.com")
        composeTestRule.onNodeWithText("Password").performTextInput("password123")
        composeTestRule.onNodeWithText("Submit").performClick()

        composeTestRule.waitForIdle()
    }

    @Test
    fun loginScreen_switchToSignUp() {
        composeTestRule.setContent {
            LoginScreen(onLoginSuccess = {}, onSignUpSuccess = {})
        }

        // Click sign up button
        composeTestRule.onNodeWithText("Don't have an account? Sign Up").performClick()

        // Check if Sign Up screen is displayed
        composeTestRule.onNodeWithText("Submit").assertExists().assertIsDisplayed()

        // Click log in button
        composeTestRule.onNodeWithText("Already have an account? Log In").performClick()

        // Check if Login screen is displayed
        composeTestRule.onNodeWithText("Log In").assertExists().assertIsDisplayed()
    }
}