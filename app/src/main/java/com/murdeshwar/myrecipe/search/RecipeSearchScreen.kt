package com.murdeshwar.myrecipe.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack

import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.murdeshwar.myrecipe.R
import com.murdeshwar.myrecipe.data.source.RecipeSearchData
import com.murdeshwar.myrecipe.ui.theme.MyRecipesTheme

@Composable
fun RecipeSearchScreen(viewModel: RecipeSearchViewModel = hiltViewModel(), onRecipeClick: (Int) -> Unit) {

    MyRecipesTheme {
        //Collecting states from ViewModel
        val searchText by viewModel.searchText.collectAsState()
        val isSearching by viewModel.isSearching.collectAsState()
        val recipeList by viewModel.searchList.collectAsState()

        Scaffold(
            topBar = {
                EmbeddedSearchBar(
                    searchText = searchText,
                    onQueryChange = viewModel::onSearchTextChange,
                    isSearchActive = isSearching,
                    onActiveChanged = { viewModel.onToggleSearch() },
                    recipeList = recipeList,
                    onRecipeClick = onRecipeClick
                )
            }
        ) { paddingValues ->
            if (recipeList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No recipes found.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(modifier = Modifier.padding(paddingValues)) {
                    items(recipeList.size) { recipe ->
                        Text(
                            text = recipeList[recipe].title,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .clickable { onRecipeClick(recipeList[recipe].id) },
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun EmbeddedSearchBar(
    searchText: String,
    onQueryChange: (String) -> Unit,
    isSearchActive: Boolean,
    onActiveChanged: (Boolean) -> Unit,
    onSearch: ((String) -> Unit)? = null,
    recipeList: List<RecipeSearchData>,
    onRecipeClick: (Int) -> Unit
) {
    var searchQuery by rememberSaveable { mutableStateOf("") }
    val activeChanged: (Boolean) -> Unit = { active ->
        searchQuery = ""
        onQueryChange("")
        onActiveChanged(active)
    }
    SearchBar(
        query = searchText,
        onQueryChange = { query ->
            searchQuery = query
            onQueryChange(query)
        },
        onSearch = onSearch ?: { activeChanged(false) },
        active = isSearchActive,
        onActiveChange = activeChanged,
        placeholder = { Text("Search") },
        leadingIcon = {
            if (isSearchActive) {
                IconButton(
                    onClick = { activeChanged(false) },
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = stringResource(R.string.navigation_action_back_cd),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                }
            } else {
                Icon(
                    imageVector = Icons.Rounded.Search,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        },
        trailingIcon = if (isSearchActive && searchQuery.isNotEmpty()) {
            {
                IconButton(
                    onClick = {
                        searchQuery = ""
                        onQueryChange("")
                    },
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = stringResource(R.string.search_text_field_clear),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                }
            }
        } else {
            null
        },
        colors = SearchBarDefaults.colors(
            containerColor = if (isSearchActive) {
                MaterialTheme.colorScheme.background
            } else {
                MaterialTheme.colorScheme.surfaceContainerLow
            },
        ),
        tonalElevation = 0.dp,
        windowInsets = WindowInsets(0.dp)
    ) {
        LazyColumn {
            items(recipeList.size) { recipe ->
                ClickableText(
                    text = AnnotatedString(recipeList[recipe].title),
                    onClick = {
                        onRecipeClick(recipeList[recipe].id)
                    },
                    modifier = Modifier
                        .padding(
                            start = 8.dp,
                            top = 4.dp,
                            end = 8.dp,
                            bottom = 4.dp
                        )
                        .semantics {
                            contentDescription = "Recipe: ${recipeList[recipe].title}"
                        },
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )

            }
        }
    }
}




