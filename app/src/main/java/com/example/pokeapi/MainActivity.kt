package com.example.pokeapi

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.util.Log
import com.example.pokeapi.databinding.ActivityMainBinding
import com.example.pokeapi.model.Pokemon
import com.example.pokeapi.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.squareup.picasso.Picasso



class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setUpClickListener()
    }


    fun setUpClickListener(){
        binding.btnSearch.setOnClickListener{
            val pokemonInput = binding.etPokemonInput.text.toString().trim()
            if(pokemonInput.isNotEmpty()){
                searchPokemon(pokemonId= pokemonInput.lowercase())
            }else {
                Toast.makeText(this,"Por favor ingrese un ID o un Nombre. ", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun searchPokemon(pokemonId: String){
        // Mostrar Estado de carga
        showLoading(true)

        //Llamada a la Api
        RetrofitClient.pokemonApiService.getPokemon(pokemonId).enqueue(object : Callback<Pokemon> {
            override fun onResponse(
                call: Call<Pokemon>,
                response: Response<Pokemon>
            ) {
                Log.i("respuesta api: ","${response.body()}")
                if (response.isSuccessful){
                    response.body()?.let { pokemon: Pokemon ->
                        displayPokemon(pokemon)
                    } ?:run {
                        showError("No se encontro ningun pokemon")
                    }
                }else{
                    when(response.code()){
                        404 -> showError("Pokemon no encontrado")
                        500 -> showError("Error em el servidor")
                        else -> showError("Error : ${response.code()}")
                    }
                }

                showLoading(showLoading = false)
            }

            override fun onFailure(
                call: Call<Pokemon>,
                t: Throwable
            ) {
                Log.e("Pokemon", t.message.toString())
                showError("Error al intentar obtener la info: ${t.localizedMessage ?: "desconocido"}")
                showLoading(false)
            }

        })
    }

    private fun displayPokemon(pokemon: Pokemon){

        binding.apply {

            cardPokemon.visibility = View.VISIBLE
            tvError.visibility = View.GONE

            tvPokemonName.text = pokemon.name.replaceFirstChar {
                if(it.isLowerCase()) it.titlecase() else it.toString()
            }
            tvPokemonId.text = "ID: #${pokemon.id}"
            tvPokemonHeight.text = "ID: #${pokemon.height} dm"
            tvPokemonWeight.text = "ID: #${pokemon.weight} hg"

            val types = pokemon.types.joinToString(
                ", "
            ){it.type.name.replaceFirstChar {
                char ->
                if (char.isLowerCase()) char.titlecase() else char.toString()
            }
            }

            tvPokemonTypes.text = types

            val stats = pokemon.stats.joinToString( "\n" ) { stat ->
                val statName = stat.stat.name.replace("-", " " ).replaceFirstChar { char ->
                    if (char.isLowerCase()) char.titlecase() else char.toString()
                }
                "$statName: ${stat.baseStat}"
            }

            tvPokemonStats.text = stats

            pokemon.sprites.frontDefault?.let { imageUrl ->
                Picasso.get()
                    .load(imageUrl) // <-- sin "path ="
                    .error(R.drawable.ic_launcher_foreground) // <-- sin "errorResId ="
                    .into(binding.ivPokemon) // <-- usar binding
            } ?: run {
                binding.ivPokemon.setImageDrawable(null)
            }
        }
    }


    private fun showError(message: String){
        binding.apply {
            tvError.text = message
            tvError.visibility = View.VISIBLE
            cardPokemon.visibility = View.GONE
        }
        Toast.makeText(this,message,Toast.LENGTH_LONG,).show()
    }


    fun showLoading(showLoading: Boolean){
        binding.progressBar.visibility = if (showLoading) View.VISIBLE else View.GONE
        binding.cardPokemon.visibility = if (showLoading) View.GONE else binding.cardPokemon.visibility
        binding.tvError.visibility = if (showLoading) View.GONE else binding.tvError.visibility
    }
}