package com.example.pokeapi.network

import com.example.pokeapi.model.Pokemon
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface PokemonApiService {

    @GET("pokemon/{id}")
    fun getPokemon(@Path("id") pokemonId: String): Call<Pokemon>
}