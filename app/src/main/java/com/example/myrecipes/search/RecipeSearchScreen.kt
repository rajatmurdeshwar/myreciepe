package com.example.myrecipes.search

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel


@OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    fun RecipeSearchScreen(viewModel: RecipeSearchViewModel = hiltViewModel(), onRecipeClick: (Int) -> Unit, onBack: () -> Unit) {


        //Collecting states from ViewModel
        val searchText by viewModel.searchText.collectAsState()
        val isSearching by viewModel.isSearching.collectAsState()
        val recipeList by viewModel.searchList.collectAsState()

        Scaffold(
            topBar = {
                SearchBar(
                    query = searchText,//text showed on SearchBar
                    onQueryChange = viewModel::onSearchTextChange, //update the value of searchText
                    onSearch = viewModel::onSearchTextChange, //the callback to be invoked when the input service triggers the ImeAction.Search action
                    active = isSearching, //whether the user is searching or not
                    onActiveChange = { viewModel.onToogleSearch() }, //the callback to be invoked when this search bar's active state is changed
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                ) {
                    LazyColumn {
                        items(recipeList.size) { recipe ->
                            ClickableText(
                                text = AnnotatedString(recipeList[recipe].title),
                                onClick = {
                                    onRecipeClick(recipeList[recipe].id)
                                },
                                modifier = Modifier.padding(
                                    start = 8.dp,
                                    top = 4.dp,
                                    end = 8.dp,
                                    bottom = 4.dp
                                )
                            )
                        }
                    }
                }
            }
        ) {
        }
    }
