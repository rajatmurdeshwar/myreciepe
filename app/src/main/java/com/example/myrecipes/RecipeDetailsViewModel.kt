package com.example.myrecipes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myrecipes.data.Repository
import com.example.myrecipes.data.source.local.LocalRecipe
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

    private val _recipe : MutableStateFlow<LocalRecipe?> = MutableStateFlow(null)
    val recipe = _recipe.asStateFlow()

    fun getRecipeByID(recipeId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getRecipeById(recipeId).collectLatest {
                _recipe.value = it
            }
        }
    }
}