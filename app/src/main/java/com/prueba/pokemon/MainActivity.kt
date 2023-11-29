package com.prueba.pokemon

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(){

    lateinit var pokemon : ArrayList<Pokemon>
    private lateinit var scrollListener: RecyclerView.OnScrollListener


    lateinit var rvLista : RecyclerView
    lateinit var progressBar: ProgressBar

    lateinit var layoutMan: LinearLayoutManager
    lateinit var listAdapter: ListaAdapter

    var buscando: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvLista = findViewById(R.id.rvHeroes)
        progressBar = findViewById(R.id.progressBar)

        callRetrofitGetHeroes()
    }

    private fun callRetrofitGetHeroes() {

        val okHttpClient = UnsafeOkHttpClient.unsafeOkHttpClient;
        val retrofit = Retrofit.Builder()
            .baseUrl(this.getString(R.string.base_url))
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(GetDataService::class.java)
        val call = service.getPokemonList()
        call.enqueue(object : Callback<PokemonList> {
            override fun onResponse(call: Call<PokemonList>, response: Response<PokemonList>) {
                if (response.code() == 200) {
                    pokemon = response.body()!!.results

                    setUpRV()
                }
            }

            override fun onFailure(call: Call<PokemonList>, t: Throwable) {
                Toast.makeText(applicationContext, "Algo salio mal", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun callRetrofitGetMorePokemon() {
        val offset = pokemon.size + 1
        val okHttpClient = UnsafeOkHttpClient.unsafeOkHttpClient;
        val retrofit = Retrofit.Builder()
            .baseUrl(this.getString(R.string.base_url))
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(GetDataService::class.java)
        val call = service.loadMorePokemon(offset.toString())
        call.enqueue(object : Callback<PokemonList> {
            override fun onResponse(call: Call<PokemonList>, response: Response<PokemonList>) {
                if (response.code() == 200) {
                    pokemon.addAll( response.body()!!.results)
                    for(i in 0 until pokemon.size){
                        pokemon.get(i).id = i
                    }
                    setUpRV()
                }
            }

            override fun onFailure(call: Call<PokemonList>, t: Throwable) {
                Toast.makeText(applicationContext, "Algo salio mal", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun setUpRV() {

        val layoutList = LinearLayoutManager(this@MainActivity)
        listAdapter = ListaAdapter(pokemon, object : ListaAdapter.OnItemClickListener {
            override fun onItemClick(pokemon: Pokemon) {
                val intent = Intent(this@MainActivity, DetallesActivity::class.java)
                intent.putExtra("pokemon", pokemon)
                startActivity(intent)
            }
        })

        rvLista.apply {
            layoutManager = layoutList
            adapter = listAdapter
        }

        rvLista.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    Log.d("tag", "scrolled to end")
                    callRetrofitGetMorePokemon()
                }
            }
        })

        buscando = false
        progressBar.visibility = View.INVISIBLE
        rvLista.visibility = View.VISIBLE
    }
}