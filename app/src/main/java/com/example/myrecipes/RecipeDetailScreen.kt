package com.example.myrecipes

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue

import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.myrecipes.data.source.local.LocalRecipe



@Composable
fun RecipeDetailScreen(modifier: Modifier = Modifier,recipeId: Int, viewModel: RecipeDetailsViewModel = hiltViewModel()) {
    Log.d("RecipeDetailScreen", "recipe ID $recipeId")
    val recipe by viewModel.recipe.collectAsStateWithLifecycle()
    LaunchedEffect(recipeId) {
        viewModel.getRecipeByID(recipeId)
    }

    recipe?.let {
        DetailComposable(modifier = modifier, recipe = it)
        Log.d("RecipeDetailScreen", "recipe ID is $it")
    }
    Log.d("RecipeDetailScreen", "recipe End")
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun DetailComposable(
    modifier: Modifier = Modifier,
    recipe: LocalRecipe,
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
        GlideImage(model = recipe.img, contentDescription = "")
        Text(text = recipe.tittle)
        Text(text = recipe.des)
    }

}