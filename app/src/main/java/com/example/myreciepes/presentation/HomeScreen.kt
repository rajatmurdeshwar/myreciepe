package com.example.myreciepes.presentation


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

import com.example.myreciepes.R
import com.example.myreciepes.Recipe

@Composable
fun HomeScreen(modifier: Modifier = Modifier,viewModel: HomeViewModel = hiltViewModel()) {
    viewModel.getReciepeDetails()
    val itemViewStates by viewModel.recipeList.collectAsStateWithLifecycle()

    SimpleComposable(modifier = modifier,itemViewStates)

}

@Composable
fun MyRecipeListItem(itemViewState: Recipe) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = dimensionResource(id = R.dimen.horizontal_margin),
                vertical = dimensionResource(id = R.dimen.list_item_padding),
            )
    ) {
        Text(text = itemViewState.tittle)
    }

}
@Composable
fun SimpleComposable(
    modifier: Modifier = Modifier,
    itemViewStates: List<Recipe>,
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
                MyRecipeListItem(itemViewState = data)

            }
        }
    }


}