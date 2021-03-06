package com.spider.jetpackcomposedummyproject.api

import com.spider.jetpackcomposedummyproject.model.PokemonInfo
import com.spider.jetpackcomposedummyproject.model.PokemonResponseList
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RetrofitApis {

    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("limit") limit:Int,
        @Query("offset") offSet:Int
    ):PokemonResponseList

    @GET("pokemon/{name}")
    suspend fun getPokemonInfo(
        @Path("name") name: String
    ): PokemonInfo
}