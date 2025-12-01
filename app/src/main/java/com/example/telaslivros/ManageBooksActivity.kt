
package com.example.telaslivros

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.widget.EditText
import android.text.TextWatcher
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ManageBooksActivity : BaseActivity() {
    override fun getBottomNavItemId() = 0;

    lateinit var btnAdd : FloatingActionButton
    lateinit var etSearch: EditText
    private lateinit var recyclerView : RecyclerView
    private lateinit var adapter : ManageBooksAdapter
    private var allBooksList: List<Book> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gerenciar_livros)

        btnAdd = findViewById(R.id.fabAddLivro)
        recyclerView = findViewById(R.id.rvLivros)
        recyclerView.layoutManager = LinearLayoutManager(this)
        etSearch = findViewById(R.id.etBuscarLivros)
        adapter = ManageBooksAdapter(emptyList())
        recyclerView.adapter = adapter

        setupBottomNavigation()

        btnAdd.setOnClickListener {
            val intent = Intent(this, AddBooksActivity::class.java)
            startActivity(intent)
        }

        setupSearchListener()
    }

    override fun onStart() {
        super.onStart()

        lifecycleScope.launch(Dispatchers.IO) {
            val listaDoBanco = DatabaseHelper.getAllBooks()

            withContext(Dispatchers.Main) {

                allBooksList = listaDoBanco


                adapter.updateList(allBooksList)


                if (etSearch.text.isNotEmpty()) {
                    filtrarLivros(etSearch.text.toString())
                }
            }
        }
    }




    private fun setupSearchListener() {
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val textoDigitado = s.toString()
                filtrarLivros(textoDigitado)
            }
        })
    }

    private fun filtrarLivros(texto: String) {

        val listaFiltrada = if (texto.isEmpty()) {
            allBooksList
        } else {
            allBooksList.filter { livro ->
                livro.title.contains(texto, ignoreCase = true) ||
                        livro.author.contains(texto, ignoreCase = true)
            }
        }

        adapter.updateList(listaFiltrada)
    }


}