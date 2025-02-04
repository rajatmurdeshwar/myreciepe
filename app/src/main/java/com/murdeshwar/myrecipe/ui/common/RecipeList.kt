package com.murdeshwar.myrecipe.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.murdeshwar.myrecipe.Dimens.ExtraSmallPadding2
import com.murdeshwar.myrecipe.Dimens.MediumPadding1
import com.murdeshwar.myrecipe.data.source.Recipe

@Composable
fun RecipeList(
    modifier: Modifier = Modifier,
    recipes: List<Recipe?>,
    onClick: (Recipe) -> Unit,
) {
    if (recipes.isEmpty()){
        EmptyScreen()
        return
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(MediumPadding1),
        contentPadding = PaddingValues(all = ExtraSmallPadding2)
    ) {
        items(count = recipes.size) {
            val recipe = recipes[it]
            if (recipe != null) {
                RecipeCard(recipe = recipe, onClick = { onClick(recipe) })
            }

        }

    }


}


@Composable
fun ShimmerEffect() {
    Column(verticalArrangement = Arrangement.spacedBy(MediumPadding1)) {
        repeat(10) {
            RecipeShimmerEffect(
                modifier = Modifier.padding(horizontal = MediumPadding1)
            )
        }

    }

}