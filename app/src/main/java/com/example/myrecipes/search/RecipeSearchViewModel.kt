package com.example.myrecipes.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myrecipes.data.Repository
import com.example.myrecipes.data.source.network.RecipeSearch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeSearchViewModel@Inject constructor(
    private val repository: Repository,
) : ViewModel() {

    //first state whether the search is happening or not
    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    //second state the text typed by the user
    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    //third state the list to be filtered
    private val _searchList = MutableStateFlow(emptyList<RecipeSearch>())
    val searchList = _searchList.asStateFlow()

    fun onSearchTextChange(text: String) {
        _searchText.value = text
        getSearchQuery(text)
    }

    fun onToogleSearch() {
        _isSearching.value = !_isSearching.value
        if (!_isSearching.value) {
            onSearchTextChange("")
        }
    }

    private fun getSearchQuery(searchQuery: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val list = repository.searchRecipe(searchQuery)
            list?.let { _searchList.emit(it.results) }
        }
    }

}