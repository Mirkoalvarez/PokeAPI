package com.example.pokeapi.model

import com.google.gson.annotations.SerializedName

data class Pokemon(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("height") val height: Int,
    @SerializedName("weight") val weight: Int,
    @SerializedName("sprites") val sprites: Sprites,
    @SerializedName("types") val types: List<PokemonType>,
    @SerializedName("stats") val stats: List<PokemonStat>
)

data class Sprites(
    @SerializedName("front_default") val frontDefault: String?
)

data class PokemonType(
    @SerializedName("slot") val slot: Int,
    @SerializedName("type") val type: Type
)

data class Type(
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String
)

data class PokemonStat(
    @SerializedName("base_stat") val baseStat: Int,
    @SerializedName("effort") val effort: Int,
    @SerializedName("stat") val stat: Stat
)

data class Stat(
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String
)

// no usé android.R , cambie serializedname