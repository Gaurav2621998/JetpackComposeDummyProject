package com.spider.jetpackcomposedummyproject.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.runtime.CompositionContext
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.palette.graphics.Palette
import com.spider.jetpackcomposedummyproject.db.LocalDatabase
import com.spider.jetpackcomposedummyproject.helper.ErrorTypes
import com.spider.jetpackcomposedummyproject.model.ErrorMessage
import com.spider.jetpackcomposedummyproject.model.Pokemon
import com.spider.jetpackcomposedummyproject.remote.PokemonPagingSource
import com.spider.jetpackcomposedummyproject.remote.PokemonRemoteMediator
import com.spider.jetpackcomposedummyproject.repository.PokemonRepository
import com.spider.jetpackcomposedummyproject.util.*
import com.spider.jetpackcomposedummyproject.util.Constants.PAGE_SIZE
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PokemonViewModel @Inject constructor(
    private val context: Context,
    private val database: LocalDatabase,
    private val repository: PokemonRepository
) : ViewModel() {

    private var curPage = 0

    var pokemonList:LiveData<List<Pokemon>>
    var searchPokemonList = mutableStateOf<List<Pokemon>>(listOf())
    var loadError = mutableStateOf(ErrorMessage(ErrorTypes.UNKNOWN_ERROR))
    var isLoading = mutableStateOf(false)
    var endReached = mutableStateOf(false)

    var searchQuery = ""


    private var isSearchStarting = true
    var isSearching = mutableStateOf(false)

    init {
        pokemonList = database.pokemonDao().getAllPokemonList()
        loadPokemonPaginated()
    }


    // TODO - Need Improvement
    fun searchPokemonList(query: String){
        searchQuery = query
        if(context.currentConnectivityState==ConnectionState.Unavailable){
            loadError.value = ErrorMessage(ErrorTypes.NETWORK_ERROR,"")
            return
        }
        viewModelScope.launch {
            if(query.isEmpty()){
                isSearching.value = false
                isSearchStarting = true
                return@launch
            }
            searchPokemonList.value = database.pokemonDao().getSearchPokemon(query)
            if(isSearchStarting){
                isSearchStarting = false
            }
            isSearching.value = true
        }
    }

    fun loadPokemonPaginated() {
        if(context.currentConnectivityState==ConnectionState.Unavailable){
            loadError.value = ErrorMessage(ErrorTypes.NETWORK_ERROR,"")
            return
        }
        if(curPage==0){
            database.pokemonDao().deleteAll()
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
                    database.pokemonDao().insertAll(pokemonEntries)
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

//    @ExperimentalPagingApi
//    val pager = Pager (
//        config = PagingConfig(20),
//        remoteMediator = PokemonRemoteMediator(
//            database,
//            repository
//        )
//    ){
//        database.pokemonDao().getPokemonList()
//    }.flow
//
//    val pokemonPagingList: Flow<PagingData<Pokemon>> = Pager(PagingConfig(pageSize = 5)) {
//        PokemonPagingSource(repository)
//    }.flow
}