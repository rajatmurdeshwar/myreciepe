package com.murdeshwar.myrecipe.data.source

data class Recipe(
    val recipeId: Int =0,
    val title: String = "",
    val description: String = "",
    val category: String = "",
    val instructions: String = "",
    val itemImage: String ="",
    val healthScore: Int =0,
    val readyIn: Int=0,
    val servings: Int=0,
    val vegan: Boolean?,
    val glutenFree: Boolean?,
    val dairyFree: Boolean?
)

data class Ingredient(
    val recipeId: Int = 0,
    val ingredientsId: Int,
    val originalName: String,
    val amount: Double,
    val unit: String
)

data class Step(
    val number: Int,
    val step: String
)

data class RecipeWithDetails(
    val recipe: Recipe,
    val ingredients: List<Ingredient>,
    val steps: List<Step>
)

data class User(
    val name: String,
    val phonenumber: String,
    val city: String,
    val email: String,
    val password: String
)

data class LoginUser(
    val email: String,
    val password: String
)