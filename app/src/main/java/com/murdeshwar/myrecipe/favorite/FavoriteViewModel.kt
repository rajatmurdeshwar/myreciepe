package com.murdeshwar.myrecipe.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.murdeshwar.myrecipe.data.Repository
import com.murdeshwar.myrecipe.home.RecipeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
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
        viewModelScope.launch(Dispatchers.IO) {
            repository.getLocalRecipes().let { list ->
                if (list.isNullOrEmpty()){
                    _recipeUiState.update {
                        it.copy(
                            emptyItems = true
                        )
                    }
                } else {
                    Timber.d("HomeViewModel","getLocalRecipes "+list.size)
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

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}