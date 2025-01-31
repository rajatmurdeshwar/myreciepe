package com.murdeshwar.myrecipe.ui.search


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding

import androidx.compose.runtime.getValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState

import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.murdeshwar.myrecipe.util.EmbeddedSearchBar


@Composable
fun RecipeSearchScreen(
    viewModel: RecipeSearchViewModel = hiltViewModel(),
    onRecipeClick: (Int) -> Unit) {


        //Collecting states from ViewModel
        val searchText by viewModel.searchText.collectAsState()
        val isSearching by viewModel.isSearching.collectAsState()
        val recipeList by viewModel.searchList.collectAsState()

        Column(
            modifier = Modifier
                .statusBarsPadding()
                .fillMaxSize()
        ) {
            EmbeddedSearchBar(
                searchText = searchText,
                onQueryChange = viewModel::onSearchTextChange,
                isSearchActive = isSearching,
                onActiveChanged = { viewModel.onToggleSearch() },
                recipeList = recipeList,
                onRecipeClick = onRecipeClick
            )

        }
}




