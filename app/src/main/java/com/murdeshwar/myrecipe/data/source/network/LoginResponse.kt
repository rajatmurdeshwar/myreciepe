package com.murdeshwar.myrecipe.data.source.network

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("token")
    val token: String
)