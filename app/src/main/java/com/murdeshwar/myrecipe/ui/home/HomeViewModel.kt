package com.murdeshwar.myrecipe.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.murdeshwar.myrecipe.data.Repository
import com.murdeshwar.myrecipe.data.source.Recipe
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

data class RecipeUiState(
    val items: List<Recipe?> = emptyList(),
    val seasonalRecipes: List<Recipe?> = emptyList(),
    val isLoading: Boolean = false,
    val userMessage: String? = null,
    val emptyItems: Boolean = false
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: Repository,
) : ViewModel() {

    private val _recipeUiState = MutableStateFlow(RecipeUiState())
    val recipeUiState: StateFlow<RecipeUiState> = _recipeUiState

    init {
        getOnlineRecipes()
        fetchSeasonalRecipe()
    }

    private fun fetchOnlineRecipes(tags: String? = null) {
        _recipeUiState.update { it.copy(isLoading = true) }
        viewModelScope.launch(IO) {
            try {
                val recipes = tags?.let { repository.getOnlineRecipesWithTags(it) }
                    ?: repository.getOnlineRecipes()
                recipes.let { list ->
                    _recipeUiState.update {
                        it.copy(
                            items = list,
                            isLoading = false,
                            emptyItems = list.isEmpty()
                        )

                    }
                }
            } catch (e: Exception) {
                Timber.e(e, "Error loading recipes")
                _recipeUiState.update {
                    it.copy(
                        userMessage = e.localizedMessage ?: "Error loading recipes",
                        isLoading = false
                    )
                }
            }
        }
    }

    private fun fetchSeasonalRecipe() {
        _recipeUiState.update { it.copy(isLoading = true) }
        viewModelScope.launch(IO) {
            try {
                val seasonalTag = SeasonalTags.getCurrentSeasonalTag()
                val seasonalRecipe = repository.getOnlineRecipesWithTags(seasonalTag)
                _recipeUiState.update {
                    it.copy(seasonalRecipes = seasonalRecipe, isLoading = false)
                }
            } catch (e: Exception) {
                Timber.e(e, "Error fetching seasonal recipe")
                _recipeUiState.update {
                    it.copy(
                        userMessage = e.localizedMessage ?: "Error fetching seasonal recipe",
                        isLoading = false
                    )
                }
            }
        }
    }

    fun getOnlineRecipes() = fetchOnlineRecipes()

    fun getOnlineRecipesWithTags(tags: String) = fetchOnlineRecipes(tags)

    fun refreshList() {
        // Decide whether to refresh both lists or just one
        getOnlineRecipes()
        fetchSeasonalRecipe()
    }

}