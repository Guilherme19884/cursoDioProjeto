package com.example.dio1.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dio1.Dominio.Carro
import com.example.dio1.R

class CarAdapter(private val carros: List<Carro>) : RecyclerView.Adapter<CarAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.carro_item, parent, false);
        return ViewHolder(view);
    }

    override fun getItemCount(): Int = carros.size;

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.preco.text = carros[position].preco;
        holder.bateria.text = carros[position].bateria;
        holder.potencia.text = carros[position].potencia;

    }


    class  ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val preco: TextView;
        val bateria : TextView;
        val potencia : TextView;
        init {
            view.apply {
                preco = findViewById(R.id.tv_preco_value);
                bateria = findViewById(R.id.tv_bateria_value);
                potencia = findViewById(R.id.tv_potencia_value);
            }
        }
    }
}
