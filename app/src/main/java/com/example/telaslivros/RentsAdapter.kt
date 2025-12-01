package com.example.telaslivros

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.time.format.DateTimeFormatter

class RentsAdapter(private val rents: List<Rent>) :
    RecyclerView.Adapter<RentsAdapter.RentViewHolder>() {

    class RentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.tvBookTitle)
        val author: TextView = view.findViewById(R.id.tvBookAuthor)
        val status: TextView = view.findViewById(R.id.tvStatus)
        val cover: ImageView = view.findViewById(R.id.ivBookCover)
        val tvInitialDate: TextView = view.findViewById(R.id.tvInitialDate)
        val tvFinalDate: TextView = view.findViewById(R.id.tvFinalDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_rent_book, parent, false)
        return RentViewHolder(view)
    }

    override fun getItemCount() = rents.size

    override fun onBindViewHolder(holder: RentViewHolder, position: Int) {
        val rent = rents[position]


        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        holder.title.text = "Título: ${rent.book?.title}"
        holder.author.text = "Autor: ${rent.book?.author}"
        holder.tvInitialDate.text = "Retirada: ${rent.initialDate?.format(formatter) ?: "-"}"
        holder.tvFinalDate.text = "Devolução: ${rent.finalDate?.format(formatter) ?: "-"}"


        holder.status.text = "Status:\n${rent.status}"


        Glide.with(holder.itemView.context)
            .load(rent.book?.coverImage)
            .placeholder(R.drawable.ic_book_placeholder)
            .error(R.drawable.ic_book_error)
            .into(holder.cover)


        if (rent.status == Status.APROVADO) {

            holder.itemView.setOnClickListener {
                val context = holder.itemView.context

                val intent = Intent(context, RemovalActivity::class.java)


                intent.putExtra("EXTRA_TRANSACTION_ID", rent.id.toString())
                intent.putExtra("BOOK_TITLE", rent.book?.title)
                intent.putExtra("BOOK_AUTHOR", rent.book?.author)
                intent.putExtra("USER_NAME", rent.userName)

                context.startActivity(intent)
            }
        } else {

            holder.itemView.setOnClickListener(null)
        }
    }
}