package com.example.myrecipes.data.source.network

import com.example.myrecipes.data.source.Recipe
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface NewRecipeApiService {

    @POST("api/")
    suspend fun addRecipe(@Body recipe: Recipe): Response<Void>
}