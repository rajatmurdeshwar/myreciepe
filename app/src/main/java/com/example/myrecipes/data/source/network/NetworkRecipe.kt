package com.example.myrecipes.data.source.network

import com.google.gson.annotations.SerializedName


data class NetworkRecipe (

  @SerializedName("recipes" ) var recipes : ArrayList<Recipes> = arrayListOf()

)