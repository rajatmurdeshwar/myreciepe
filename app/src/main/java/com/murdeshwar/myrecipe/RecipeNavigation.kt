package com.murdeshwar.myrecipe


import androidx.navigation.NavHostController
import com.murdeshwar.myrecipe.RecipeDestinationsArgs.RECIPE_ID_ARG
import com.murdeshwar.myrecipe.RecipeScreens.RECIPES_SCREEN
import com.murdeshwar.myrecipe.RecipeScreens.RECIPE_DETAIL_SCREEN
import com.murdeshwar.myrecipe.RecipeScreens.RECIPE_FAV_SCREEN
import com.murdeshwar.myrecipe.RecipeScreens.RECIPE_LOGIN
import com.murdeshwar.myrecipe.RecipeScreens.RECIPE_PROFILE_SCREEN
import com.murdeshwar.myrecipe.RecipeScreens.RECIPE_SEARCH_SCREEN
import com.murdeshwar.myrecipe.RecipeScreens.SPLASH_SCREEN


private object RecipeScreens {
    const val SPLASH_SCREEN = "splash_screen"
    const val RECIPE_LOGIN = "recipe_login"
    const val RECIPES_SCREEN = "recipe_home"
    const val RECIPE_DETAIL_SCREEN = "recipe_detail"
    const val RECIPE_SEARCH_SCREEN = "recipe_search"
    const val RECIPE_FAV_SCREEN = "recipe_favorite"
    const val RECIPE_PROFILE_SCREEN = "recipe_profile"
}

object RecipeDestinationsArgs {
    const val RECIPE_ID_ARG = "recipeId"
}

object RecipeDestinations {
    const val RECIPE_DETAIL_ROUTE = "$RECIPE_DETAIL_SCREEN/{$RECIPE_ID_ARG}"
}

object SplashScreen {
    const val RECIPE_SPLASH_ROUTE = "$SPLASH_SCREEN"
}

object RecipeLogin {
    const val RECIPE_LOGIN_ROUTE = "$RECIPE_LOGIN"
}
object RecipeHome {
    const val RECIPE_HOME_ROUTE = "$RECIPES_SCREEN"
}

object RecipeProfile {
    const val RECIPE_PROFILE_ROUTE = "$RECIPE_PROFILE_SCREEN"
}

object RecipeSearch {
    const val RECIPE_SEARCH_ROUTE = "$RECIPE_SEARCH_SCREEN"
}

object RecipeFavorite {
    const val RECIPE_FAV_ROUTE = "$RECIPE_FAV_SCREEN"
}

class RecipeNavigationActions(private val navController: NavHostController) {

    fun navigateToHome() {
        navController.navigate(RECIPES_SCREEN)
    }

    fun navigateToRecipeDetail(recipeId: Int) {
        navController.navigate("$RECIPE_DETAIL_SCREEN/$recipeId")
    }

    fun navigateToFavoriteScreen() {
        navController.navigate(RECIPE_FAV_SCREEN)
    }

    fun navigateToSearchScreen() {
        navController.navigate(RECIPE_SEARCH_SCREEN)
    }
}
