package com.murdeshwar.myrecipe.ui.home


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.murdeshwar.myrecipe.data.source.Recipe
import com.murdeshwar.myrecipe.ui.favorite.RecipeListComposable


@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onRecipeClick: (Recipe) -> Unit,
    onSearchButtonClick: () -> Unit
) {
    var selectedIndex by remember { mutableIntStateOf(0) }

Column {
    SearchBarButton(
        onSearchIconClick = onSearchButtonClick,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )

    Text(
        text = "Categories",
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )
    val categories = listOf(
        "Explore",
        "Vegetarian",
        "Breakfast",
        "Lunch",
        "Dinner",
        "Appetizer",
        "Salad",
        "Dessert"
    )

    RecipeCategoryTabRow(modifier = Modifier, categories = categories,selectedIndex = selectedIndex,
        onTabSelected = { index ->
            selectedIndex = index
            viewModel.getOnlineRecipesWithTags(categories[index])

        }, onRecipeClick, viewModel)
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
            modifier = modifier.weight(1f)
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

@OptIn( ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun RecipeCategoryTabRow(
    modifier: Modifier,
    categories: List<String>,
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit,
    onRecipeClick: (Recipe) -> Unit,
    viewModel: HomeViewModel
) {
    val pagerState =
        rememberPagerState(initialPage = selectedIndex, pageCount = { categories.size })
    val recipeUiState by viewModel.recipeUiState.collectAsStateWithLifecycle()
    // Sync pager with tab click
    LaunchedEffect(selectedIndex) {
        pagerState.scrollToPage(selectedIndex)
    }

    Column {
        ScrollableTabRow(
            selectedTabIndex = pagerState.currentPage,
            edgePadding = 0.dp,
            indicator = { tabPositions ->
                Column(
                    modifier = modifier
                        .tabIndicatorOffset(tabPositions[pagerState.currentPage])
                        .fillMaxSize()
                        .padding(10.dp)
                        .background(
                            MaterialTheme.colorScheme.secondaryContainer,
                            FilterChipDefaults.shape,
                        )
                ) {}
            }
        ) {
            categories.forEachIndexed { index, category ->
                FilterChip(
                    modifier = modifier.wrapContentSize()
                        .zIndex(2f),
                    selected = index == selectedIndex,
                    border = null,
                    onClick = { onTabSelected(index) },
                    label = {
                        Text(
                            text = category,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                )
            }
        }

        HorizontalPager(state = pagerState) { page ->
            // Your actual page content for each category
            when (page) {
                0 -> {
                    ExploreScreen(
                        viewModel = viewModel,
                        onRecipeClick = onRecipeClick
                    )
                }

                else -> {
                    RecipeListComposable(
                        modifier = modifier,
                        recipeUiState = recipeUiState,
                        onRecipeClick = onRecipeClick,
                        textTitle = categories[page],
                        enablePullToRefresh = false
                    )
                }
            }
        }
    }
}

