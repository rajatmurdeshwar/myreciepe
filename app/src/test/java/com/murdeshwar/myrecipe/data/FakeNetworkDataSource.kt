package com.murdeshwar.myrecipe.data

import com.murdeshwar.myrecipe.data.source.network.NetworkDataSource
import com.murdeshwar.myrecipe.data.source.network.NetworkRecipe
import com.murdeshwar.myrecipe.data.source.network.RecipeSearchResult
import com.murdeshwar.myrecipe.data.source.network.Recipes

class FakeNetworkDataSource(): NetworkDataSource {
    override suspend fun getRecipes(number: Int, apiKey: String): NetworkRecipe {
        TODO("Not yet implemented")
    }

    override suspend fun getRecipesWithTags(
        number: Int,
        tags: String,
        apiKey: String
    ): NetworkRecipe {
        TODO("Not yet implemented")
    }

    override suspend fun searchRecipe(query: String, apiKey: String): RecipeSearchResult {
        TODO("Not yet implemented")
    }

    override suspend fun getRecipeById(id: Int, apiKey: String): Recipes {
        TODO("Not yet implemented")
    }

    override suspend fun getRecipeByRecommendation(id: Int, apiKey: String): Recipes {
        TODO("Not yet implemented")
    }
}