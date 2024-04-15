package com.example.myrecipes.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myrecipes.data.source.local.LocalRecipe
import com.example.myrecipes.data.Repository
import com.example.myrecipes.data.source.Recipe
import com.example.myrecipes.data.source.network.NetworkRecipe
import com.example.myrecipes.data.toLocal
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RecipeUiState(
    val items: List<Recipe> = emptyList(),
    val isLoading: Boolean = false,
    val userMessage: Int? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: Repository,
) : ViewModel() {

    private val _recipeUiState = MutableStateFlow(RecipeUiState())
    val recipeUiState: StateFlow<RecipeUiState> = _recipeUiState

    init {
        getLocalRecipes()
        viewModelScope.launch(IO) {
            recipeList?.collect { recipes ->
                if (recipes.isEmpty()) {
                    getOnlineRecipes()
                }
            }
        }
    }


    private val _recipeList = MutableStateFlow(emptyList<Recipe>())
    val recipeList = _recipeList.asStateFlow()

    private val _recipeOnlineList = MutableStateFlow(NetworkRecipe())
    val recipeOnlineList = _recipeOnlineList


    private fun getLocalRecipes() {
        _recipeUiState.update {
            it.copy(isLoading = true)
        }
        viewModelScope.launch(IO) {
            repository.getLocalRecipes().let { list ->
            if (list !=null){
                _recipeUiState.update {
                    it.copy(
                        items = list,
                        isLoading = false
                    )
                }
            } else{
                _recipeUiState.update {
                    it.copy(isLoading = false)
                }
            }

            }
        }
    }

    private fun getOnlineRecipes() {
        viewModelScope.launch(IO) {
            val onlineRecipes = repository.getOnlineRecipes()
            onlineRecipes?.let {
                _recipeOnlineList?.emit(it)
                updateRecipes(it.recipes.toLocal())
            }

        }
    }

    private fun updateRecipes(recipeList: List<LocalRecipe>) {
        viewModelScope.launch(IO) {
            repository.insertAllRecipes(
                recipeList
            )
        }
    }


}