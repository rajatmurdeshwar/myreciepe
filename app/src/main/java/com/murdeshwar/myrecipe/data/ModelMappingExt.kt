package com.murdeshwar.myrecipe.data

import com.murdeshwar.myrecipe.data.source.Ingredient
import com.murdeshwar.myrecipe.data.source.Recipe
import com.murdeshwar.myrecipe.data.source.RecipeSearchData
import com.murdeshwar.myrecipe.data.source.RecipeWithDetails
import com.murdeshwar.myrecipe.data.source.Step
import com.murdeshwar.myrecipe.data.source.local.LocalIngredient
import com.murdeshwar.myrecipe.data.source.local.LocalRecipe
import com.murdeshwar.myrecipe.data.source.local.LocalRecipeWithDetails
import com.murdeshwar.myrecipe.data.source.local.LocalStep
import com.murdeshwar.myrecipe.data.source.network.RecipeSearch
import com.murdeshwar.myrecipe.data.source.network.Recipes
import org.jsoup.Jsoup


// Local View --> local Database
/**
 * Converts an external representation of a recipe to a local representation.
 */
fun RecipeWithDetails.toLocal(): LocalRecipeWithDetails {
    return LocalRecipeWithDetails(
        recipe = LocalRecipe(
            id = recipe.recipeId,
            title = recipe.title,
            description = recipe.description,
            category = recipe.category,
            instructions = recipe.instructions,
            itemImage = recipe.itemImage,
            healthScore = recipe.healthScore,
            readyIn = recipe.readyIn,
            servings = recipe.servings,
            vegan = recipe.vegan,
            glutenFree = recipe.glutenFree,
            dairyFree = recipe.dairyFree
        ),
        ingredients = ingredients.map { ingredient ->
            LocalIngredient(
                ingredientId = ingredient.ingredientsId, // or assign a unique ID if necessary
                recipeId = recipe.recipeId,
                originalName = ingredient.originalName,
                amount = ingredient.amount,
                unit = ingredient.unit
            )
        },
        steps = steps.map { step ->
            LocalStep(
                recipeId = recipe.recipeId,
                number = step.number,
                step = step.step
            )
        }
    )
}


fun List<RecipeWithDetails>.toLocal() = map(RecipeWithDetails::toLocal)

// Local Database --> Local View
fun LocalRecipeWithDetails.toExternal(): RecipeWithDetails {
    return RecipeWithDetails(
        recipe = Recipe(
            recipeId = recipe.id,
            title = recipe.title,
            description = recipe.description,
            category = recipe.category,
            instructions = recipe.instructions,
            itemImage = recipe.itemImage,
            healthScore = recipe.healthScore,
            readyIn = recipe.readyIn,
            servings = recipe.servings,
            vegan = recipe.vegan,
            glutenFree = recipe.glutenFree,
            dairyFree = recipe.dairyFree
        ),
        ingredients = ingredients.map { localIngredient ->
            Ingredient(
                ingredientsId = localIngredient.ingredientId,
                originalName = localIngredient.originalName,
                amount = localIngredient.amount,
                unit = localIngredient.unit
            )
        },
        steps = steps.map { localStep ->
            Step(
                number = localStep.number,
                step = localStep.step
            )
        }
    )
}


@JvmName("localDatabaseToExternal")
fun List<LocalRecipeWithDetails>.toExternal() = map(LocalRecipeWithDetails::toExternal)

// Network --> Local Database
fun Recipes.toLocal(): LocalRecipeWithDetails {
    return LocalRecipeWithDetails(
        recipe = LocalRecipe(
            id = id ?: 0,
            title = title ?: "",
            description = extractData(summary ?: ""),
            category = dishTypes.firstOrNull() ?: "",
            instructions = extractData(instructions ?: ""),
            itemImage = image ?: "",
            healthScore = healthScore ?: 0,
            readyIn = readyInMinutes ?: 0,
            servings = servings ?: 0,
            vegan = vegan,
            glutenFree = glutenFree,
            dairyFree = dairyFree

        ),
        ingredients = extendedIngredients?.map { ingredient ->
            LocalIngredient(
                recipeId = id ?: 0,  // Use recipe ID as a foreign key
                ingredientId = ingredient.id ?: 0,
                originalName = ingredient.originalName ?: "",
                amount = ingredient.amount ?: 0.0,
                unit = ingredient.unit ?: ""
            )
        } ?: emptyList(),
        steps = analyzedInstructions?.flatMap { instruction ->
            instruction.steps.map { step ->
                LocalStep(
                    recipeId = id ?: 0,  // Use recipe ID as a foreign key
                    number = step.number ?: 0,
                    step = step.step ?: ""
                )
            }
        } ?: emptyList()
    )
}


fun extractData(dat: String): String {
    return Jsoup.parse(dat).text()
}

@JvmName("networkToLocalDatabase")
fun List<Recipes>.toLocal() = map(Recipes::toLocal)

fun LocalRecipe.toUiExternal() = Recipe(
    recipeId = id,
    title = title,
    description = description,
    category = category,
    instructions = instructions,
    itemImage = itemImage,
    healthScore = healthScore,
    readyIn = readyIn,
    servings = servings,
    vegan = vegan,
    glutenFree = glutenFree,
    dairyFree = dairyFree
)

@JvmName("localToExternal")
fun List<LocalRecipe>.tUioExternal() = map(LocalRecipe::toUiExternal)

fun Recipes.toUiLocal() = Recipe(
    recipeId = id ?: 0,
    title = title ?: "",
    description = extractData(summary ?: ""),
    category = dishTypes.firstOrNull() ?: "",
    instructions = extractData(instructions ?: ""),
    itemImage = image ?: "",
    healthScore = healthScore ?: 0,
    readyIn = readyInMinutes ?: 0,
    servings = servings ?: 0,
    vegan = vegan,
    glutenFree = glutenFree,
    dairyFree = dairyFree
)

@JvmName("networkToLocal")
fun List<Recipes>.toUiLocal() = map(Recipes::toUiLocal)

// Adding RecipeSearch to RecipeSearchData conversion
fun RecipeSearch.toRecipeSearchData() = RecipeSearchData(
    id = id,
    title = title,
    image = image,
    imageType = imageType
)

fun List<RecipeSearch>.toRecipeSearchDataList() = map(RecipeSearch::toRecipeSearchData)
