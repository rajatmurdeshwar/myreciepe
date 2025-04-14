package com.murdeshwar.myrecipe.ui.favorite

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.murdeshwar.myrecipe.R
import com.murdeshwar.myrecipe.data.source.Recipe
import com.murdeshwar.myrecipe.ui.common.EmptyScreen
import com.murdeshwar.myrecipe.ui.home.RecipeUiState
import com.murdeshwar.myrecipe.util.RecipeThumbnailCard

@Composable
fun RecipeFavoriteScreen(
    viewModel: FavoriteViewModel = hiltViewModel(),
    onRecipeClick: (Recipe) -> Unit,
) {
    val recipeUiState by viewModel.recipeUiState.collectAsStateWithLifecycle()

    RecipeListComposable(
        modifier = Modifier,
        recipeUiState = recipeUiState,
        onRecipeClick = onRecipeClick,
        textTitle = "Favorites",
        enablePullToRefresh = true
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeListComposable(
    modifier: Modifier,
    recipeUiState: RecipeUiState,
    onRecipeClick: (Recipe) -> Unit,
    textTitle: String = "",
    enablePullToRefresh: Boolean = true
) {
    // Pull to Refresh state
    val pullToRefreshState = rememberPullToRefreshState()
    if (enablePullToRefresh && pullToRefreshState.isRefreshing) {
        //viewModel.getLocalRecipes()  // Fetch new data
        pullToRefreshState.endRefresh() // Stop refresh animation
    }
    Box(
        modifier = modifier
            .fillMaxSize()
            .then(
                if (enablePullToRefresh) modifier.nestedScroll(pullToRefreshState.nestedScrollConnection)
                else Modifier
            )

    ) {
        Column {
            Text(
                text = "$textTitle Recipes",
                modifier = modifier.padding(
                    horizontal = dimensionResource(id = R.dimen.list_item_padding),
                    vertical = dimensionResource(id = R.dimen.vertical_margin)
                ),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            if (recipeUiState.items.isEmpty()) {
                EmptyScreen()
            }

            if (recipeUiState.isLoading) {
                CircularProgressIndicator(modifier = modifier.align(Alignment.CenterHorizontally))
            } else {
                LazyVerticalGrid(GridCells.Fixed(2)) {
                    items(recipeUiState.items.size) { index ->
                        recipeUiState.items.getOrNull(index)?.let { recipe ->
                            MyRecipeListItem(
                                modifier = modifier,
                                itemOnline = recipe,
                                onRecipeClick = onRecipeClick
                            )
                        }
                    }
                }
            }
        }
        if (enablePullToRefresh) {
            PullToRefreshContainer(
                state = pullToRefreshState,
                modifier = modifier.align(Alignment.TopCenter)
            )
        }
    }
}

@Composable
fun MyRecipeListItem(
    modifier: Modifier,
    itemOnline: Recipe,
    onRecipeClick: (Recipe) -> Unit,
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = dimensionResource(id = R.dimen.horizontal_margin),
                vertical = dimensionResource(id = R.dimen.list_item_padding),
            )
            .clickable { onRecipeClick(itemOnline) }
            .fillMaxSize()
    ) {
        RecipeThumbnailCard(modifier = modifier, itemOnline,onRecipeClick)

    }

}