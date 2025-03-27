package com.murdeshwar.myrecipe.search

import com.google.common.truth.Truth.assertThat
import com.murdeshwar.myrecipe.FakeRecipeRepository
import com.murdeshwar.myrecipe.MainCoroutineRule
import com.murdeshwar.myrecipe.data.source.Recipe
import com.murdeshwar.myrecipe.data.source.RecipeSearchData
import com.murdeshwar.myrecipe.data.source.RecipeWithDetails
import com.murdeshwar.myrecipe.ui.search.RecipeSearchViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class SearchViewModelTest {

    // Subject under test
    private lateinit var viewModel: RecipeSearchViewModel

    // Fake repository
    private lateinit var fakeRepository: FakeRecipeRepository

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setup() {
        fakeRepository = FakeRecipeRepository()
        viewModel = RecipeSearchViewModel(fakeRepository)
    }

    @Test
    fun `onSearchTextChange should update searchText state and fetch results`() = runTest {
        // Given: A recipe is added to the fake repository
        val recipe = RecipeSearchData(1, "Pasta", "", "")
        fakeRepository.insertAllRecipes(
            listOf(
                RecipeWithDetails(
                    recipe = Recipe(1, "Pasta", "", "Lunch", "", "", 0, 0, 0, false, false, false),
                    ingredients = emptyList(),
                    steps = emptyList()
                )
            )
        )

        // When: User searches for "Pasta"
        viewModel.onSearchTextChange("Pasta")

        // Then: Verify the searchText is updated
        assertThat(viewModel.searchText.first()).isEqualTo("Pasta")

        // And: The searchList should contain the recipe
        val searchResults = viewModel.searchList.first()
        assertThat(searchResults).isNotEmpty()
        assertThat(searchResults[0].title).isEqualTo("Pasta")
    }

    @Test
    fun `onSearchTextChange should clear search results when text is empty`() = runTest {
        // Given: A previous search exists
        viewModel.onSearchTextChange("Pasta")

        // When: User clears the search text
        viewModel.onSearchTextChange("")

        // Then: The searchText state should be empty
        assertThat(viewModel.searchText.first()).isEqualTo("")

        // And: The searchList should be empty
        assertThat(viewModel.searchList.first()).isEmpty()
    }

    @Test
    fun `getSearchQuery should handle errors gracefully`() = runTest {
        // Given: The repository is set to throw an error
        fakeRepository.setShouldThrowError(true)

        // When: A search is performed
        viewModel.onSearchTextChange("Error")

        // Then: The searchList should be empty
        val searchResults = viewModel.searchList.first()
        assertThat(searchResults).isEmpty()
    }


    @Test
    fun `onToggleSearch should clear search results when toggled off`() = runTest {
        // Given: A search is performed
        viewModel.onSearchTextChange("Pasta")

        // When: Toggle search off
        viewModel.onToggleSearch()

        // Then: Search text should be empty
        assertThat(viewModel.searchText.first()).isEqualTo("")

        // And: Search list should be empty
        assertThat(viewModel.searchList.first()).isEmpty()
    }
}
