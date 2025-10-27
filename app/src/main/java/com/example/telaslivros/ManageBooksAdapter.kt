package com.example.telaslivros

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ManageBooksAdapter (private val books: List<Book>) :
    RecyclerView.Adapter<ManageBooksAdapter.ManageBooksViewHolder>() {

    class ManageBooksViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.tvTitulo)
        val author: TextView = view.findViewById(R.id.tvAutor)
        val stock: TextView = view.findViewById(R.id.tvEstoque)
        val cover: ImageView = view.findViewById(R.id.imgCapa)
        val editBtn : Button = view.findViewById(R.id.btnEditar)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ManageBooksViewHolder {
        // Infla (cria) o layout XML do item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_livro_admin, parent, false)
        return ManageBooksViewHolder(view)
    }


    override fun getItemCount() = books.size


    override fun onBindViewHolder(holder: ManageBooksViewHolder, position: Int) {
        val book = books[position]


        holder.title.text = "Título: ${book.title}"
        holder.author.text = "Autor: ${book.author}"
        holder.stock.text = "Status: ${book.stock}"

        Glide.with(holder.itemView.context)
            .load(book.imageURL)
            .placeholder(R.drawable.ic_book_placeholder)
            .error(R.drawable.ic_book_error)
            .into(holder.cover)

        holder.editBtn.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, EditBooksActivity::class.java)
            intent.putExtra("IMAGE_URL", book.imageURL)
            intent.putExtra("TITLE", book.title)
            intent.putExtra("AUTHOR", book.author)
            intent.putExtra("SYNOPSYS", book.synopsis)
            intent.putExtra("STOCK", book.stock.toString())
            intent.putExtra("GENRE", book.genre)
            context.startActivity(intent)
        }

    }
}