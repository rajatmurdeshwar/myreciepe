package com.example.myrecipes.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myrecipes.data.Repository
import com.example.myrecipes.data.source.Recipe
import com.example.myrecipes.data.source.local.LocalRecipe
import com.example.myrecipes.data.toExternal
import com.example.myrecipes.data.toLocal
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

    private val _recipe : MutableStateFlow<Recipe?> = MutableStateFlow(null)
    val recipe = _recipe.asStateFlow()

    fun getRecipeByID(recipeId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getRecipeById(recipeId).collectLatest {
                if (it != null) {
                    _recipe.value = it.toExternal()
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
                _recipe.emit(it?.toExternal())
                if (it != null) {
                    repository.insertRecipe(it.toLocal())
                }
            }
        }
    }
}