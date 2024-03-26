package com.example.myrecipes.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myrecipes.Recipe
import com.example.myrecipes.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: Repository,
) : ViewModel() {

    private val _recipeList = MutableStateFlow(emptyList<Recipe>())
    val recipeList = _recipeList.asStateFlow()

    fun getRecipeDetails() {
        viewModelScope.launch(IO) {
            repository.getAllRecipes().collectLatest {
                _recipeList.tryEmit(it)
            }
        }
    }
}