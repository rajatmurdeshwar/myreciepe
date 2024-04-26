package com.example.myrecipes.Home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.myrecipes.HiltTestActivity
import dagger.hilt.android.testing.HiltAndroidRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.runner.RunWith
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule

@RunWith(AndroidJUnit4::class)
@LargeTest
@HiltAndroidTest
@OptIn(ExperimentalCoroutinesApi::class)
class RecipeHomeTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule(order = 2)
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()

}