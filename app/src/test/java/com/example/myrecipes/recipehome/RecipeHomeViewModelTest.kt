package com.example.myrecipes.recipehome

import com.example.myrecipes.MainCoroutineRule
import com.example.myrecipes.FakeRecipeRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule

@ExperimentalCoroutinesApi
class RecipeHomeViewModelTest {

    // Subject under test
    private lateinit var recipeViewModel: RecipeHomeViewModelTest

    // Use a fake repository to be injected into the viewmodel
    private lateinit var recipeRepository: FakeRecipeRepository

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setupViewModel() {
        recipeRepository = FakeRecipeRepository().apply {

        }
    }
}