package com.prueba.pokemon

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DetallesActivity : AppCompatActivity() {

    lateinit var pokemon: Pokemon

    private val imageURL = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalles)

        val back = findViewById<ImageView>(R.id.btnBack)

        val bundle = intent.extras

        pokemon = bundle!!.getSerializable("pokemon") as Pokemon

        callRetrofitGetPokemonDetails()

        back.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                finish()
            }

        })
    }

    private fun setUpViews() {

        val nombre = findViewById<TextView>(R.id.tvNombre)
        val imagen = findViewById<CircleImageView>(R.id.ivHeroe)
        val weight = findViewById<TextView>(R.id.tvWeight)
        val height = findViewById<TextView>(R.id.tvHeight)

        Picasso.get().load(imageURL + pokemon.id + ".png").into(imagen)
        nombre.text = pokemon.name.capitalize() + " #" + pokemon.id
        weight.text = pokemon.weight
        height.text = pokemon.height
    }

    private fun callRetrofitGetPokemonDetails() {

        val okHttpClient = UnsafeOkHttpClient.unsafeOkHttpClient;
        val retrofit = Retrofit.Builder()
            .baseUrl(this.getString(R.string.base_url))
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(GetDataService::class.java)
        val call = service.getPokemonDetail(pokemon.id)
        call.enqueue(object : Callback<Pokemon> {
            override fun onResponse(call: Call<Pokemon>, response: Response<Pokemon>) {
                if (response.code() == 200) {
                    pokemon = response.body()!!

                    setUpViews()
                }
            }

            override fun onFailure(call: Call<Pokemon>, t: Throwable) {
                Toast.makeText(applicationContext, "Algo salio mal", Toast.LENGTH_LONG).show()
            }
        })
    }
}