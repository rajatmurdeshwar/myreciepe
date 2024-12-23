package com.murdeshwar.myrecipe.home


import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder

import com.murdeshwar.myrecipe.R
import com.murdeshwar.myrecipe.data.source.Recipe
import com.murdeshwar.myrecipe.ui.theme.MyRecipesTheme

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    onRecipeClick: (Recipe) -> Unit,
    onSearchButtonClick: () -> Unit
) {
    val recipeUiState by viewModel.recipeUiState.collectAsStateWithLifecycle()
    var categoryTitle by remember { mutableStateOf("Random") }


    Box {
        // Background Image
        Image(
            modifier = modifier.fillMaxSize(),
            painter = painterResource(id = R.drawable.home_background),
            contentDescription = "background_image",
            contentScale = ContentScale.FillBounds
        )
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent.copy(alpha = 0.1f),
                            if (isSystemInDarkTheme()) Color.Black.copy(alpha = 0.5f)
                            else Color.White.copy(alpha = 0.5f)
                        )
                    )
                )
        )

            LazyColumn(
            ) {
                item {
                    SearchBarButton (
                        onSearchIconClick = onSearchButtonClick
                    )
                }
                item {
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
                        viewModel.getOnlineRecipesWithTags(category)
                    })
                }
                item {
                    FeaturedBanner(recipeUiState = recipeUiState, onRecipeClick = onRecipeClick)
                }
                item {
                    SeasonalBanner(recipeUiState = recipeUiState, onRecipeClick = onRecipeClick)
                }
            }
        }

}

@Composable
fun SearchBarButton(
    modifier: Modifier = Modifier,
    placeHolderText: String = "Search recipes or ingredients....",
    onSearchIconClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)
            .clickable(
                interactionSource = remember {
                    MutableInteractionSource()
                },
                indication = rememberRipple(
                    bounded = true,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                onClick = { onSearchIconClick() }
            )
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = placeHolderText,
            style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
            modifier = Modifier.weight(1f)
        )

        IconButton(onClick = { onSearchIconClick() }) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Tap to search recipes or ingredients",
                tint = MaterialTheme.colorScheme.primary
            )
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
            painter = painterResource(id =  resId),
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

@OptIn(ExperimentalFoundationApi::class, ExperimentalGlideComposeApi::class)
@Composable
fun FeaturedBanner(
    recipeUiState: RecipeUiState,
    onRecipeClick: (Recipe) -> Unit,
) {

    // State for managing the pager
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { recipeUiState.items.size} // Total number of pages
    )

    Column {
        // Title for the banner
        Text(
            text = "Recipe of the Day",
            modifier = Modifier.padding(
                horizontal = dimensionResource(id = R.dimen.list_item_padding),
                vertical = dimensionResource(id = R.dimen.vertical_margin)
            ),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        if (recipeUiState.items.isEmpty()) {
            Image(
                painter = painterResource(id = R.drawable.tray_on_hand),
                contentDescription = stringResource(id = R.string.tray_on_hand)
            )
        }
        if (recipeUiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            // Horizontal Pager for scrolling through cards
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp) // Set the height for the pager
            ) { page ->
                val recipe = recipeUiState.items[page]
                val recipeImage = recipe?.itemImage ?: R.drawable.card_shape
                // Each page is an image card
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    ),
                    modifier = Modifier
                        .padding(8.dp) // Add spacing between cards
                        .fillMaxSize(), // Fill the available space
                    onClick = {
                        recipe?.let { onRecipeClick(it) }
                    }
                ) {
                    GlideImage(
                        model = recipeImage,
                        contentDescription = "Recipe Image for Page $page",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop

                    ) {
                        it.error{ placeholder(R.drawable.card_shape) }
                    }

                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            DotsIndicator(
                totalDots = recipeUiState.items.size,
                selectedIndex = pagerState.currentPage
            )
        }
    }
}

@Composable
fun DotsIndicator(
    totalDots: Int,
    selectedIndex: Int,
    dotSize: Dp = 8.dp,
    dotSpacing: Dp = 8.dp,
    selectedColor: Color = Color.Green,
    unselectedColor: Color = Color.Gray
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        for (i in 0 until totalDots) {
            val color by animateColorAsState(
                targetValue = if (i == selectedIndex) selectedColor else unselectedColor, label = ""
            )
            Box(
                modifier = Modifier
                    .size(dotSize)
                    .padding(horizontal = dotSpacing / 2)
                    .background(color, CircleShape)
            )
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun SeasonalBanner(
    recipeUiState: RecipeUiState,
    onRecipeClick: (Recipe) -> Unit,
){
    Column {
        // Title for the banner
        Text(
            text = "Holiday Special",
            modifier = Modifier.padding(
                horizontal = dimensionResource(id = R.dimen.list_item_padding),
                vertical = dimensionResource(id = R.dimen.vertical_margin)
            ),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        if (recipeUiState.items.isEmpty()) {
            Image(
                painter = painterResource(id = R.drawable.tray_on_hand),
                contentDescription = stringResource(id = R.string.tray_on_hand)
            )
        }
        if (recipeUiState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            LazyVerticalGrid(
                GridCells.Fixed(2),
                modifier = Modifier.height(500.dp)
            ) {
                items(recipeUiState.items.size) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        ),
                        modifier = Modifier
                            .padding(8.dp) // Add spacing between cards
                            .fillMaxSize(), // Fill the available space
                        onClick = {
                            recipeUiState.items[it]?.let { it1 -> onRecipeClick(it1) }
                        }
                    ) {

                        Column {
                            GlideImage(
                                model = recipeUiState.items[it]?.itemImage,
                                contentDescription = "Recipe Image for Page ",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop,
                                failure = placeholder(R.drawable.card_shape)
                            )
                            recipeUiState.items[it]?.let { it1 ->
                                Text(
                                    text = it1.title,
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(4.dp),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }

                    }
                }

            }
        }
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

