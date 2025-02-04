package com.murdeshwar.myrecipe.ui.details

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate

import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.murdeshwar.myrecipe.R
import com.murdeshwar.myrecipe.util.RecipeDetailTopAppBar

import com.murdeshwar.myrecipe.data.source.RecipeWithDetails
import com.murdeshwar.myrecipe.ui.theme.MyRecipesTheme
import timber.log.Timber


@Composable
fun RecipeDetailScreen(
    modifier: Modifier = Modifier,
    recipeId: Int,
    onBack: () -> Unit,
    viewModel: RecipeDetailsViewModel = hiltViewModel()
) {
    BackHandler { onBack() }
    MyRecipesTheme {
        Timber.d("RecipeDetailScreen", "recipe ID $recipeId")
        val recipe by viewModel.recipe.collectAsStateWithLifecycle()
        val userMessage by viewModel.userMessage.collectAsStateWithLifecycle()

        var saved by remember { mutableStateOf(false) }
        val rotation by animateFloatAsState(if (saved) 360f else 0f, label = "rotation")

        val snackbarHostState = remember { SnackbarHostState() }

        // Fetch recipe when the screen opens
        LaunchedEffect(recipeId) {
            viewModel.getRecipeByID(recipeId)
        }
        // Show Snackbar when userMessage updates
        LaunchedEffect(userMessage) {
            userMessage?.let { message ->
                snackbarHostState.showSnackbar(message)
                viewModel.clearUserMessage() // Clear message after showing
            }
        }

        recipe?.recipeWithDetails?.let { recipeDetails ->
            Scaffold(
                snackbarHost = { SnackbarHost(snackbarHostState) {
                    Snackbar(
                        snackbarData = it,
                        modifier = Modifier.padding(8.dp),
                        shape = RoundedCornerShape(12.dp),
                    )
                } },
                topBar = {
                    RecipeDetailTopAppBar(recipeDetails.recipe.title, onBack = onBack)
                },
                floatingActionButton = {
                    FloatingActionButton(
                        modifier = Modifier.testTag("fab_save_recipe"),
                        onClick = {
                            viewModel.saveRecipeToRemoteAndLocal(recipe = recipeDetails)
                            saved = !saved },
                        shape = CircleShape,
                        containerColor = if (saved) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                    ) {
                        Icon(
                            imageVector = if (saved) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = "Save Recipe",
                            modifier = Modifier.rotate(rotation)
                        )
                    }
                },
                content = { paddingValues ->
                    DetailComposable(
                        modifier = modifier,
                        paddingValues = paddingValues,
                        recipe = recipeDetails
                    )
                })
        } ?: run {
            Box(
                modifier = modifier.fillMaxSize().testTag("loading_indicator"),
                contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalLayoutApi::class)
@Composable
fun DetailComposable(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    recipe: RecipeWithDetails,
) {
    AnimatedVisibility(
        visible = true,
        enter = fadeIn(animationSpec = tween(500)) + slideInVertically(),
        exit = fadeOut(animationSpec = tween(500)) + slideOutVertically()
    ) {

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
                    .height(dimensionResource(id = R.dimen.image_height)),
                contentScale = ContentScale.Crop,
                loading = placeholder(R.drawable.loading_placeholder),
                failure = placeholder(R.drawable.card_shape)
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_xlarge)))

            Text(
                text = recipe.recipe.title,
                fontSize = dimensionResource(id = R.dimen.text_size_large).value.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(id = R.dimen.padding_large))
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_xlarge)))

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
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_xlarge)))

            // Summary Section
            Text(
                text = "Summary",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(
                    horizontal = dimensionResource(id = R.dimen.padding_large),
                    vertical = dimensionResource(id = R.dimen.padding_small)
                )
            )
            Text(
                text = recipe.recipe.description,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(
                    horizontal = dimensionResource(id = R.dimen.padding_large),
                    vertical = dimensionResource(id = R.dimen.padding_small)
                )
            )

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_xlarge)))

            Text(
                text = "Ingredients",
                fontWeight = FontWeight.Bold,
                fontSize = dimensionResource(id = R.dimen.text_size_medium).value.sp,
                modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_large))
            )

            recipe.ingredients.forEach { ingredient ->
                Text(
                    text = "${ingredient.originalName}: ${ingredient.amount} ${ingredient.unit}",
                    fontSize = dimensionResource(id = R.dimen.text_size_small).value.sp,
                    modifier = Modifier.padding(
                        horizontal = dimensionResource(id = R.dimen.padding_large),
                        vertical = dimensionResource(id = R.dimen.padding_small)
                    )
                )
            }

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_xlarge)))

            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(id = R.dimen.padding_large)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(Modifier.padding(dimensionResource(id = R.dimen.padding_large))) {
                    Text(
                        text = "Instructions",
                        fontWeight = FontWeight.Bold,
                        fontSize = dimensionResource(id = R.dimen.text_size_medium).value.sp
                    )
                    recipe.steps.forEach { step ->
                        Text(
                            text = "${step.number}. ${step.step}",
                            fontSize = dimensionResource(id = R.dimen.text_size_small).value.sp,
                            modifier = Modifier.padding(vertical = dimensionResource(id = R.dimen.padding_small))
                        )
                    }
                }
            }
        }
    }

}

@Composable
fun ChipItem(text: String) {
    var selected by remember { mutableStateOf(false) }
    FilterChip(
        modifier = Modifier.padding(end = dimensionResource(id = R.dimen.padding_small)),
        onClick = { selected = !selected },
        border = BorderStroke(dimensionResource(id = R.dimen.chip_border_stroke), MaterialTheme.colorScheme.onSurface),
        label = {
            Text(text, color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface)
        },
        selected = selected
    )
}