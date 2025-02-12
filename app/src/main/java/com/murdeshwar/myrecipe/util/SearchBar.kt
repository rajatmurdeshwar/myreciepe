package com.murdeshwar.myrecipe.util


import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.ClickableText

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager

import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.AnnotatedString

import androidx.compose.ui.unit.dp
import com.murdeshwar.myrecipe.data.source.RecipeSearchData


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmbeddedSearchBar(
    searchText: String,
    onQueryChange: (String) -> Unit,
    isSearchActive: Boolean = true, // Start active
    onActiveChanged: (Boolean) -> Unit,
    onSearch: ((String) -> Unit)? = null,
    recipeList: List<RecipeSearchData>,
    onRecipeClick: (Int) -> Unit
) {
    var isActive by rememberSaveable { mutableStateOf(isSearchActive) } // Internal active state
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    // Ensure focus is requested only when active is true
    LaunchedEffect(isActive) {
        if (isActive) {
            focusRequester.requestFocus()
        }
    }

    SearchBar(
        query = searchText,
        onQueryChange = onQueryChange,
        onSearch = {
            onSearch?.invoke(searchText)
            focusManager.clearFocus() // Dismiss keyboard after search
        },
        active = isActive,
        onActiveChange = { newActiveState ->
            isActive = newActiveState
            onActiveChanged(newActiveState)
        },
        placeholder = { Text("Search recipes...") },
        modifier = Modifier.focusRequester(focusRequester),
        leadingIcon = {
            if (isActive) {
                IconButton(onClick = {
                    isActive = false
                    onActiveChanged(false)
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = "Go back",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            } else {
                Icon(
                    imageVector = Icons.Rounded.Search,
                    contentDescription = "Search icon",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        trailingIcon = if (isActive && searchText.isNotEmpty()) {
            {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = "Clear search text",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        } else {
            null
        },
        colors = SearchBarDefaults.colors(
            containerColor = if (isActive) {
                MaterialTheme.colorScheme.background
            } else {
                MaterialTheme.colorScheme.surfaceContainerLow
            }
        ),
        tonalElevation = 0.dp,
        windowInsets = WindowInsets(0.dp)
    ) {
        LazyColumn {
            items(recipeList, key = { it.id }) { recipe ->
                ClickableText(
                    text = AnnotatedString(recipe.title),
                    onClick = { onRecipeClick(recipe.id) },
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .semantics {
                            contentDescription = "Recipe: ${recipe.title}"
                        },
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
            }
        }
    }
}



