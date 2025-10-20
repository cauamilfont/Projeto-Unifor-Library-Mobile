package com.example.telaslivros

import android.os.Bundle
import android.widget.GridView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ExplorarLivrosActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_explorar_livros)

        val gridView = findViewById<GridView>(R.id.gridLivros)

        // Exemplo simples de dados estáticos
        val livros = listOf("O Pequeno Príncipe", "Dom Casmurro", "Harry Potter", "1984", "Percy Jackson")

        // Adapter simples pra teste
        val adapter = android.widget.ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            livros
        )

        gridView.adapter = adapter

        gridView.setOnItemClickListener { _, _, position, _ ->
            Toast.makeText(this, "Selecionado: ${livros[position]}", Toast.LENGTH_SHORT).show()
        }
    }
}
