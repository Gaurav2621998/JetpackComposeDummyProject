package com.spider.jetpackcomposedummyproject.db

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.spider.jetpackcomposedummyproject.model.Pokemon

@Dao
interface PokemonDao {

    @Query("Select * from pokemon_collection")
    fun getPokemonList():PagingSource<Int, Pokemon>

    @Query("Select * from pokemon_collection")
    fun getAllPokemonList():LiveData<List<Pokemon>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list:List<Pokemon>)

    @Query("Delete from pokemon_collection")
    fun deleteAll()

    @Query( " Select * from pokemon_collection where pokemonName Like '%' ||:query || '%'")
    fun getSearchPokemon(query:String):List<Pokemon>
}