package com.example.telaslivros

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RentsAdapter(private val rents: List<Rent>) :
    RecyclerView.Adapter<RentsAdapter.RentViewHolder>() {

    class RentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.tvBookTitle)
        val author: TextView = view.findViewById(R.id.tvBookAuthor)
        val status: TextView = view.findViewById(R.id.tvStatus)
        val cover: ImageView = view.findViewById(R.id.ivBookCover)
    }

    // 2. onCreateViewHolder (Criador de Visualizações)
    // Chamado quando o RecyclerView precisa criar um novo "card"
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RentViewHolder {
        // Infla (cria) o layout XML do item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_rent_book, parent, false)
        return RentViewHolder(view)
    }

    // 3. getItemCount (Contador de Itens)
    // Diz ao RecyclerView quantos itens existem na lista
    override fun getItemCount() = rents.size

    // 4. onBindViewHolder (Vinculador de Dados)
    // Pega os dados de uma posição da lista e os coloca no "card"
    override fun onBindViewHolder(holder: RentViewHolder, position: Int) {
        val rent = rents[position] // Pega o item da lista

        // Coloca os dados nos TextViews
        holder.title.text = "Título: ${rent.title}"
        holder.author.text = "Autor: ${rent.author}"
        holder.status.text = "Status:\n${rent.status}"
        // holder.cover.setImageResource(...) // (Aqui você carregaria a imagem)
    }
}