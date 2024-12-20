package com.murdeshwar.myrecipe.search

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SearchBarScreen() {
    var searchText by remember { mutableStateOf("") }
    var isSearchActive by remember { mutableStateOf(false) }
    val allRecipes = listOf(
        "Spaghetti Bolognese",
        "Chicken Alfredo",
        "Vegan Buddha Bowl",
        "Beef Stroganoff",
        "Caesar Salad",
        "Margherita Pizza",
        "Tacos",
        "Ramen Noodles",
        "Pancakes",
        "Grilled Cheese Sandwich"
    )

    val filteredRecipes = allRecipes.filter { it.contains(searchText, ignoreCase = true) }

    Scaffold(
        topBar = {
            SimpleSearchBar(
                searchText = searchText,
                onQueryChange = { searchText = it },
                isSearchActive = isSearchActive,
                onActiveChanged = { isSearchActive = it }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            items(filteredRecipes) { recipe ->
                Text(
                    text = recipe,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun SimpleSearchBar(
    searchText: String,
    onQueryChange: (String) -> Unit,
    isSearchActive: Boolean,
    onActiveChanged: (Boolean) -> Unit
) {
    SearchBar(
        query = searchText,
        onQueryChange = onQueryChange,
        onSearch = { onActiveChanged(false) }, // Close the search on submit
        active = isSearchActive,
        onActiveChange = onActiveChanged,
        placeholder = { Text("Search recipes...") },
        leadingIcon = {
            if (isSearchActive) {
                IconButton(onClick = { onActiveChanged(false) }) {
                    Icon(
                        imageVector = Icons.Rounded.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            } else {
                Icon(
                    imageVector = Icons.Rounded.Search,
                    contentDescription = null
                )
            }
        },
        trailingIcon = if (isSearchActive && searchText.isNotEmpty()) {
            {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = "Clear search"
                    )
                }
            }
        } else {
            null
        },
        colors = SearchBarDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        tonalElevation = 0.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .animateContentSize()
    ) {
        // You can add extra content inside the dropdown of the search bar if needed
    }
}




