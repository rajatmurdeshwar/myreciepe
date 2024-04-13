package com.example.myrecipes.data.source.network

import com.google.gson.annotations.SerializedName


data class Measures (

  @SerializedName("us"     ) var us     : MeasureDetails?     = MeasureDetails(),
  @SerializedName("metric" ) var measureDetails : MeasureDetails? = MeasureDetails()

)