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
import com.murdeshwar.myrecipe.ui.details.RecipeDetailScreen
import com.murdeshwar.myrecipe.ui.onboarding.OnBoardingScreen
import com.murdeshwar.myrecipe.ui.user.LoginScreen

@Composable
fun RecipeNavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String = RECIPE_SPLASH_ROUTE
) {
    val isUserLoggedIn = remember { mutableStateOf(false) }

    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {

        // Splash Screen
        composable(RECIPE_SPLASH_ROUTE) {
            SplashScreen {
                isUserLoggedIn.value = it
                if (isUserLoggedIn.value) {
                    navController.navigate(RECIPE_HOME_ROUTE) {
                        popUpTo(RECIPE_SPLASH_ROUTE) { inclusive = true }
                    }
                } else {
                    navController.navigate("recipe_onboard") {
                        popUpTo(RECIPE_SPLASH_ROUTE) { inclusive = true }
                    }
                }
            }
        }

        composable(
            route = "recipe_onboard"
        ) {
            OnBoardingScreen {
                navController.navigate(RECIPE_LOGIN_ROUTE) {
                    popUpTo(RECIPE_SPLASH_ROUTE) { inclusive = true }

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
            BottomNavigation()
        }

    }
}