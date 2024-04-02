package com.example.myrecipes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myrecipes.RecipeDestinationsArgs.RECIPE_ID_ARG
import com.example.myrecipes.presentation.HomeScreen

@Composable
fun RecipeNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = "recipes",
    navActions: RecipeNavigationActions = remember(navController) {
        RecipeNavigationActions(navController)
    }
) {



    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {

        composable("recipes") {
            HomeScreen(
                onRecipeClick = { recipeId -> navActions.navigateToRecipeDetail(recipeId.uid) }
            )
        }

        composable(
            RecipeDestinations.RECIPE_DETAIL_ROUTE,
            arguments = listOf(
                navArgument(RECIPE_ID_ARG) {
                    type = NavType.IntType
                    defaultValue = 0
                }
            )
        ) { navBackStackEntry ->
            val recipeId = navBackStackEntry.arguments?.getInt(RECIPE_ID_ARG)
            recipeId?.let { RecipeDetailScreen(recipeId = it) }
        }
    }
}