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

        // CORREÇÃO: Acessando os dados dentro do objeto 'book' aninhado
        holder.title.text = "Título: ${rent.book?.title}"
        holder.author.text = "Autor: ${rent.book?.author}"

        // Datas (estão no objeto rent direto)
        holder.alugadoDate.text = "Alugado: ${rent.initialDate.format(formatter)}"
        holder.devolvidoDate.text = "Devolvido: ${rent.finalDate.format(formatter)}"

        // Imagem (está no objeto book)
        Glide.with(holder.itemView.context)
            .load(rent.book?.coverImage)
            .placeholder(R.drawable.ic_book_placeholder)
            .error(R.drawable.ic_book_error)
            .into(holder.cover)

        // Clique para Avaliação (Review)
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, ReviewActivity::class.java)

            // Passar dados do livro para a tela de avaliação
            intent.putExtra("BOOK_TITLE", rent.book?.title)
            intent.putExtra("BOOK_ID", rent.book?.id)


            context.startActivity(intent)
        }
    }
}