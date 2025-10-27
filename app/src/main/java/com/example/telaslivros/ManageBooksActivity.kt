package com.example.telaslivros

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
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
        val mockData = createMockData()
        adapter = ManageBooksAdapter(mockData)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter


        setupBottomNavigation()
    }

    override fun onStart() {
        super.onStart()


        btnAdd.setOnClickListener {
            val intent = Intent(this, AddBooksActivity::class.java)
            startActivity(intent)
        }
    }

    private fun createMockData() : MutableList<Book>{
        return mutableListOf(
            Book(
                title = "A Culpa é das Estrelas",
                author = "John Green",
                imageURL = "https://m.media-amazon.com/images/I/811ivBP1rsL._UF1000,1000_QL80_.jpg",
                synopsis = "Hazel Grace Lancaster, uma paciente com câncer de 16 anos, conhece e se apaixona por Gus Waters, um adolescente com uma perna amputada.",
                bookQuality = 3.2F,
                physicalQuality = 4.5F,
                stock = 5,
                genre = "Romance"
            ),
            Book(
                title = "O Pequeno Príncipe",
                author = "Antoine de Saint-Exupéry",
                imageURL = "https://m.media-amazon.com/images/I/81TmOZIXvzL._UF1000,1000_QL80_.jpg",
                synopsis = "Um piloto cai no deserto do Saara e conhece um jovem príncipe que viajou de seu próprio asteróide.",
                bookQuality = 4.9F,
                physicalQuality = 4.8F,
                stock = 10,
                genre = "Literatura Infantil"
            ),
            Book(
                title = "Duna",
                author = "Frank Herbert",
                imageURL = "https://m.media-amazon.com/images/I/81zN7udGRUL.jpg",
                synopsis = "Uma saga de ficção científica sobre a luta pelo controle do planeta deserto Arrakis, a única fonte da especiaria 'melange'.",
                bookQuality = 4.8F,
                physicalQuality = 4.6F,
                stock = 8,
                genre = "Ficção Científica"
            ),
            Book(
                title = "1984",
                author = "George Orwell",
                imageURL = "https://m.media-amazon.com/images/I/61t0bwt1s3L._AC_UF1000,1000_QL80_.jpg",
                synopsis = "Em um futuro distópico, a sociedade é controlada pelo Partido e seu líder onipresente, o Grande Irmão.",
                bookQuality = 4.9F,
                physicalQuality = 4.5F,
                stock = 15,
                genre = "Ficção Científica"
            ),

            Book(
                title = "Harry Potter e a Pedra Filosofal",
                author = "J.K. Rowling",
                imageURL = "https://m.media-amazon.com/images/I/61jgm6ooXzL._AC_UF1000,1000_QL80_.jpg",
                synopsis = "Um jovem órfão descobre que é um bruxo e é convidado a estudar na Escola de Magia e Bruxaria de Hogwarts.",
                bookQuality = 4.9F,
                physicalQuality = 4.7F,
                stock = 10,
                genre = "Fantasia"
            )
        )
    }
}