package com.example.dio1.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dio1.Dominio.Carro
import com.example.dio1.R

class CarAdapter(private val carros: List<Carro>) : RecyclerView.Adapter<CarAdapter.ViewHolder>() {

    var carItemListener : (Carro) -> Unit = {}
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.carro_item, parent, false);
        return ViewHolder(view);
    }

    override fun getItemCount(): Int = carros.size;
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.preco.text = carros[position].preco;
        holder.bateria.text = carros[position].bateria;
        holder.potencia.text = carros[position].potencia;
        holder.favorite.setOnClickListener {
            val carro = carros[position];
            carItemListener(carro);
            setupFavorite(carro, holder)

        }
    }

    private fun setupFavorite(
        carro: Carro,
        holder: ViewHolder
    ) {
        carro.isFavorite = !carro.isFavorite;

        if (carro.isFavorite) {
            holder.favorite.setImageResource(R.drawable.ic_star_selected)
        } else {
            holder.favorite.setImageResource(R.drawable.ic_star)
        }
    }

    class  ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val preco: TextView;
        val bateria : TextView;
        val potencia : TextView;
        val favorite : ImageView;
        init {
            view.apply {
                preco = findViewById(R.id.tv_preco_value);
                bateria = findViewById(R.id.tv_bateria_value);
                potencia = findViewById(R.id.tv_potencia_value);
                favorite = findViewById(R.id.iv_favorite);
            }
        }
    }
}
