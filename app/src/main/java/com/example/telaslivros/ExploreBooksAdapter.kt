package com.example.telaslivros


import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ExploreBooksAdapter(private var bookList : List<Book>) :
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

    fun updateList(newList: List<Book>) {
        bookList = newList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ExploreBookViewHolder, position: Int) {
        val books = bookList[position]

        Log.e("BooksId", books.id.toString())
        val id : Int = books.id
        val bookTitle: String = books.title
        val urlImage = books.coverImage
        val author : String = books.author
        val synopsys = books.synopsis
        val bookQuality  = books.bookQuality
        val physicalQuality = books.physicalQuality


        holder.title.text = "$bookTitle"

        Glide.with(holder.itemView.context)
            .load(books.coverImage)
            .placeholder(R.drawable.ic_book_placeholder)
            .error(R.drawable.ic_book_error)
            .into(holder.cover)

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, BookDetailsActivity::class.java)
            intent.putExtra("BOOK_ID", id)
            intent.putExtra("TITLE", bookTitle)
            intent.putExtra("AUTHOR", author)
            intent.putExtra("SYNOPSYS", synopsys)
            if (bookQuality != null) intent.putExtra("BOOK_QUALITY", bookQuality)
            if (physicalQuality != null) intent.putExtra("PHYSICAL_QUALITY", physicalQuality)
            context.startActivity(intent)
        }


    }
}