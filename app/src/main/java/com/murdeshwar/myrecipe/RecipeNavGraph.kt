package com.murdeshwar.myrecipe

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.murdeshwar.myrecipe.RecipeHome.RECIPE_HOME_ROUTE
import com.murdeshwar.myrecipe.RecipeLogin.RECIPE_LOGIN_ROUTE
import com.murdeshwar.myrecipe.SplashScreen.RECIPE_SPLASH_ROUTE
import com.murdeshwar.myrecipe.ui.onboarding.OnBoardingScreen
import com.murdeshwar.myrecipe.ui.user.LoginScreen
import com.murdeshwar.myrecipe.util.NetworkMonitor
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@Composable
fun RecipeNavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String = RECIPE_SPLASH_ROUTE,
    networkMonitor: NetworkMonitor
) {
    val isUserLoggedIn = remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val isOffline = remember(networkMonitor) {
        networkMonitor.isOnline
            .map(Boolean::not)
            .stateIn(
                scope = coroutineScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = false,
            )
    }

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
                },
                isOfflineState = isOffline)
        }

        composable(
            route = RECIPE_HOME_ROUTE
        ) {
            BottomNavigation(isOfflineState = isOffline)
        }

    }
}