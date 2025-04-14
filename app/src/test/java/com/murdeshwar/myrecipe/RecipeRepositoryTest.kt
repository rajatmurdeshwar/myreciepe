package com.murdeshwar.myrecipe

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.murdeshwar.myrecipe.data.source.Ingredient
import com.murdeshwar.myrecipe.data.source.Recipe
import com.murdeshwar.myrecipe.data.source.RecipeWithDetails
import com.murdeshwar.myrecipe.data.source.RepositoryImpl
import com.murdeshwar.myrecipe.data.source.Step
import com.murdeshwar.myrecipe.data.source.local.AppDatabase
import com.murdeshwar.myrecipe.data.source.local.RecipeDao
import com.murdeshwar.myrecipe.data.source.network.NetworkDataSource
import com.murdeshwar.myrecipe.data.source.network.NewRecipeApiService
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.mock
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class RecipeRepositoryTest {

    private lateinit var recipeDao: RecipeDao
    private lateinit var database: AppDatabase
    private lateinit var repository: RepositoryImpl

    @Before
    fun setup() {
        // Create an in-memory database for testing
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()

        recipeDao = database.getDao()

        val mockApi =mock<NetworkDataSource>()  // Mocked network API
        val mockLocalApi = mock<NewRecipeApiService>() // Mocked API
        val ioDispatcher = Dispatchers.IO

        repository = RepositoryImpl(recipeDao, mockApi, mockLocalApi, ioDispatcher)
    }

    @Test
    fun testInsertAndGetRecipe() = runBlocking {
        val recipe = RecipeWithDetails(
            recipe = Recipe(1, "Test Recipe", "description", "category", "instructions", "image", 10, 30, 4,false,false,false),
            ingredients = listOf(Ingredient(1, 1, "Tomato", 3.0,"scoop")),
            steps = listOf(Step(1, "Chop the tomato"))
        )

        repository.insertRecipe(recipe)

        val retrievedRecipe = repository.getRecipeById(1).first()
        assertNotNull(retrievedRecipe)
        assertEquals("Test Recipe", retrievedRecipe?.recipe?.title)
    }

    @After
    fun tearDown() {
        database.close()
    }
}
