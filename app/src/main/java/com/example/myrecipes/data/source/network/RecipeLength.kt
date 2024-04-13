package com.example.myrecipes.data.source.network

import com.google.gson.annotations.SerializedName

data class RecipeLength(@SerializedName("number")val number: Int,
                        @SerializedName("unit")val unit: String) {
}