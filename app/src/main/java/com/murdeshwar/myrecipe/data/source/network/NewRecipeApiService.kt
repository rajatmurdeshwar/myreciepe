package com.murdeshwar.myrecipe.data.source.network

import com.murdeshwar.myrecipe.data.source.LoginUser
import com.murdeshwar.myrecipe.data.source.Recipe
import com.murdeshwar.myrecipe.data.source.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface NewRecipeApiService {

    @POST("api/signup")
    suspend fun signup(@Body userData: User): Response<Void>

    @POST("api/login")
    suspend fun login(@Body login: LoginUser) : Response<Void>

    @POST("api/")
    suspend fun addRecipe(@Body recipe: Recipe): Response<Void>

    @GET("api/recipes")
    suspend fun getRecipes(): Response<Recipes>
}