package com.example.telaslivros

import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout

class MeusAlugueisActivity : AppCompatActivity() {

    private lateinit var tabLayout: TabLayout
    private lateinit var listaAlugueis: LinearLayout
    private lateinit var btnVoltar: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meus_alugueis)

        // Referências dos componentes
        tabLayout = findViewById(R.id.tabLayout)
        listaAlugueis = findViewById(R.id.listaAlugueis)
        btnVoltar = findViewById(R.id.btnVoltar)

        // Botão voltar
        btnVoltar.setOnClickListener {
            finish()
        }

        // Tabs: alternar conteúdo conforme aba selecionada
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> mostrarAlugueisEmAnalise()
                    1 -> mostrarAlugueisAprovados()
                    2 -> mostrarHistorico()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        // Mostra a primeira aba por padrão
        mostrarAlugueisEmAnalise()
    }

    private fun mostrarAlugueisEmAnalise() {
        listaAlugueis.removeAllViews()
        adicionarCardLivro("A Culpa é das Estrelas", "John Green", "Status: Pendente")
        adicionarCardLivro("O Pequeno Príncipe", "Antoine de Saint-Exupéry", "Status: Pendente")
    }

    private fun mostrarAlugueisAprovados() {
        listaAlugueis.removeAllViews()
        adicionarCardLivro("Duna", "Frank Herbert", "Status: Aprovado")
        adicionarCardLivro("O Hobbit", "J. R. R. Tolkien", "Status: Aprovado")
    }

    private fun mostrarHistorico() {
        listaAlugueis.removeAllViews()
        adicionarCardLivro("A Culpa é das Estrelas", "John Green", "Devolvido em 17/04/2025")
        adicionarCardLivro("Duna", "Frank Herbert", "Devolvido em 21/08/2025")
    }

    private fun adicionarCardLivro(titulo: String, autor: String, status: String) {
        val card = layoutInflater.inflate(R.layout.item_livro_card, listaAlugueis, false)
        val txtTitulo = card.findViewById<TextView>(R.id.txtTitulo)
                val txtAutor = card.findViewById<TextView>(R.id.txtAutor)
                val txtStatus = card.findViewById<TextView>(R.id.txtStatus)

                txtTitulo.text = "Título: $titulo"
        txtAutor.text = "Autor: $autor"
        txtStatus.text = status

        listaAlugueis.addView(card)
    }
}
