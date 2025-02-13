package com.murdeshwar.myrecipe.ui.favorite

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.murdeshwar.myrecipe.Dimens.Elevation
import com.murdeshwar.myrecipe.R
import com.murdeshwar.myrecipe.data.source.Recipe
import com.murdeshwar.myrecipe.ui.common.EmptyScreen
import com.murdeshwar.myrecipe.ui.home.RecipeUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeFavoriteScreen(
    viewModel: FavoriteViewModel = hiltViewModel(),
    onRecipeClick: (Recipe) -> Unit,
) {
    val recipeUiState by viewModel.recipeUiState.collectAsStateWithLifecycle()

    // Pull to Refresh state
    val pullToRefreshState = rememberPullToRefreshState()
    if (pullToRefreshState.isRefreshing) {
        viewModel.getLocalRecipes()  // Fetch new data
        pullToRefreshState.endRefresh() // Stop refresh animation
    }

    RecipeListComposable(
        recipeUiState = recipeUiState,
        onRecipeClick = onRecipeClick,
        pullToRefreshState = pullToRefreshState
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeListComposable(
    recipeUiState: RecipeUiState,
    onRecipeClick: (Recipe) -> Unit,
    textTitle: String = "Saved",
    pullToRefreshState: PullToRefreshState,
) {
    Box(
        modifier = Modifier
            .nestedScroll(pullToRefreshState.nestedScrollConnection)
            .fillMaxSize()
    ) {
        Column {
            Text(
                text = "$textTitle Recipes",
                modifier = Modifier.padding(
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
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else {
                LazyVerticalGrid(GridCells.Fixed(2)) {
                    items(recipeUiState.items.size) { index ->
                        recipeUiState.items.getOrNull(index)?.let { recipe ->
                            MyRecipeListItem(
                                itemOnline = recipe,
                                onRecipeClick = onRecipeClick
                            )
                        }
                    }
                }
            }
        }
        PullToRefreshContainer(
            state = pullToRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MyRecipeListItem(
    itemOnline: Recipe,
    onRecipeClick: (Recipe) -> Unit,
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = dimensionResource(id = R.dimen.horizontal_margin),
                vertical = dimensionResource(id = R.dimen.list_item_padding),
            )
            .clickable { onRecipeClick(itemOnline) }
            .fillMaxSize()
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
            ), elevation = CardDefaults.cardElevation(defaultElevation = Elevation)

        ) {
            val sizeImage by remember { mutableStateOf(IntSize.Zero) }

            val gradient = Brush.verticalGradient(
                colors = listOf(Color.Transparent.copy(alpha = 0.1f), Color.Black.copy(alpha = 0.5f)),
                startY = sizeImage.height.toFloat()/3,
                endY = sizeImage.height.toFloat()
            )
            Box(modifier = Modifier
                .size(width = 240.dp, height = 100.dp)
            ) {
                GlideImage(
                    model = itemOnline.itemImage,
                    contentDescription = "",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    failure = placeholder(R.drawable.ic_platter)
                )
                Box(modifier = Modifier
                    .fillMaxSize()
                    .background(gradient))
                Text(
                    text = itemOnline.title,
                    color = Color.White,
                    modifier = Modifier.align(Alignment.BottomCenter),
                    maxLines = 1,
                    style = MaterialTheme.typography.bodyLarge
                )

            }

        }

    }

}