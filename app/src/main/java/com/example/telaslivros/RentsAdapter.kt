package com.example.telaslivros

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class RentsAdapter(private val rents: List<Rent>) :
    RecyclerView.Adapter<RentsAdapter.RentViewHolder>() {

    class RentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.tvBookTitle)
        val author: TextView = view.findViewById(R.id.tvBookAuthor)
        val status: TextView = view.findViewById(R.id.tvStatus)
        val cover: ImageView = view.findViewById(R.id.ivBookCover)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RentViewHolder {
        // Infla (cria) o layout XML do item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_rent_book, parent, false)
        return RentViewHolder(view)
    }


    override fun getItemCount() = rents.size

    override fun onBindViewHolder(holder: RentViewHolder, position: Int) {
        val rent = rents[position]

        // Coloca os dados nos TextViews
        holder.title.text = "Título: ${rent.title}"
        holder.author.text = "Autor: ${rent.author}"
        holder.status.text = "Status:\n${rent.status}"

        Glide.with(holder.itemView.context)
            .load(rent.imageUrl)
            .placeholder(R.drawable.ic_book_placeholder)
            .error(R.drawable.ic_book_error)
            .into(holder.cover)

        if(rent.status.equals("aprovado", ignoreCase = true )) {
            holder.itemView.setOnClickListener {
                val context = holder.itemView.context
                val intent = Intent(context, RemovalActivity::class.java)
                intent.putExtra("TITLE", rent.title)
                intent.putExtra("USER", rent.user)
                context.startActivity(intent)
            }
        }
        if(rent.status.equals("Histórico", ignoreCase = true )) {
            holder.itemView.setOnClickListener {
                val context = holder.itemView.context
                val intent = Intent(context, ReviewActivity::class.java)
                context.startActivity(intent)
            }
        }


    }
}