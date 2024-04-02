package com.example.myrecipes.presentation


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage

import com.example.myrecipes.R
import com.example.myrecipes.data.source.Recipe
import com.example.myrecipes.data.source.local.LocalRecipe

@Composable
fun HomeScreen(modifier: Modifier = Modifier,viewModel: HomeViewModel = hiltViewModel(), onRecipeClick: (LocalRecipe) -> Unit,) {
    viewModel.getRecipeDetails()
    val itemViewStates by viewModel.recipeList.collectAsStateWithLifecycle()

    SimpleComposable(modifier = modifier,itemViewStates, onRecipeClick = onRecipeClick)

}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MyRecipeListItem(
    itemViewState: LocalRecipe,
    onRecipeClick: (LocalRecipe) -> Unit,
    ) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = dimensionResource(id = R.dimen.horizontal_margin),
                vertical = dimensionResource(id = R.dimen.list_item_padding),
            )
            .clickable { onRecipeClick(itemViewState) }
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
            ),
            modifier = Modifier
                .size(width = 240.dp, height = 100.dp)
        ) {
            Text(text = itemViewState.tittle)
            
            GlideImage(model = itemViewState.img, contentDescription = "")

        }

    }

}
@Composable
fun SimpleComposable(
    modifier: Modifier = Modifier,
    itemViewStates: List<LocalRecipe>,
    onRecipeClick: (LocalRecipe) -> Unit,
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = dimensionResource(id = R.dimen.horizontal_margin))
    ) {
        Text(text = "Best Diet",
            modifier = Modifier.padding(
                horizontal = dimensionResource(id = R.dimen.list_item_padding),
                vertical = dimensionResource(id = R.dimen.vertical_margin)
            ),
            style = MaterialTheme.typography.headlineMedium
        )
        LazyColumn(modifier = modifier) {
            items(itemViewStates) {data ->
                MyRecipeListItem(
                    itemViewState = data,
                    onRecipeClick = onRecipeClick,
                )

            }
        }
    }


}