package com.murdeshwar.myrecipe.data

import com.murdeshwar.myrecipe.data.source.LoginUser
import com.murdeshwar.myrecipe.data.source.Recipe
import com.murdeshwar.myrecipe.data.source.User
import com.murdeshwar.myrecipe.data.source.UserData
import com.murdeshwar.myrecipe.data.source.network.LoginResponse
import com.murdeshwar.myrecipe.data.source.network.NewRecipeApiService
import com.murdeshwar.myrecipe.data.source.network.Recipes

import retrofit2.Response

class FakeLocalDataSource(): NewRecipeApiService {
    override suspend fun signup(userData: User): Response<LoginResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun login(login: LoginUser): Response<LoginResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun addRecipe(recipe: Recipe): Response<Void> {
        TODO("Not yet implemented")
    }

    override suspend fun getUserDetails(): Response<UserData> {
        TODO("Not yet implemented")
    }

    override suspend fun getRecipes(): Response<Recipes> {
        TODO("Not yet implemented")
    }
}