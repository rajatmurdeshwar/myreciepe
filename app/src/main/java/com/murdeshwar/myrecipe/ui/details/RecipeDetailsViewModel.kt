package com.murdeshwar.myrecipe.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.murdeshwar.myrecipe.data.Repository
import com.murdeshwar.myrecipe.data.source.RecipeWithDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeDetailsViewModel@Inject constructor(
    private val repository: Repository,
) : ViewModel() {

    private val _recipe = MutableStateFlow(RecipeDetailsUiState(isLoading = true))
    val recipe : StateFlow<RecipeDetailsUiState> = _recipe.asStateFlow()

    private val _userMessage = MutableStateFlow<String?>(null)
    val userMessage = _userMessage.asStateFlow()

    fun getRecipeByID(recipeId: Int) {
        viewModelScope.launch {
            repository.getRecipeById(recipeId).collectLatest {recipe ->
                if (recipe != null) {
                    _recipe.value = RecipeDetailsUiState(recipeWithDetails = recipe)
                } else {
                    getRecipeDetailsByID(recipeId)
                }
            }
        }
    }

    fun saveRecipeToRemoteAndLocal(recipe: RecipeWithDetails) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Step 1: Add to remote database
                repository.addRecipesToDb(recipe.recipe)
                // Step 2: Add to local database
                repository.insertRecipe(recipe)

                _userMessage.value = "Recipe successfully saved! 🎉"

            } catch (e: Exception) {
                _userMessage.value = "Failed to save recipe. Please try again."
            }
        }
    }

    private fun getRecipeDetailsByID(recipeId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val recipeDetail = repository.getRecipeDetailsById(recipeId)
            _recipe.update {
                RecipeDetailsUiState(recipeWithDetails = recipeDetail, isLoading = false)
            }
        }
    }

    fun clearUserMessage() {
        _userMessage.value = null
    }


}
data class RecipeDetailsUiState(
    val recipeWithDetails: RecipeWithDetails? = null,
    val isLoading: Boolean = false,
    val userMessage: String? = null,
    val emptyItems: Boolean = false
)
