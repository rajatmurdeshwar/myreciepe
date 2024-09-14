package com.example.myrecipes.home


import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder

import com.example.myrecipes.R
import com.example.myrecipes.util.RecipeHomeTopAppBar
import com.example.myrecipes.data.source.Recipe
import com.example.myrecipes.ui.theme.MyRecipesTheme

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    onRecipeClick: (Recipe) -> Unit,
    onSearchButtonClick: () -> Unit
) {
    val itemViewStates by viewModel.recipeUiState.collectAsStateWithLifecycle()
    //Log.d("HomeScreen","ItemUI "+itemViewStates.items.size)
    ContentComposable(
        modifier = modifier,
        recipeUiState = itemViewStates,
        onRecipeClick = onRecipeClick,
        onSearchButtonClick = onSearchButtonClick,
        onRefreshClick = {
            viewModel.refreshList()
        },
        onCategoryClick = {
            viewModel.getOnlineRecipesWithTags(it)
        })

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
            )

        ) {
            val sizeImage by remember { mutableStateOf(IntSize.Zero) }

            val gradient = Brush.verticalGradient(
                colors = listOf(Color.Transparent.copy(alpha = 0.1f), Color.Black.copy(alpha = 0.5f)),
                startY = sizeImage.height.toFloat()/3,  // 1/3
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
                    failure = placeholder(R.drawable.card_shape)
                )
                Box(modifier = Modifier
                    .fillMaxSize()
                    .background(gradient))
                Text(
                    text = itemOnline.title,
                    color = Color.White,
                    modifier = Modifier.align(Alignment.BottomCenter),
                    maxLines = 1,
                    style = MaterialTheme.typography.bodySmall
                )

            }

        }

    }

}
@Composable
fun ContentComposable(
    modifier: Modifier = Modifier,
    recipeUiState: RecipeUiState,
    onRecipeClick: (Recipe) -> Unit,
    onSearchButtonClick: () -> Unit,
    onRefreshClick: () -> Unit,
    onCategoryClick: (String) -> Unit
) {
    var categoryTitle by remember { mutableStateOf("Saved") }

    val isDarkTheme = isSystemInDarkTheme()
    val gradientColors = if (isDarkTheme) {
        listOf(Color.Transparent.copy(alpha = 0.1f), Color.Black.copy(alpha = 0.5f))
    } else {
        listOf(Color.Transparent.copy(alpha = 0.1f), Color.White.copy(alpha = 0.5f))
    }

    val gradient = Brush.verticalGradient(
        colors = gradientColors,
        startY = 0f,
        endY = Float.POSITIVE_INFINITY
    )

    Box {
        // Background Image
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(id = R.drawable.home_background),
            contentDescription = "background_image",
            contentScale = ContentScale.FillBounds
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient)
        )

        // Scaffold with TopAppBar
        Scaffold(
            topBar = {
                RecipeHomeTopAppBar(
                    onRefreshClick = onRefreshClick,
                    onSearchButtonClick = onSearchButtonClick
                )
            },
            containerColor = Color.Transparent
        ) { paddingValues ->

            Column(
                modifier = Modifier
                    .padding(paddingValues)
            ) {
                Text(
                    text = "Categories",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(
                        horizontal = dimensionResource(id = R.dimen.list_item_padding),
                        vertical = dimensionResource(id = R.dimen.vertical_margin)
                    )
                )
                RecipeCategoryComposable(onCategoryClick = { category ->
                    categoryTitle = category
                    onCategoryClick(category)
                })

                RecipeListComposable(
                    recipeUiState = recipeUiState,
                    onRecipeClick = onRecipeClick,
                    textTitle = categoryTitle)

            }
        }
    }
}

@Composable
fun RecipeListComposable(
    recipeUiState: RecipeUiState,
    onRecipeClick: (Recipe) -> Unit,
    textTitle: String = "Saved"
) {
    Column {
        // Saved Recipes Title
        Text(
            text = "$textTitle Recipes",
            modifier = Modifier.padding(
                horizontal = dimensionResource(id = R.dimen.list_item_padding),
                vertical = dimensionResource(id = R.dimen.vertical_margin)
            ),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

        // Show tray image if no recipes are available
        if (recipeUiState.items.isEmpty()) {
            Image(
                painter = painterResource(id = R.drawable.tray_on_hand),
                contentDescription = stringResource(id = R.string.tray_on_hand)
            )
        }

        // Show loading indicator if data is loading, otherwise show recipe list
        if (recipeUiState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            LazyVerticalGrid(GridCells.Fixed(2)) {
                items(recipeUiState.items.size) { index ->
                    val recipe = recipeUiState.items.getOrNull(index)
                    recipe?.let {
                        MyRecipeListItem(
                            itemOnline = it,
                            onRecipeClick = onRecipeClick
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun RecipeCategoryComposable(
    onCategoryClick: (String) -> Unit,
) {
    val categories = listOf(
        Pair("Vegetarian", R.drawable.veggies),
        Pair("Breakfast", R.drawable.breakfast),
        Pair("Lunch", R.drawable.lunch),
        Pair("Dinner", R.drawable.dinner),
        Pair("Appetizer", R.drawable.appetizer),
        Pair("Salad", R.drawable.salad),
        Pair("Dessert", R.drawable.dessert)
    )
    LazyRow(
        modifier = Modifier.padding(
            horizontal = dimensionResource(id = R.dimen.list_item_padding),
            vertical = dimensionResource(id = R.dimen.vertical_margin)
        )
    ) {
        items(categories.size) { category ->
            CircularImageWithText(
                keyValue = categories[category],
                onCategoryClick = onCategoryClick )
        }
    }
}

@Composable
fun CircularImageWithText(
    keyValue: Pair<String, Int>,
    modifier: Modifier = Modifier,
    onCategoryClick: (String) -> Unit
) {
    val (text, resId) = keyValue
    Column(
        modifier = Modifier
            .padding(
                horizontal = dimensionResource(id = R.dimen.list_item_padding),
                vertical = dimensionResource(id = R.dimen.vertical_margin),

            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter =painterResource(id =  resId),
            contentDescription = "Image",
            contentScale = ContentScale.Crop,
            modifier = modifier
                .size(64.dp)
                .clip(CircleShape)
                .border(1.dp, MaterialTheme.colorScheme.onSurface, CircleShape)
                .clickable {
                    onCategoryClick(text)
                })
        Text(text = text,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface)
    }
}
@Preview
@Composable
private fun CircularImageWithTextPreview() {

    MyRecipesTheme {
        Surface {
            CircularImageWithText(
                    Pair("Vegetarian", R.drawable.veggies),
                    Modifier,
                    onCategoryClick = {}
            )
        }
    }

}

