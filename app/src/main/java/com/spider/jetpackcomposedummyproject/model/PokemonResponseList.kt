package com.spider.jetpackcomposedummyproject.model

class PokemonResponseList(
    val count: Int,
    val next: String,
    val previous: Any,
    val results: List<Pokemon>
) {

}