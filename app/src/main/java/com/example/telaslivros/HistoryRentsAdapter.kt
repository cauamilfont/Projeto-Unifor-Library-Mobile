package com.example.telaslivros

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.time.format.DateTimeFormatter

class HistoryRentsAdapter(private val historyList: List<Rent>) :
    RecyclerView.Adapter<HistoryRentsAdapter.HistoryViewHolder>() {

    private val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")


    class HistoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cover: ImageView = view.findViewById(R.id.ivBookCover)
        val title: TextView = view.findViewById(R.id.tvBookTitle)
        val author: TextView = view.findViewById(R.id.tvBookAuthor)
        val alugadoDate: TextView = view.findViewById(R.id.tvAlugadoDate)
        val devolvidoDate: TextView = view.findViewById(R.id.tvDevolvidoDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_rent_history, parent, false)
        return HistoryViewHolder(view)
    }

    override fun getItemCount() = historyList.size

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val rent = historyList[position]


        holder.title.text = "Título: ${rent.title}"
        holder.author.text = "Autor: ${rent.author}"


        holder.alugadoDate.text = "Alugado: ${rent.initialDate.format(formatter)}"
        holder.devolvidoDate.text = "Devolvido: ${rent.finalDate.format(formatter)}"




        Glide.with(holder.itemView.context)
            .load(rent.imageUrl)
            .placeholder(R.drawable.ic_book_placeholder)
            .error(R.drawable.ic_book_error)
            .into(holder.cover)

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, ReviewActivity::class.java)
            context.startActivity(intent)
        }
    }


}