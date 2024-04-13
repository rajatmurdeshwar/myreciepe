package com.example.myrecipes.data.source.network

import com.google.gson.annotations.SerializedName


data class Steps (

  @SerializedName("number"      ) var number      : Int?                 = null,
  @SerializedName("step"        ) var step        : String?              = null,
  @SerializedName("ingredients" ) var ingredients : ArrayList<Equipment>    = arrayListOf(),
  @SerializedName("equipment"   ) var equipment   : ArrayList<Equipment> = arrayListOf(),
  @SerializedName("length"   ) var leng : RecipeLength

)