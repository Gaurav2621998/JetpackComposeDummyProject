package com.spider.jetpackcomposedummyproject.repository

import com.spider.jetpackcomposedummyproject.api.RetrofitApis
import com.spider.jetpackcomposedummyproject.model.PokemonResponseList
import com.spider.jetpackcomposedummyproject.util.Resource
import dagger.hilt.android.scopes.ActivityScoped
import java.lang.Exception
import javax.inject.Inject

@ActivityScoped
class PokemonRepository @Inject constructor(
    private val api:RetrofitApis
)  {

    suspend fun getPokemonList(limit:Int,offset:Int):Resource<PokemonResponseList>{
        val response = try {
            api.getPokemonList(limit,offset)
        }catch (e:Exception){
            return Resource.Error("An unknown error occured.")
        }
        return Resource.Success(response)
    }
}