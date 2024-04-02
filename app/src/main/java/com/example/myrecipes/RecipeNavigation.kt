package com.example.myrecipes


import androidx.navigation.NavHostController
import com.example.myrecipes.RecipeDestinationsArgs.RECIPE_ID_ARG
import com.example.myrecipes.RecipeScreens.RECIPES_SCREEN
import com.example.myrecipes.RecipeScreens.RECIPE_DETAIL_SCREEN


private object RecipeScreens {
    const val RECIPES_SCREEN = "recipes"
    const val RECIPE_DETAIL_SCREEN = "recipe"
}

object RecipeDestinationsArgs {
    const val RECIPE_ID_ARG = "recipeId"
}

object RecipeDestinations {
    const val RECIPE_DETAIL_ROUTE = "$RECIPE_DETAIL_SCREEN/{$RECIPE_ID_ARG}"
}

class RecipeNavigationActions(private val navController: NavHostController) {

    fun navigateToHome() {
        navController.navigate(RECIPES_SCREEN)
    }

    fun navigateToRecipeDetail(recipeId: Int) {
        navController.navigate("$RECIPE_DETAIL_SCREEN/$recipeId")
    }
}
