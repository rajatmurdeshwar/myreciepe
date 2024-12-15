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
import com.example.myrecipes.RecipeFavorite.RECIPE_FAV_ROUTE
import com.example.myrecipes.RecipeHome.RECIPE_HOME_ROUTE
import com.example.myrecipes.RecipeSearch.RECIPE_SEARCH_ROUTE
import com.example.myrecipes.details.RecipeDetailScreen
import com.example.myrecipes.favorite.RecipeFavoriteScreen
import com.example.myrecipes.home.HomeScreen
import com.example.myrecipes.search.RecipeSearchScreen

@Composable
fun RecipeNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = RECIPE_HOME_ROUTE,
    navActions: RecipeNavigationActions = remember(navController) {
        RecipeNavigationActions(navController)
    }
) {

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {

        composable(
            route = RECIPE_HOME_ROUTE
        ) {
            HomeScreen(
                onRecipeClick = { recipeId -> recipeId.recipeId?.let { it1 ->
                    navActions.navigateToRecipeDetail(
                        it1
                    )
                } },
                onSearchButtonClick = {
                    navActions.navigateToSearchScreen()
                },
                onBottomBarClick = {
                    navActions.navigateToFavoriteScreen()
                }
            )
        }

        composable(
            route = RecipeDestinations.RECIPE_DETAIL_ROUTE,
            arguments = listOf(
                navArgument(RECIPE_ID_ARG) {
                    type = NavType.IntType
                    defaultValue = 0
                }
            )
        ) { navBackStackEntry ->
            val recipeId = navBackStackEntry.arguments?.getInt(RECIPE_ID_ARG)
            recipeId?.let {
                RecipeDetailScreen(
                    recipeId = it,
                    onBack = {
                        navController.popBackStack()
                             },
                    )
            }
        }

        composable(
            route = RECIPE_SEARCH_ROUTE

        ) {

            RecipeSearchScreen(
                onRecipeClick = { recipeId ->
                        navActions.navigateToRecipeDetail(
                            recipeId
                        )
                },
                navController = navController
            )
        }

        composable(
            route = RECIPE_FAV_ROUTE
        ) {
            RecipeFavoriteScreen(
                onRecipeClick = { recipeId ->
                    recipeId.recipeId?.let { it1 ->
                        navActions.navigateToRecipeDetail(
                            it1
                        )
                    }
                }
            )
        }
    }
}