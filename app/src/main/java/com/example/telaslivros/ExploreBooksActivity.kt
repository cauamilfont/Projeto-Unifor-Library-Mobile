package com.example.telaslivros

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ExploreBooksActivity : BaseActivity() {

    override fun getBottomNavItemId() = R.id.navigation_inicio;

    lateinit var chatButton : FloatingActionButton
    lateinit var book : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_explorar_livros)
        chatButton = findViewById(R.id.fab_chat)
        book = findViewById(R.id.book)

        setupBottomNavigation()
    }

    override fun onStart() {
        super.onStart()
        chatButton.setOnClickListener {
        val intent = Intent(this, ChatBotActivity::class.java)
        startActivity(intent)
        }
        book.setOnClickListener {
            val intent = Intent(this, BookDetailsActivity::class.java)
            startActivity(intent)
        }
    }
}