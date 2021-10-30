package com.spider.jetpackcomposedummyproject.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "pokemon_collection")
data class Pokemon(
    @SerializedName("name") val pokemonName: String,
    @SerializedName("url") val imageUrl: String,
){
    @PrimaryKey(autoGenerate = true)
    var id:Long=0L
}