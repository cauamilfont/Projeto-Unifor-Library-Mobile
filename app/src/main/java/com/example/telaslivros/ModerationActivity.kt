package com.example.telaslivros

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ModerationActivity : BaseActivity() {
    override fun getBottomNavItemId() = R.id.navigation_admin_moderacao;

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ModerationAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_moderacao)



        setupBottomNavigation()

        recyclerView = findViewById(R.id.recyclerViewComentarios)

        val mockData = createMockData()

        adapter = ModerationAdapter(mockData)


        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    override fun onStart() {
        super.onStart()

}

    private fun createMockData(): MutableList<Comment> {
        return mutableListOf(
            Comment(
                id = "1",
                bookTitle = "O Guia do Mochileiro das Galáxias",
                commentContent = "Este é um exemplo de comentário que precisa ser moderado. O livro é muito bom, mas a entrega demorou um pouco.",
                ratingContent = 4.5f,
                ratingPhysical = 4.0f,
                authorName = "João Silva"
            ),
            Comment(
                id = "2",
                bookTitle = "Duna",
                commentContent = "Odiei. A história é muito confusa e os personagens são chatos. Não recomendo para ninguém. Péssima experiência.",
                ratingContent = 1.0f,
                ratingPhysical = 3.0f,
                authorName = "Maria Souza"
            ),
            Comment(
                id = "3",
                bookTitle = "A Culpa é das Estrelas",
                commentContent = "Simplesmente perfeito! Chegou rápido e o livro é lindo. Chorei horrores. 10/10.",
                ratingContent = 5.0f,
                ratingPhysical = 5.0f,
                authorName = "Ana Clara"
            )
        )
    }
    }