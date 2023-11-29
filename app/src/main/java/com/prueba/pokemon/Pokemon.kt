package com.prueba.pokemon

import java.io.Serializable

data class PokemonList(val results: ArrayList<Pokemon>)

data class Pokemon(
    var id: Int,
    val name: String,
    val height: String,
    val weight: String
) : Serializable