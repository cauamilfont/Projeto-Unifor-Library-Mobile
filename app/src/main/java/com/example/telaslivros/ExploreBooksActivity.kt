// ARQUIVO: ExploreBooksActivity.kt
package com.example.telaslivros

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ExploreBooksActivity : BaseActivity() {

    override fun getBottomNavItemId() = R.id.navigation_inicio;

    private lateinit var recyclerView : RecyclerView
    private lateinit var adapter : ExploreBooksAdapter
    lateinit var chatButton : FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_explorar_livros)

        chatButton = findViewById(R.id.fab_chat)
        recyclerView = findViewById(R.id.recyclerViewBooks)

        // **AQUI ESTÁ A LÓGICA:**
        // 1. Pegar a lista de livros DE VERDADE do repositório
        val listaDeLivrosReal = BookRepository.bookList

        // 2. Criar e configurar o adapter com a lista real
        adapter = ExploreBooksAdapter(listaDeLivrosReal)
        recyclerView.layoutManager = GridLayoutManager(this, 3)
        recyclerView.adapter = adapter

        setupBottomNavigation()
    }

    override fun onStart() {
        super.onStart()
        chatButton.setOnClickListener {
            val intent = Intent(this, ChatBotActivity::class.java)
            startActivity(intent)
        }

        // Notifica o adapter se a lista mudou
        adapter.notifyDataSetChanged()
    }

}