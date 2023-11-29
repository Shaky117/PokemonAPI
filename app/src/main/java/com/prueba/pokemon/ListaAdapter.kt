package com.prueba.pokemon

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class ListaAdapter (val pokemonList: List<Pokemon>, val listener: OnItemClickListener): RecyclerView.Adapter<ListaViewHolder>() {


    interface OnItemClickListener {
        fun onItemClick(pokemon: Pokemon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pokemon, parent, false)
        return ListaViewHolder(view)
    }

    override fun getItemCount(): Int {
        return pokemonList.size
    }

    override fun onBindViewHolder(holder: ListaViewHolder, position: Int) {
        var pokemon = pokemonList[position]
        pokemon.id = position + 1
        return holder.bind(pokemonList[position], listener)
    }
}

class ListaViewHolder(itemView : View): RecyclerView.ViewHolder(itemView) {
    private val foto: CircleImageView = itemView.findViewById(R.id.ivHeroe)
    private val nombre: TextView = itemView.findViewById(R.id.tvNombre)
    val imageURL = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/"
    fun bind(pokemon: Pokemon, listener: ListaAdapter.OnItemClickListener) {
        Picasso.get().load(imageURL + pokemon.id + ".png").into(foto)
        nombre.text = pokemon.name.capitalize()

        itemView.setOnClickListener { listener.onItemClick(pokemon) }
    }
}

