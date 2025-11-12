
package com.example.telaslivros

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ManageBooksActivity : BaseActivity() {
    override fun getBottomNavItemId() = 0;

    lateinit var btnAdd : FloatingActionButton
    private lateinit var recyclerView : RecyclerView
    private lateinit var adapter : ManageBooksAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gerenciar_livros)

        btnAdd = findViewById(R.id.fabAddLivro)
        recyclerView = findViewById(R.id.rvLivros)
        recyclerView.layoutManager = LinearLayoutManager(this)

        setupBottomNavigation()

        btnAdd.setOnClickListener {
            val intent = Intent(this, AddBooksActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()


        val listaDeLivrosReal = BookRepository.bookList


        adapter = ManageBooksAdapter(listaDeLivrosReal)
        recyclerView.adapter = adapter
    }


}