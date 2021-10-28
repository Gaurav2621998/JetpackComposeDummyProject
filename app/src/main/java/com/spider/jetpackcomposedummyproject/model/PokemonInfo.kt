package com.spider.jetpackcomposedummyproject.model

import com.google.gson.annotations.SerializedName

data class PokemonInfo (
    @SerializedName("base_experience") val base_experience : Int,
    @SerializedName("height") val height : Int,
    @SerializedName("id") val id : Int,
    @SerializedName("is_default") val is_default : Boolean,
    @SerializedName("location_area_encounters") val location_area_encounters : String,
    @SerializedName("name") val name : String,
    @SerializedName("order") val order : Int,
    @SerializedName("past_types") val past_types : List<String>,
    @SerializedName("sprites") val sprites : Sprites,
    @SerializedName("weight") val weight : Int
)

data class Sprites (
    @SerializedName("back_default") val back_default : String,
    @SerializedName("back_female") val back_female : String,
    @SerializedName("back_shiny") val back_shiny : String,
    @SerializedName("back_shiny_female") val back_shiny_female : String,
    @SerializedName("front_default") val front_default : String,
    @SerializedName("front_female") val front_female : String,
    @SerializedName("front_shiny") val front_shiny : String,
    @SerializedName("front_shiny_female") val front_shiny_female : String,
)