package com.example.myreciepes.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myreciepes.Recipe
import com.example.myreciepes.Repository
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

    private val _reciepeList = MutableStateFlow(emptyList<Recipe>())
    val recipeList = _reciepeList.asStateFlow()

    fun getReciepeDetails() {
        viewModelScope.launch(IO) {
            repository.getAllRecipes().collectLatest {
                _reciepeList.tryEmit(it)
            }
        }
    }
}