package com.example.myrecipes.util

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myrecipes.R
import com.example.myrecipes.data.source.network.RecipeSearch
import com.example.myrecipes.ui.theme.MyRecipesTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailTopAppBar(titleName:String,onBack: () -> Unit) {
    TopAppBar(
        title = {
            Text(text = titleName,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onPrimary)
        },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = stringResource(id = R.string.menu_back),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
    ),windowInsets = WindowInsets(0.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeHomeTopAppBar(onSavedRecipes: () -> Unit, onRefreshClick: () -> Unit, onSearchButtonClick: () -> Unit) {
    TopAppBar(
        title = {
            Text(text = "RecipeMaker",
                color = MaterialTheme.colorScheme.primary,

            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        actions = {
            IconButton(onClick = onSavedRecipes) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = stringResource(id = R.string.menu_saved),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer)

            }
            IconButton(onClick =  onRefreshClick ) {
                Icon(
                    imageVector = Icons.Filled.Refresh,
                    contentDescription = stringResource(id = R.string.menu_refresh),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )

            }
            IconButton(onClick =
                onSearchButtonClick
            ) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = stringResource(id = R.string.menu_search),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )

            }
        },windowInsets = WindowInsets(0.dp)
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeSearchTopAppBar(
    searchText: String,
    isSearching: Boolean,
    onToogleSearch: Boolean,
    onSearchTextChange: (String) -> Unit,
    recipeList: List<RecipeSearch>,
    onRecipeClick: (Int) -> Unit,
    onBack: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        SearchBar(
            query = searchText,
            onQueryChange = onSearchTextChange,
            onSearch = onSearchTextChange,
            active = isSearching,
            onActiveChange = { onToogleSearch },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
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
}


@Preview
@Composable
private fun RecipeDetailTopAppBarPreview() {
    MyRecipesTheme {
        Surface {
            RecipeDetailTopAppBar("Home",{ })
        }
    }
}