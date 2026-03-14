package com.example.catlendar.network

import com.google.gson.annotations.SerializedName

data class CatFactDto(
    @SerializedName("type")
    val type: String?,
    @SerializedName("text")
    val text: String
)
