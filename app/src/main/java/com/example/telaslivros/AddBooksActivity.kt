
package com.example.telaslivros

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText

class AddBooksActivity : BaseActivity() {
    override fun getBottomNavItemId() = 0;

    lateinit var btnSave : Button
    lateinit var etTitulo: TextInputEditText
    lateinit var etAutor: TextInputEditText
    lateinit var etGenero: TextInputEditText
    lateinit var etSinopse: TextInputEditText
    lateinit var etEstoque: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adicionar_livro)
        setupBottomNavigation()

        btnSave = findViewById(R.id.btnSalvar)


        etTitulo = findViewById(R.id.etTitulo)
        etAutor = findViewById(R.id.etAutor)
        etGenero = findViewById(R.id.etGenero)
        etSinopse = findViewById(R.id.etSinopse)
        etEstoque = findViewById(R.id.etEstoque)


        btnSave.setOnClickListener {
            saveBook()
        }
    }

    private fun saveBook() {

        val titulo = etTitulo.text.toString()
        val autor = etAutor.text.toString()
        val genero = etGenero.text.toString()
        val sinopse = etSinopse.text.toString()
        val estoqueStr = etEstoque.text.toString()


        if (titulo.isBlank() || autor.isBlank() || estoqueStr.isBlank() || sinopse.isBlank()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            return // Para a execução aqui
        }


        val estoque = estoqueStr.toIntOrNull()
        if (estoque == null) {
            Toast.makeText(this, "Quantidade em estoque inválida", Toast.LENGTH_SHORT).show()
            return
        }


        val urlImagemPlaceholder = "https://m.media-amazon.com/images/I/910-Az3-p6L._AC_UF1000,1000_QL80_.jpg"


        val novoLivro = Book(
            title = titulo,
            author = autor,
            imageURL = urlImagemPlaceholder,
            genre = genero,
            stock = estoque,
            synopsis = sinopse,
            bookQuality = 0.0F,
            physicalQuality = 0.0F
        )


        BookRepository.bookList.add(0, novoLivro)

        Toast.makeText(this, "Livro salvo com sucesso!", Toast.LENGTH_SHORT).show()


        val intent = Intent(this, ManageBooksActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
        finish()
    }

    override fun onStart() {
        super.onStart()

    }
}