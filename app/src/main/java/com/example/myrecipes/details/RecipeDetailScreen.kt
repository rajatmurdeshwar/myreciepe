package com.example.myrecipes.details

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier

import androidx.compose.ui.layout.ContentScale

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.example.myrecipes.R
import com.example.myrecipes.util.RecipeDetailTopAppBar

import com.example.myrecipes.data.source.RecipeWithDetails
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
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            Timber.d("RecipeDetailScreen", "Loading recipe...")
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalLayoutApi::class)
@Composable
fun DetailComposable(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    recipe: RecipeWithDetails,
) {
    Scaffold(
        topBar = {
            RecipeDetailTopAppBar(recipe.recipe.title, onBack = onBack)
        },
        content = { paddingValues ->
            Column(
                modifier = modifier
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
            ) {
                // Recipe Image
                GlideImage(
                    model = recipe.recipe.itemImage,
                    contentDescription = "Recipe Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Crop,
                    loading = placeholder(R.drawable.loading_placeholder),
                    failure = placeholder(R.drawable.card_shape)
                )
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = recipe.recipe.title,
                    fontSize = 24.sp,
                    fontWeight =  FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "Ingredients",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                
                recipe.ingredients.forEach {ingredient -> 
                    Text(
                        text = "${ingredient.originalName}: ${ingredient.amount} ${ingredient.unit}",
                        fontSize = 16.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                    
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "Instructions",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                
                recipe.steps.forEach{step -> 
                    Text(
                        text = "${step.number}. ${step.step}",
                        fontSize = 16.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                    
                }


                Spacer(modifier = Modifier.height(16.dp))

                // Chip items (servings, health score, ready time)
                FlowRow(modifier = Modifier.padding(6.dp)) {
                    ChipItem("Servings: ${recipe.recipe.servings}")
                    ChipItem("Health Score: ${recipe.recipe.healthScore}")
                    ChipItem("Ready In: ${recipe.recipe.readyIn} mins")
                    if (recipe.recipe.vegan == true) {
                        ChipItem("Vegan")
                    }
                    if (recipe.recipe.glutenFree == true) {
                        ChipItem("Gluten Free")
                    }
                    if (recipe.recipe.dairyFree == true) {
                        ChipItem("Dairy Free")
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                // Summary Section
                Text(
                    text = "Summary",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
                Text(
                    text = recipe.recipe.description,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
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