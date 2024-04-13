package com.example.myrecipes.data.source.network


import retrofit2.http.GET

interface NetworkDataSource {

    @GET("recipes/random?number=5&apiKey=70d62e58791441b2874bd5dc63393d10")
    suspend fun getRecipes(): NetworkRecipe
}