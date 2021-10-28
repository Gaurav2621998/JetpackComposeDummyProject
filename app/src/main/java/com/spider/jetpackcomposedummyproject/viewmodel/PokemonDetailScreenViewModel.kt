package com.spider.jetpackcomposedummyproject.viewmodel

import androidx.lifecycle.ViewModel
import com.spider.jetpackcomposedummyproject.model.PokemonInfo
import com.spider.jetpackcomposedummyproject.repository.PokemonRepository
import com.spider.jetpackcomposedummyproject.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PokemonDetailScreenViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel(){

    suspend fun getPokemonInfo(pokemonName:String) : Resource<PokemonInfo>{
        return repository.getPokemonInfo(pokemonName)
    }
}