package com.example.myrecipes.details

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.myrecipes.RecipeDetailTopAppBar
import com.example.myrecipes.data.source.Recipe
import com.example.myrecipes.data.source.local.LocalRecipe


@Composable
fun RecipeDetailScreen(modifier: Modifier = Modifier,recipeId: Int, onBack: () -> Unit, viewModel: RecipeDetailsViewModel = hiltViewModel()) {
    Log.d("RecipeDetailScreen", "recipe ID $recipeId")
    val recipe by viewModel.recipe.collectAsStateWithLifecycle()
    LaunchedEffect(recipeId) {
        viewModel.getRecipeByID(recipeId)
    }

    recipe?.let {
        DetailComposable(modifier = modifier, onBack,recipe = it)
        Log.d("RecipeDetailScreen", "recipe ID is $it")
    }
    Log.d("RecipeDetailScreen", "recipe End")
}

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalMaterial3Api::class)
@Composable
fun DetailComposable(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    recipe: Recipe,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            RecipeDetailTopAppBar(recipe.title,onBack = onBack)
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
        ) {

            GlideImage(
                model = recipe.itemImage,
                contentDescription = "",
                modifier = Modifier.fillMaxSize())
            // Display recipe details in Rows
            RecipeDetailRow("Servings:", recipe.servings.toString())
            RecipeDetailRow("Health Score:", recipe.healthScore.toString())
            RecipeDetailRow("Ready In:", recipe.readyIn.toString())
            Text(
                text = "Summary",
                fontWeight = FontWeight.Bold,
                )
            Text(
                text = recipe.description,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Text(
                text = "Instructions",
                fontWeight = FontWeight.Bold)
            Text(
                text = recipe.instructions,
                color = Color.Blue)

        }
    }
}

@Composable
fun RecipeDetailRow(label: String, value: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Text(text = label, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = value)
    }
}