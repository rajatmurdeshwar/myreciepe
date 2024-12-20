package com.murdeshwar.myrecipe

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.murdeshwar.myrecipe.favorite.RecipeFavoriteScreen
import com.murdeshwar.myrecipe.home.HomeScreen
import com.murdeshwar.myrecipe.profile.RecipeProfileScreen
import com.murdeshwar.myrecipe.search.SearchBarScreen
import com.murdeshwar.myrecipe.util.BottomNavigationItem

@Composable
fun BottomNavigation(navController: NavController) {
    val bottomNavController = rememberNavController()
    val navBackStackEntry by bottomNavController.currentBackStackEntryAsState()
    val navigationItems = BottomNavigationItem().bottomNavigationItems()
    val currentDestination = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            NavigationBar {
                navigationItems.forEach { navigationItem ->
                    NavigationBarItem(
                        selected = navigationItem.route == currentDestination,
                        label = {
                            Text(navigationItem.label)
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
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        val navActions: RecipeNavigationActions = remember(bottomNavController) {
        RecipeNavigationActions(bottomNavController)
    }
        NavHost(
            navController = bottomNavController,
            startDestination = RecipeFavorite.RECIPE_FAV_ROUTE,
            modifier = Modifier.padding(paddingValues)
        ) {

            composable(
                route = RecipeHome.RECIPE_HOME_ROUTE
            ) {
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

                SearchBarScreen(
                )
            }
            composable(
                route = RecipeProfile.RECIPE_PROFILE_ROUTE
            ) {
                RecipeProfileScreen(
                    navController = navController
                )
            }

            composable(
                route = RecipeFavorite.RECIPE_FAV_ROUTE
            ) {
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
        }
    }
}