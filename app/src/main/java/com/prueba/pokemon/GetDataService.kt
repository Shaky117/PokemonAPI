package com.prueba.pokemon

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GetDataService {
    @GET("pokemon")
    fun getPokemonList() : Call<PokemonList>

    @GET("pokemon/{id}")
    fun getPokemonDetail(@Path("id") id : Int) : Call<Pokemon>

    @GET("pokemon/")
    fun loadMorePokemon(@Query("offset") nextPage: String) : Call<PokemonList>
}