package com.example.myrecipes.details

import androidx.compose.foundation.BorderStroke

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue

import androidx.compose.ui.Modifier

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.example.myrecipes.R
import com.example.myrecipes.util.RecipeDetailTopAppBar
import com.example.myrecipes.data.source.Recipe
import com.example.myrecipes.ui.theme.MyRecipesTheme
import timber.log.Timber


@Composable
fun RecipeDetailScreen(
    modifier: Modifier = Modifier,
    recipeId: Int,
    onBack: () -> Unit,
    viewModel: RecipeDetailsViewModel = hiltViewModel()
) {
    MyRecipesTheme {
        Timber.d("RecipeDetailScreen", "recipe ID $recipeId")
        val recipe by viewModel.recipe.collectAsStateWithLifecycle()

        LaunchedEffect(recipeId) {
            viewModel.getRecipeByID(recipeId)
        }

        recipe?.let {
            DetailComposable(modifier = modifier, onBack, recipe = it)
            Timber.d("RecipeDetailScreen", "Loaded recipe: $it")
        } ?: run {
            Timber.d("RecipeDetailScreen", "Loading recipe...")
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalLayoutApi::class)
@Composable
fun DetailComposable(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    recipe: Recipe,
) {
    Scaffold(
        topBar = {
            RecipeDetailTopAppBar(recipe.title, onBack = onBack)
        },
        content = { paddingValues ->
            Column(
                modifier = modifier
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
            ) {
                // Recipe Image
                GlideImage(
                    model = recipe.itemImage,
                    contentDescription = "Recipe Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                    failure = placeholder(R.drawable.card_shape)
                )

                // Chip items (servings, health score, ready time)
                FlowRow(modifier = Modifier.padding(6.dp)) {
                    ChipItem("Servings: ${recipe.servings}")
                    ChipItem("Health Score: ${recipe.healthScore}")
                    ChipItem("Ready In: ${recipe.readyIn} mins")
                }

                // Summary Section
                Text(
                    text = "Summary",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(top = 8.dp)
                )
                Text(
                    text = recipe.description,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                // Instructions Section
                Text(
                    text = "Instructions",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(top = 8.dp)
                )
                Text(
                    text = recipe.instructions,
                    style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.primary),
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
    )
}

@Composable
fun ChipItem(text: String, onClick: () -> Unit = {}) {
    FilterChip(
        modifier = Modifier.padding(end = 4.dp),
        onClick = onClick,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface),
        label = {
            Text(text, color = MaterialTheme.colorScheme.onSurface)
        },
        selected = false
    )
}