package com.murdeshwar.myrecipe.data

import com.murdeshwar.myrecipe.data.source.RepositoryImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Before

@ExperimentalCoroutinesApi
class RecipeRepositoryTest {

    private var testDispatcher = UnconfinedTestDispatcher()
    private var testScope = TestScope(testDispatcher)

    // Test dependencies
    private lateinit var networkDataSource: FakeNetworkDataSource
    private lateinit var localDataSource: FakeLocalDataSource
    private lateinit var localDao: FakeRecipeDao

    private lateinit var recipeRepository : RepositoryImpl

    @ExperimentalCoroutinesApi
    @Before
    fun createRepository() {
        recipeRepository = RepositoryImpl(
            dao = localDao,
            foodApi = networkDataSource,
            localApi = localDataSource,
            ioDispatcher = testDispatcher
        )
    }
}