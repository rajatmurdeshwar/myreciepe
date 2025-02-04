package com.murdeshwar.myrecipe.data.source.network

import com.murdeshwar.myrecipe.data.source.LoginUser
import com.murdeshwar.myrecipe.data.source.Recipe
import com.murdeshwar.myrecipe.data.source.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface NewRecipeApiService {

    @POST("auth/signup")
    suspend fun signup(@Body userData: User): Response<Void>

    @POST("auth/login")
    suspend fun login(@Body login: LoginUser) : Response<LoginResponse>

    @POST("api/addRecipe")
    suspend fun addRecipe(@Body recipe: Recipe): Response<Void>

    @POST("auth/userDetails")
    suspend fun getUserDetails(): Response<User>

    @GET("api/recipes")
    suspend fun getRecipes(): Response<Recipes>
}