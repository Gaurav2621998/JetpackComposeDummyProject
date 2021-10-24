package com.spider.jetpackcomposedummyproject.model

import com.google.gson.annotations.SerializedName

data class Pokemon(
    @SerializedName("name") val pokemonName: String,
    @SerializedName("url") val imageUrl: String,
)