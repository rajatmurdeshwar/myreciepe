package com.example.myrecipes.data

import com.example.myrecipes.data.source.Recipe
import com.example.myrecipes.data.source.local.LocalRecipe
import com.example.myrecipes.data.source.network.Recipes
import org.jsoup.Jsoup


// External to local
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
    id = id!!,
    title = title!!,
    description = extractData(summary!!),
    category = dishTypes[0],
    instructions = extractData(instructions!!),
    itemImage = image!!,
    healthScore = healthScore!!,
    readyIn = readyInMinutes!!,
    servings = servings!!
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