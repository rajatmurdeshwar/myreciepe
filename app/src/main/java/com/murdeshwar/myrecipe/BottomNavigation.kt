package com.murdeshwar.myrecipe

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.murdeshwar.myrecipe.ui.details.RecipeDetailScreen
import com.murdeshwar.myrecipe.ui.favorite.RecipeFavoriteScreen
import com.murdeshwar.myrecipe.ui.home.HomeScreen
import com.murdeshwar.myrecipe.ui.profile.RecipeProfileScreen
import com.murdeshwar.myrecipe.ui.search.RecipeSearchScreen
import com.murdeshwar.myrecipe.util.BottomNavigationItem

@Composable
fun BottomNavigation() {
    val bottomNavController = rememberNavController()
    val navBackStackEntry by bottomNavController.currentBackStackEntryAsState()
    val navigationItems = BottomNavigationItem().bottomNavigationItems()
    val currentDestination = navBackStackEntry?.destination?.route


    var isBottomBarVisible by remember { mutableStateOf(true) }

    Scaffold(
        bottomBar = {
            if (isBottomBarVisible) {
                NavigationBar(
                    modifier = Modifier
                        .fillMaxWidth(),
                    tonalElevation = 10.dp,
                    containerColor = MaterialTheme.colorScheme.surface,
                    windowInsets = WindowInsets(0.dp)
                ) {
                    navigationItems.forEach { navigationItem ->
                        NavigationBarItem(
                            selected = navigationItem.route == currentDestination,
                            label = {
                                Text(
                                    navigationItem.label,
                                    style = MaterialTheme.typography.labelSmall
                                )
                            },
                            icon = {
                                Icon(
                                    navigationItem.icon,
                                    contentDescription = navigationItem.label
                                )
                            },
                            onClick = {
                                bottomNavController.navigate(navigationItem.route) {
                                    popUpTo(bottomNavController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                indicatorColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        )
                    }
                }
            }
        }
    ) {
        val bottomPadding = it.calculateBottomPadding()
        val navActions: RecipeNavigationActions = remember(bottomNavController) {
        RecipeNavigationActions(bottomNavController)
    }
        NavHost(
            navController = bottomNavController,
            startDestination = RecipeFavorite.RECIPE_FAV_ROUTE,
            modifier = Modifier.padding(bottom = bottomPadding)
        ) {

            composable(
                route = RecipeHome.RECIPE_HOME_ROUTE
            ) {
                isBottomBarVisible = true
                HomeScreen(
                    onRecipeClick = { recipe ->
                        recipe.recipeId?.let { recipeId ->
                            navActions.navigateToRecipeDetail(recipeId)
                        }
                    },
                    onSearchButtonClick = {
                        navActions.navigateToSearchScreen()
                    }
                )
            }


            composable(
                route = RecipeSearch.RECIPE_SEARCH_ROUTE

            ) {
                isBottomBarVisible = false
                RecipeSearchScreen(
                    onRecipeClick = { recipeId ->
                    navActions.navigateToRecipeDetail(
                        recipeId
                    )
                })
            }
            composable(
                route = RecipeProfile.RECIPE_PROFILE_ROUTE
            ) {
                isBottomBarVisible = true
                RecipeProfileScreen(
                )
            }

            composable(
                route = RecipeFavorite.RECIPE_FAV_ROUTE
            ) {
                isBottomBarVisible = true
                RecipeFavoriteScreen(
                    onRecipeClick = { recipe ->
                        recipe.recipeId?.let { recipeId ->
                            navActions.navigateToRecipeDetail(
                                recipeId
                            )
                        }
                    }
                )
            }

            composable(
                route = RecipeDestinations.RECIPE_DETAIL_ROUTE,
                arguments = listOf(
                    navArgument(RecipeDestinationsArgs.RECIPE_ID_ARG) {
                        type = NavType.IntType
                        defaultValue = 0
                    }
                )
            ) { navBackStackEntry ->
                val recipeId = navBackStackEntry.arguments?.getInt(RecipeDestinationsArgs.RECIPE_ID_ARG)
                recipeId?.let {
                    RecipeDetailScreen(
                        recipeId = it,
                        onBack = {
                            bottomNavController.popBackStack()
                        },
                    )
                }
            }
        }
    }
}