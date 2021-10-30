package com.spider.jetpackcomposedummyproject.remote

import androidx.paging.PagingSource
import com.spider.jetpackcomposedummyproject.model.Pokemon
import com.spider.jetpackcomposedummyproject.repository.PokemonRepository
import com.spider.jetpackcomposedummyproject.util.Constants.PAGE_SIZE

class PokemonPagingSource(
    private val repository: PokemonRepository
) : PagingSource<Int, Pokemon>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Pokemon> {
        return try {
            val nextPage = params.key ?: 0
            val movieListResponse = repository.getPokemonList(PAGE_SIZE,nextPage* PAGE_SIZE)

            val pokemonEntries = movieListResponse.data?.results?.mapIndexed { index, entry ->
                val number = if(entry.imageUrl.endsWith("/")) {
                    entry.imageUrl.dropLast(1).takeLastWhile { it.isDigit() }
                } else {
                    entry.imageUrl.takeLastWhile { it.isDigit() }
                }
                val url = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${number}.png"
                Pokemon(entry.pokemonName,url)
            }

            LoadResult.Page(
                data = pokemonEntries!!,
                prevKey = if (nextPage == 0) null else nextPage - 1,
                nextKey = nextPage+1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}