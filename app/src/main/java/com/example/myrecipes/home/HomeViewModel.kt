package com.example.myrecipes.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myrecipes.data.Repository
import com.example.myrecipes.data.source.Recipe
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RecipeUiState(
    val items: List<Recipe?> = emptyList(),
    val isLoading: Boolean = false,
    val userMessage: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: Repository,
) : ViewModel() {

    private val _recipeUiState = MutableStateFlow(RecipeUiState())
    val recipeUiState: StateFlow<RecipeUiState> = _recipeUiState

    init {
        getLocalRecipes()
    }


    private fun getLocalRecipes() {
        _recipeUiState.update {
            it.copy(isLoading = true)
        }
        viewModelScope.launch(IO) {
            repository.getLocalRecipes().let { list ->
            if (list.isNullOrEmpty()){
                getOnlineRecipes()
            } else {
                //Log.d("HomeViewModel","getLocalRecipes "+list.size)
                _recipeUiState.update {
                    it.copy(
                        items = list,
                        isLoading = false
                    )
                }

            }

            }
        }
    }

    private fun getOnlineRecipes() {
        _recipeUiState.update {
            it.copy(isLoading = true)
        }
        viewModelScope.launch(IO) {
            try {
                val onlineRecipes = repository.getOnlineRecipes()
                onlineRecipes?.let {list ->
                    _recipeUiState.update {
                        it.copy(
                            items = list,
                            isLoading = false
                        )
                    }
                    updateRecipes(list)
                }
            } catch (e: Exception) {
                _recipeUiState.value = _recipeUiState.value.copy(
                    userMessage = "Failed to fetch recipes: ${e.message}",
                    isLoading = false
                )
            }

        }
    }

    fun getOnlineRecipesWithTags(tags: String) {
        _recipeUiState.update {
            it.copy(isLoading = true)
        }
        viewModelScope.launch(IO) {
            try {
                val onlineRecipes = repository.getOnlineRecipesWithTags(tags)
                Log.d("HomeViewModel", "online $tags")
                onlineRecipes?.let {list ->
                    _recipeUiState.update {
                        it.copy(
                            items = list,
                            isLoading = false
                        )
                    }
                    //updateRecipes(list.recipes.toLocal())
                }
            } catch (e: Exception) {
                _recipeUiState.value = _recipeUiState.value.copy(
                    userMessage = "Failed to fetch recipes: ${e.message}",
                    isLoading = false
                )
            }

        }
    }

    private fun updateRecipes(recipeList: List<Recipe?>) {
        viewModelScope.launch(IO) {
            val nonNullList = recipeList.filterNotNull()
            repository.insertAllRecipes(nonNullList)
        }
    }

    fun refreshList() {
        getLocalRecipes()
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }


}