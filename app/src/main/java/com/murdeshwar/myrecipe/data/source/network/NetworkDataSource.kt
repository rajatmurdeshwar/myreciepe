package com.murdeshwar.myrecipe.data.source.network


import com.murdeshwar.myrecipe.BuildConfig
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NetworkDataSource {

    @GET("recipes/random")
    suspend fun getRecipes(
        @Query("number") number: Int,
        @Query("apiKey") apiKey: String = BuildConfig.RECIPE_API_KEY
    ): NetworkRecipe

    @GET("recipes/random")
    suspend fun getRecipesWithTags(
        @Query("number") number: Int,
        @Query("includeTags") tags: String,
        @Query("apiKey") apiKey: String = BuildConfig.RECIPE_API_KEY

    ): NetworkRecipe

    @GET("recipes/complexSearch")
    suspend fun searchRecipe(
        @Query("query") query: String,
        @Query("apiKey") apiKey: String = BuildConfig.RECIPE_API_KEY
    ): RecipeSearchResult

    @GET("recipes/{id}/information")
    suspend fun getRecipeById(
        @Path("id") id: Int,
        @Query("apiKey") apiKey: String = BuildConfig.RECIPE_API_KEY
    ): Recipes

    @GET("recipes/{id}/similar")
    suspend fun getRecipeByRecommendation(
        @Path("id") id: Int,
        @Query("apiKey") apiKey: String = BuildConfig.RECIPE_API_KEY
    ): Recipes


}