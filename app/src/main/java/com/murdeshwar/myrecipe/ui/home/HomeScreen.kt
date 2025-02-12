package com.murdeshwar.myrecipe.ui.home


import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
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
import com.murdeshwar.myrecipe.ui.common.shimmerEffect
import com.murdeshwar.myrecipe.ui.onboarding.PageIndicator
import com.murdeshwar.myrecipe.ui.theme.MyRecipesTheme


@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onRecipeClick: (Recipe) -> Unit,
    onSearchButtonClick: () -> Unit
) {
    val recipeUiState by viewModel.recipeUiState.collectAsStateWithLifecycle()
    var categoryTitle by remember { mutableStateOf("Random") }

    // Use LazyColumn to handle a large amount of data efficiently
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp) // Add padding for better spacing
    ) {
        // Search Bar
        item {
            SearchBarButton(
                onSearchIconClick = onSearchButtonClick,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }

        // Categories Section
        item {
            Text(
                text = "Categories",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            RecipeCategoryComposable(
                onCategoryClick = { category ->
                    categoryTitle = category
                    viewModel.getOnlineRecipesWithTags(category)
                },
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        // Featured Banner Section
        item {
            FeaturedBanner(
                recipeUiState = recipeUiState,
                onRecipeClick = onRecipeClick,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        // Seasonal Banner Section
        item {
            SeasonalBanner(
                recipeUiState = recipeUiState,
                onRecipeClick = onRecipeClick,
                modifier = Modifier.padding(vertical = 8.dp)
            )
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
    modifier: Modifier
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
                horizontal = dimensionResource(id = R.dimen.horizontal_margin),
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
    modifier: Modifier
) {
    // State for managing the pager
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { recipeUiState.items.size } // Total number of pages
    )

    Column {
        // Title for the banner
        Text(
            text = "Recipe of the Day",
            modifier = modifier.padding(
                horizontal = dimensionResource(id = R.dimen.list_item_padding),
                vertical = dimensionResource(id = R.dimen.vertical_margin)
            ),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

        // Show shimmer effect or content based on loading state
        if (recipeUiState.isLoading) {
            // Shimmer effect for the loading state
            HorizontalPager(
                state = rememberPagerState(pageCount = { 3 }), // Show 3 shimmer placeholders
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            ) { page ->
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    ),
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxSize(),
                    elevation = CardDefaults.cardElevation(defaultElevation = Elevation)
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        // Shimmer effect for the image
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .shimmerEffect()
                        )

                        // Shimmer effect for the gradient overlay
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Transparent,
                                            Color.Black.copy(alpha = 0.7f)
                                        ),
                                        startY = 300f
                                    )
                                )
                        )

                        // Shimmer effect for the recipe title
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .fillMaxWidth(0.8f)
                                .height(24.dp)
                                .padding(12.dp)
                                .shimmerEffect()
                        )
                    }
                }
            }

            // Shimmer effect for the page indicator
            Row(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = 8.dp)
            ) {
                repeat(3) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .shimmerEffect()
                            .padding(horizontal = 4.dp)
                    )
                }
            }
        } else if (recipeUiState.items.isEmpty()) {
            // Show empty state
            EmptyScreen()
        } else {
            // Show actual content
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            ) { page ->
                val recipe = recipeUiState.items[page]
                val recipeImage = recipe?.itemImage ?: R.drawable.card_shape
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    ),
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxSize(),
                    onClick = {
                        recipe?.let { onRecipeClick(it) }
                    },
                    elevation = CardDefaults.cardElevation(defaultElevation = Elevation)
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        // Recipe Image (Thumbnail)
                        GlideImage(
                            model = recipeImage,
                            contentDescription = "Recipe Image for Page $page",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        ) {
                            it.error { placeholder(R.drawable.card_shape) }
                        }

                        // Gradient Overlay for better text readability
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Transparent,
                                            Color.Black.copy(alpha = 0.7f)
                                        ),
                                        startY = 300f
                                    )
                                )
                        )

                        // Recipe Name Text - Positioned at the bottom center
                        if (recipe != null) {
                            Text(
                                text = recipe.title,
                                color = Color.White,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .padding(12.dp)
                            )
                        }
                    }
                }
            }

            // Page Indicator
            Spacer(modifier = Modifier.height(8.dp))
            PageIndicator(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                pageSize = recipeUiState.items.size,
                selectedPage = pagerState.currentPage
            )
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun SeasonalBanner(
    recipeUiState: RecipeUiState,
    onRecipeClick: (Recipe) -> Unit,
    modifier: Modifier
) {
    val seasonalTag = remember { SeasonalTags.getCurrentSeasonalTag() }
    val seasonalText = seasonalTagToText(seasonalTag)

    Column {
        // Title for the banner
        Text(
            text = seasonalText,
            modifier = modifier.padding(
                horizontal = dimensionResource(id = R.dimen.list_item_padding),
                vertical = dimensionResource(id = R.dimen.vertical_margin)
            ),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

        // Show shimmer effect or content based on loading state
        if (recipeUiState.isLoading) {
            // Shimmer effect for the loading state
            LazyVerticalGrid(
                GridCells.Fixed(2),
                modifier = Modifier.height(500.dp)
            ) {
                items(6) { // Show 6 shimmer placeholders
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        ),
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxSize(),
                        elevation = CardDefaults.cardElevation(defaultElevation = Elevation)
                    ) {
                        Column {
                            // Shimmer effect for the image
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .shimmerEffect()
                            )

                            // Shimmer effect for the title
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(24.dp)
                                    .padding(4.dp)
                                    .shimmerEffect()
                            )
                        }
                    }
                }
            }
        } else if (recipeUiState.seasonalRecipes.isEmpty()) {
            // Show empty state image
            Image(
                painter = painterResource(id = R.drawable.tray_on_hand),
                contentDescription = stringResource(id = R.string.tray_on_hand)
            )
        } else {
            // Show actual content
            LazyVerticalGrid(
                GridCells.Fixed(2),
                modifier = Modifier.height(500.dp)
            ) {
                items(recipeUiState.seasonalRecipes.size) { index ->
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        ),
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxSize(),
                        onClick = {
                            recipeUiState.seasonalRecipes[index]?.let { recipe ->
                                onRecipeClick(recipe)
                            }
                        },
                        elevation = CardDefaults.cardElevation(defaultElevation = Elevation)
                    ) {
                        Column {
                            GlideImage(
                                model = recipeUiState.seasonalRecipes[index]?.itemImage,
                                contentDescription = "Recipe Image for Page $index",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop,
                                failure = placeholder(R.drawable.card_shape)
                            )
                            recipeUiState.seasonalRecipes[index]?.let { recipe ->
                                Text(
                                    text = recipe.title,
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(4.dp),
                                    color = MaterialTheme.colorScheme.onSurface,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

fun seasonalTagToText(tag: String): String {
    return when (tag) {
        "new-year" -> "New Year Special 🎉"
        "valentines-day" -> "Valentine's Day Special ❤️"
        "halloween" -> "Halloween Treats 🎃"
        "christmas" -> "Christmas Delights 🎄"
        "spring" -> "Spring Fresh Recipes 🌸"
        "summer" -> "Summer Refreshments ☀️"
        "autumn" -> "Autumn Flavors 🍂"
        "winter" -> "Winter Warmers ❄️"
        else -> "Holiday Special"
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
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

