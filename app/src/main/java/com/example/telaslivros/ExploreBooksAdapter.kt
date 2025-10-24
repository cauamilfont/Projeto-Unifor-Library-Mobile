package com.example.telaslivros


import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ExploreBooksAdapter(private val bookList : MutableList<Book>) :
    RecyclerView.Adapter<ExploreBooksAdapter.ExploreBookViewHolder>(){

    class ExploreBookViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val title : TextView = view.findViewById(R.id.tvBookTitle)
        val cover : ImageView = view.findViewById(R.id.ivBookCover)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExploreBookViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_book_grid, parent, false)
        return ExploreBookViewHolder(view)
    }

    override fun getItemCount() = bookList.size

    override fun onBindViewHolder(holder: ExploreBookViewHolder, position: Int) {
        val books = bookList[position]

        holder.title.text = "Livro: ${books.title}"

        Glide.with(holder.itemView.context)
            .load(books.imageURL)
            .placeholder(R.drawable.ic_book_placeholder)
            .error(R.drawable.ic_book_error)
            .into(holder.cover)

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, BookDetailsActivity::class.java)
            context.startActivity(intent)
        }


    }
}