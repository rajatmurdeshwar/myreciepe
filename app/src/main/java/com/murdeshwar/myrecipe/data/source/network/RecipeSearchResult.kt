package com.murdeshwar.myrecipe.data.source.network

import com.google.gson.annotations.SerializedName

data class RecipeSearchResult(
    @SerializedName("results" ) val results: List<RecipeSearch>,
    @SerializedName("offset" ) val offset: Int,
    @SerializedName("number" ) val number: Int,
    @SerializedName("totalResults" ) val totalResults: Int
)

data class RecipeSearch(
    @SerializedName("id" ) val id: Int,
    @SerializedName("title" ) val title: String,
    @SerializedName("image" ) val image: String,
    @SerializedName("imageType" ) val imageType: String
)