package com.example.myrecipes


import androidx.navigation.NavHostController
import com.example.myrecipes.RecipeDestinationsArgs.RECIPE_ID_ARG
import com.example.myrecipes.RecipeScreens.RECIPES_SCREEN
import com.example.myrecipes.RecipeScreens.RECIPE_DETAIL_SCREEN
import com.example.myrecipes.RecipeScreens.RECIPE_FAV_SCREEN
import com.example.myrecipes.RecipeScreens.RECIPE_SEARCH_SCREEN


private object RecipeScreens {
    const val RECIPES_SCREEN = "recipe_home"
    const val RECIPE_DETAIL_SCREEN = "recipe_detail"
    const val RECIPE_SEARCH_SCREEN = "recipe_search"
    const val RECIPE_FAV_SCREEN = "recipe_favorite"
}

object RecipeDestinationsArgs {
    const val RECIPE_ID_ARG = "recipeId"
}

object RecipeDestinations {
    const val RECIPE_DETAIL_ROUTE = "$RECIPE_DETAIL_SCREEN/{$RECIPE_ID_ARG}"
}

object RecipeHome {
    const val RECIPE_HOME_ROUTE = "$RECIPES_SCREEN"
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
