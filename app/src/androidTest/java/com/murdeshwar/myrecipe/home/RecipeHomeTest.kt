package com.murdeshwar.myrecipe.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.murdeshwar.myrecipe.HiltTestActivity
import com.murdeshwar.myrecipe.R
import com.murdeshwar.myrecipe.data.Repository
import com.murdeshwar.myrecipe.ui.theme.MyRecipesTheme
import dagger.hilt.android.testing.HiltAndroidRule
import org.junit.runner.RunWith
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
@LargeTest
@HiltAndroidTest
class RecipeHomeTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule(order = 2)
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()

    private val activity get() = composeTestRule.activity

    @Inject
    lateinit var repository: Repository

    @Before
    fun init() {
        hiltRule.inject()
    }

    fun getRecipes() {
        setContent()
        composeTestRule.onNodeWithContentDescription(activity.getString(R.string.menu_search))
            .performClick()
        findTextField("SEARCH")
    }


    private fun setContent() {
        composeTestRule.setContent {
            MyRecipesTheme {
                //BottomNavigation()
            }
        }
    }

    private fun findTextField(textId: Int): SemanticsNodeInteraction {
        return composeTestRule.onNode(
            hasSetTextAction() and hasText(activity.getString(textId))
        )
    }

    private fun findTextField(text: String): SemanticsNodeInteraction {
        return composeTestRule.onNode(
            hasSetTextAction() and hasText(text)
        )
    }



}