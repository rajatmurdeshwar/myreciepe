package com.murdeshwar.myrecipe.ui.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
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
import com.murdeshwar.myrecipe.util.RecipeThumbnailCard
import kotlinx.coroutines.delay

@Composable
fun ExploreScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onRecipeClick: (Recipe) -> Unit,
) {
    val recipeUiState by viewModel.recipeUiState.collectAsStateWithLifecycle()


    // Use LazyColumn to handle a large amount of data efficiently
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp) // Add padding for better spacing
    ) {
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
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FeaturedBanner(
    recipeUiState: RecipeUiState,
    onRecipeClick: (Recipe) -> Unit,
    modifier: Modifier = Modifier
) {
    // State for managing the pager
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { recipeUiState.items.size } // Total number of pages
    )
    val pageCount = recipeUiState.items.size

    LaunchedEffect(key1 = pagerState.currentPage) {
        if (!pagerState.isScrollInProgress && pageCount > 0) {
            delay(3000)
            val nextPage = (pagerState.currentPage + 1) % pageCount
            pagerState.animateScrollToPage(nextPage)
        }
    }

    Column {
        // Title for the banner
        BannerTitle("Recipe of the Day", modifier)

        // Show shimmer effect or content based on loading state
        when {
            recipeUiState.isLoading -> ShimmerBannerPlaceholder()
            recipeUiState.items.isEmpty() -> EmptyScreen()
            else -> {
                HorizontalPager(
                    state = pagerState,
                    modifier = modifier
                        .fillMaxWidth()
                        .height(250.dp)
                ) { page ->
                    recipeUiState.items[page]?.let { recipe ->
                        RecipeBannerCard(recipe, onRecipeClick)
                    }
                }
                // Page Indicator
                Spacer(modifier = modifier.height(8.dp))
                PageIndicator(
                    modifier = modifier.align(Alignment.CenterHorizontally),
                    pageSize = recipeUiState.items.size,
                    selectedPage = pagerState.currentPage
                )
            }
        }

    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ShimmerBannerPlaceholder() {
    HorizontalPager(
        state = rememberPagerState(pageCount = { 3 }),
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            modifier = Modifier.padding(8.dp).fillMaxSize(),
            elevation = CardDefaults.cardElevation(defaultElevation = Elevation)
        ) {
            Box(
                modifier = Modifier.fillMaxSize().shimmerEffect()
            )
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun RecipeBannerCard(recipe: Recipe, onRecipeClick: (Recipe) -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        modifier = Modifier.padding(8.dp).fillMaxSize(),
        onClick = { onRecipeClick(recipe) },
        elevation = CardDefaults.cardElevation(defaultElevation = Elevation)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            GlideImage(
                model = recipe.itemImage ?: R.drawable.card_shape,
                contentDescription = "Recipe Image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            ) { it.error { placeholder(R.drawable.card_shape) } }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f)),
                        startY = 300f
                    ))
            )

            Text(
                text = recipe.title,
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.align(Alignment.BottomCenter).padding(12.dp)
            )
        }
    }
}


@Composable
fun SeasonalBanner(
    recipeUiState: RecipeUiState,
    onRecipeClick: (Recipe) -> Unit,
    modifier: Modifier = Modifier
) {
    val seasonalTag = remember { SeasonalTags.getCurrentSeasonalTag() }
    val seasonalText = seasonalTagToText(seasonalTag)

    Column {
        BannerTitle(seasonalText, modifier)
        when {
            recipeUiState.isLoading -> SeasonalShimmerGrid(6)
            recipeUiState.seasonalRecipes.isEmpty() -> EmptyState()
            else -> SeasonalRecipeGrid(
                modifier = modifier,
                recipeUiState,
                onRecipeClick)
        }
    }
}

@Composable
fun BannerTitle(text: String, modifier: Modifier) {
    Text(
        text = text,
        modifier = modifier.padding(
            horizontal = dimensionResource(id = R.dimen.list_item_padding),
            vertical = dimensionResource(id = R.dimen.vertical_margin)
        ),
        style = MaterialTheme.typography.headlineMedium,
        color = MaterialTheme.colorScheme.onSurface
    )
}

@Composable
fun SeasonalShimmerGrid(count: Int) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.height(500.dp)
    ) {
        items(count) {
            ShimmerCard()
        }
    }
}

@Composable
fun SeasonalRecipeGrid(
    modifier: Modifier,
    recipeUiState: RecipeUiState,
    onRecipeClick: (Recipe) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.height(500.dp)
    ) {
        items(recipeUiState.seasonalRecipes.size) { index ->
            recipeUiState.seasonalRecipes[index]?.let { recipe ->
                RecipeThumbnailCard(
                    modifier = modifier,
                    recipe,
                    onRecipeClick)
            }
        }
    }
}

@Composable
fun ShimmerCard() {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize(),
        elevation = CardDefaults.cardElevation(defaultElevation = Elevation)
    ) {
        Column {
            Box(modifier = Modifier.fillMaxSize().shimmerEffect())
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

@Composable
fun EmptyState() {
    Image(
        painter = painterResource(id = R.drawable.tray_on_hand),
        contentDescription = stringResource(id = R.string.tray_on_hand)
    )
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