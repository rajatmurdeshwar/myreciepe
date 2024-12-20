package com.murdeshwar.myrecipe

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.murdeshwar.myrecipe.RecipeDestinationsArgs.RECIPE_ID_ARG
import com.murdeshwar.myrecipe.RecipeHome.RECIPE_HOME_ROUTE
import com.murdeshwar.myrecipe.RecipeLogin.RECIPE_LOGIN_ROUTE
import com.murdeshwar.myrecipe.SplashScreen.RECIPE_SPLASH_ROUTE
import com.murdeshwar.myrecipe.details.RecipeDetailScreen
import com.murdeshwar.myrecipe.user.LoginScreen

@Composable
fun RecipeNavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String = RECIPE_SPLASH_ROUTE,
    navActions: RecipeNavigationActions = remember(navController) {
        RecipeNavigationActions(navController)
    }
) {
    val isUserLoggedIn = remember { mutableStateOf(false) }

    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {

        // Splash Screen
        composable(RECIPE_SPLASH_ROUTE) {
            SplashScreen {
                if (isUserLoggedIn.value) {
                    navController.navigate(RECIPE_HOME_ROUTE) {
                        popUpTo(RECIPE_SPLASH_ROUTE) { inclusive = true }
                    }
                } else {
                    navController.navigate(RECIPE_LOGIN_ROUTE) {
                        popUpTo(RECIPE_SPLASH_ROUTE) { inclusive = true }
                    }
                }
            }
        }

        composable(
            route = RECIPE_LOGIN_ROUTE
        ) {
            LoginScreen(onLoginSuccess = {
                isUserLoggedIn.value = true
                navController.navigate(RECIPE_HOME_ROUTE) {
                    popUpTo(RECIPE_LOGIN_ROUTE) { inclusive = true }
                }
            },
                onSignUpSuccess = {
                    isUserLoggedIn.value = true
                    navController.navigate(RECIPE_HOME_ROUTE) {
                        popUpTo(RECIPE_LOGIN_ROUTE) { inclusive = true }
                    }
                })
        }

        composable(
            route = RECIPE_HOME_ROUTE
        ) {
            BottomNavigation(navController)
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
    }
}