package com.murdeshwar.myrecipe.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.murdeshwar.myrecipe.data.Repository
import com.murdeshwar.myrecipe.data.source.RecipeWithDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeDetailsViewModel@Inject constructor(
    private val repository: Repository,
) : ViewModel() {

    private val _recipe : MutableStateFlow<RecipeWithDetails?> = MutableStateFlow(null)
    val recipe = _recipe.asStateFlow()

    fun getRecipeByID(recipeId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getRecipeById(recipeId).collectLatest {
                if (it != null) {
                    _recipe.value = it
                } else {
                    getRecipeDetailsByID(recipeId)
                }
            }
        }
    }

    private fun getRecipeDetailsByID(recipeId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val recipeDetail = repository.getRecipeDetailsById(recipeId)
            recipeDetail.let {
                _recipe.emit(it)
                if (it != null) {
                    repository.addRecipesToDb(it.recipe)
                    repository.insertRecipe(it)
                }
            }
        }
    }

}