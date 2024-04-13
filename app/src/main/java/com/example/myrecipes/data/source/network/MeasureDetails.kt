package com.example.myrecipes.data.source.network

import com.google.gson.annotations.SerializedName


data class MeasureDetails (

  @SerializedName("amount"    ) var amount    : Double?    = null,
  @SerializedName("unitShort" ) var unitShort : String? = null,
  @SerializedName("unitLong"  ) var unitLong  : String? = null

)