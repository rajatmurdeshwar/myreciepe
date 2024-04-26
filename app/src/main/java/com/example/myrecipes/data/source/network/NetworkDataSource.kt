package com.example.myrecipes.data.source.network


import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NetworkDataSource {

    companion object {
        const val API_KEY = "70d62e58791441b2874bd5dc63393d10"
    }

    @GET("recipes/random")
    suspend fun getRecipes(
        @Query("number") number: Int,
        @Query("apiKey") apiKey: String = API_KEY
    ): NetworkRecipe

    @GET("recipes/complexSearch")
    suspend fun searchRecipe(
        @Query("query") query: String,
        @Query("apiKey") apiKey: String = API_KEY
    ): RecipeSearchResult

    @GET("recipes/{id}/information")
    suspend fun getRecipeById(
        @Path("id") id: Int,
        @Query("apiKey") apiKey: String = API_KEY
    ): Recipes


}