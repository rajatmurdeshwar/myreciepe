package com.example.myrecipes.data

import com.example.myrecipes.data.source.Recipe
import com.example.myrecipes.data.source.RecipeSearchData
import com.example.myrecipes.data.source.local.LocalRecipe
import com.example.myrecipes.data.source.network.RecipeSearch
import com.example.myrecipes.data.source.network.Recipes
import org.jsoup.Jsoup


// External to local
/**
 * Converts an external representation of a recipe to a local representation.
 */
fun Recipe.toLocal() = LocalRecipe(
    id = id,
    title = title,
    description = description,
    category = category,
    instructions = instructions,
    itemImage = itemImage,
    healthScore = healthScore,
    readyIn = readyIn,
    servings = servings

)

fun List<Recipe>.toLocal() = map(Recipe::toLocal)

// Local to External
fun LocalRecipe.toExternal() = Recipe(
    id = id,
    title = title,
    description = description,
    category = category,
    instructions = instructions,
    itemImage = itemImage,
    healthScore = healthScore,
    readyIn = readyIn,
    servings = servings
)

@JvmName("localToExternal")
fun List<LocalRecipe>.toExternal() = map(LocalRecipe::toExternal)

// Network to Local
fun Recipes.toLocal() = LocalRecipe(
    id = id ?: 0,
    title = title ?: "",
    description = extractData(summary ?: ""),
    category = dishTypes.firstOrNull() ?: "",
    instructions = extractData(instructions ?: ""),
    itemImage = image ?: "",
    healthScore = healthScore ?: 0,
    readyIn = readyInMinutes ?: 0,
    servings = servings ?: 0
)

fun extractData(dat: String): String {
    return Jsoup.parse(dat).text()
}

@JvmName("networkToLocal")
fun List<Recipes>.toLocal() = map(Recipes::toLocal)



// Network to External
fun Recipes.toExternal() = toLocal().toExternal()

@JvmName("networkToExternal")
fun List<Recipes>.toExternal() = map(Recipes::toExternal)

// Adding RecipeSearch to RecipeSearchData conversion
fun RecipeSearch.toRecipeSearchData() = RecipeSearchData(
    id = id,
    title = title,
    image = image,
    imageType = imageType
)

fun List<RecipeSearch>.toRecipeSearchDataList() = map(RecipeSearch::toRecipeSearchData)
