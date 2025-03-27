package com.murdeshwar.myrecipe.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.murdeshwar.myrecipe.data.Repository
import com.murdeshwar.myrecipe.data.source.RecipeSearchData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class RecipeSearchViewModel@Inject constructor(
    private val repository: Repository,

) : ViewModel() {

    //first state whether the search is happening or not
    private val _isSearching = MutableStateFlow(true)
    val isSearching = _isSearching.asStateFlow()

    //second state the text typed by the user
    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    //third state the list to be filtered
    private val _searchList = MutableStateFlow(emptyList<RecipeSearchData>())
    val searchList = _searchList.asStateFlow()

    fun onSearchTextChange(text: String) {
        _searchText.value = text
        if (text.isNotEmpty()) {
            getSearchQuery(text)
        } else {
            _searchList.value = emptyList()
        }
    }

    fun onToggleSearch() {
        _isSearching.value = !_isSearching.value
        if (!_isSearching.value) {
            onSearchTextChange("")
        }
    }

    private fun getSearchQuery(searchQuery: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val list = repository.searchRecipe(searchQuery)
                _searchList.emit(list.filterNotNull()) // Emit only non-null results
            } catch (e: Exception) {
                Timber.e(e, "Search query failed: $searchQuery")
                _searchList.emit(emptyList()) // Clear list on error
            }
        }
    }

}