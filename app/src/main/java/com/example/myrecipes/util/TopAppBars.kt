package com.example.myrecipes.util

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
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
            Text(text = titleName)
        },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(Icons.Filled.ArrowBack, stringResource(id = R.string.menu_back))
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeHomeTopAppBar(onRefreshClick: () -> Unit, onSearchButtonClick: () -> Unit) {
    TopAppBar(
        title = {
            Text(text = "RecipeMaker")
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        actions = {
                         IconButton(onClick =  onRefreshClick ) {
                             Icon(Icons.Filled.Refresh, stringResource(id = R.string.menu_refresh) )

                         }
            IconButton(onClick =
                onSearchButtonClick
            ) {
                Icon(Icons.Filled.Search, stringResource(id = R.string.menu_search))

            }
        },
        modifier = Modifier.fillMaxWidth()
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
            query = searchText,//text showed on SearchBar
            onQueryChange = onSearchTextChange, //update the value of searchText
            onSearch = onSearchTextChange, //the callback to be invoked when the input service triggers the ImeAction.Search action
            active = isSearching, //whether the user is searching or not
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
            RecipeDetailTopAppBar("",{ })
        }
    }
}