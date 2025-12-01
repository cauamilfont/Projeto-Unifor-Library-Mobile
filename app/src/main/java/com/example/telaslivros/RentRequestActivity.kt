package com.example.telaslivros

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RentRequestActivity : BaseActivity() {
    override fun getBottomNavItemId() = 0;

    lateinit var oktButton : Button
    lateinit var title : TextView
    lateinit var author : TextView
    lateinit var cover : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_requisicao_aluguel)
        oktButton = findViewById(R.id.btnOk)
        title = findViewById(R.id.tvTitle)
        author = findViewById(R.id.tvAuthor)
        cover = findViewById(R.id.ivImage)

        setupBottomNavigation()
    }

    override fun onStart() {
        super.onStart()
        val bookId = intent.getIntExtra("BOOK_ID", 0)

        lifecycleScope.launch(Dispatchers.IO) {
            val book = DatabaseHelper.getBookById(bookId)
            if(book != null){
                withContext(Dispatchers.Main) {
                    Glide.with(this@RentRequestActivity)
                        .load(book.coverImage)
                        .placeholder(R.drawable.ic_book_placeholder)
                        .error(R.drawable.ic_book_error)
                        .into(cover)
                }

            }



        }

        title.text = "Título : ${intent.getStringExtra("TITLE")}"
        author.text = "Autor : ${intent.getStringExtra("AUTHOR")}"



        oktButton.setOnClickListener {
            val intent = Intent(this, ExploreBooksActivity::class.java)
            startActivity(intent)
        }
    }
}