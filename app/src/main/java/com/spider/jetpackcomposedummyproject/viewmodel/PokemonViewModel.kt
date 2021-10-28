package com.spider.jetpackcomposedummyproject.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.runtime.CompositionContext
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import com.spider.jetpackcomposedummyproject.helper.ErrorTypes
import com.spider.jetpackcomposedummyproject.model.ErrorMessage
import com.spider.jetpackcomposedummyproject.model.Pokemon
import com.spider.jetpackcomposedummyproject.repository.PokemonRepository
import com.spider.jetpackcomposedummyproject.util.*
import com.spider.jetpackcomposedummyproject.util.Constants.PAGE_SIZE
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PokemonViewModel @Inject constructor(
    private val context: Context,
    private val repository: PokemonRepository
) : ViewModel() {

    private var curPage = 0

    var pokemonList = mutableStateOf<List<Pokemon>>(listOf())
    var loadError = mutableStateOf(ErrorMessage(ErrorTypes.UNKNOWN_ERROR))
    var isLoading = mutableStateOf(false)
    var endReached = mutableStateOf(false)


    private var cachedPokemonList = listOf<Pokemon>()
    private var isSearchStarting = true
    var isSearching = mutableStateOf(false)


    init {
        loadPokemonPaginated()
    }

    fun searchPokemonList(query: String){

        if(context.currentConnectivityState==ConnectionState.Unavailable){
            loadError.value = ErrorMessage(ErrorTypes.NETWORK_ERROR,"")
            return
        }

        val listToSearch = if(isSearchStarting){
            pokemonList.value
        }
        else{
            cachedPokemonList
        }

        viewModelScope.launch {
            if(query.isEmpty()){
                pokemonList.value = cachedPokemonList
                isSearching.value = false
                isSearchStarting = true
                return@launch
            }
            val results = listToSearch.filter {
                it.pokemonName.contains(query.trim(),ignoreCase = true)
            }
            if(isSearchStarting){
                cachedPokemonList = pokemonList.value
                isSearchStarting = false
            }
            pokemonList.value = results
            isSearching.value = true
        }
    }

    fun loadPokemonPaginated() {
        if(context.currentConnectivityState==ConnectionState.Unavailable){
            loadError.value = ErrorMessage(ErrorTypes.NETWORK_ERROR,"")
            return
        }
        // TODO - Change it to paging 3
        viewModelScope.launch {
            isLoading.value = true
            val result = repository.getPokemonList(PAGE_SIZE, curPage * PAGE_SIZE)
            when(result) {
                is Resource.Success -> {
                    endReached.value = curPage * PAGE_SIZE >= result.data!!.count
                    val pokemonEntries = result.data.results.mapIndexed { index, entry ->
                        val number = if(entry.imageUrl.endsWith("/")) {
                            entry.imageUrl.dropLast(1).takeLastWhile { it.isDigit() }
                        } else {
                            entry.imageUrl.takeLastWhile { it.isDigit() }
                        }
                        val url = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${number}.png"
                        Pokemon(entry.pokemonName,url)
                    }
                    curPage++

                    loadError.value = ErrorMessage(ErrorTypes.UNKNOWN_ERROR)
                    isLoading.value = false
                    pokemonList.value += pokemonEntries
                }
                is Resource.Error -> {
                    loadError.value = ErrorMessage(ErrorTypes.UNKNOWN_ERROR,result.message)
                    isLoading.value = false
                }
            }
        }
    }


    fun calcDominantColor(drawable: Drawable, onFinish: (Color) -> Unit) {
        val bmp = (drawable as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888, true)

        Palette.from(bmp).generate { palette ->
            palette?.dominantSwatch?.rgb?.let { colorValue ->
                onFinish(Color(colorValue))
            }
        }
    }
}