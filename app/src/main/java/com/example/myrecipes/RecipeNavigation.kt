package com.example.myrecipes


import androidx.navigation.NavHostController
import com.example.myrecipes.RecipeDestinationsArgs.RECIPE_ID_ARG
import com.example.myrecipes.RecipeScreens.RECIPES_SCREEN
import com.example.myrecipes.RecipeScreens.RECIPE_DETAIL_SCREEN
import com.example.myrecipes.RecipeScreens.RECIPE_SEARCH_SCREEN


private object RecipeScreens {
    const val RECIPES_SCREEN = "recipes"
    const val RECIPE_DETAIL_SCREEN = "recipe"
    const val RECIPE_SEARCH_SCREEN = "search"
}

object RecipeDestinationsArgs {
    const val RECIPE_ID_ARG = "recipeId"
}

object RecipeDestinations {
    const val RECIPE_DETAIL_ROUTE = "$RECIPE_DETAIL_SCREEN/{$RECIPE_ID_ARG}"
}

object RecipeSearch {
    const val RECIPE_SEARCH_ROUTE = "$RECIPE_SEARCH_SCREEN"
}

class RecipeNavigationActions(private val navController: NavHostController) {

    fun navigateToHome() {
        navController.navigate(RECIPES_SCREEN)
    }

    fun navigateToRecipeDetail(recipeId: Int) {
        navController.navigate("$RECIPE_DETAIL_SCREEN/$recipeId")
    }

    fun navigateToSearchScreen() {
        navController.navigate(RECIPE_SEARCH_SCREEN)
    }
}
