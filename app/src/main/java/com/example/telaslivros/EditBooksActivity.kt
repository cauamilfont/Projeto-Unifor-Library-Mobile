
package com.example.telaslivros

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputEditText

class EditBooksActivity : BaseActivity() {
    override fun getBottomNavItemId() = 0;


    lateinit var btnSave : Button
    lateinit var btnExcluir : Button
    lateinit var title : TextInputEditText
    lateinit var author : TextInputEditText
    lateinit var genre : TextInputEditText
    lateinit var synopsys : TextInputEditText
    lateinit var stock : TextInputEditText
    lateinit var cover : ImageView


    private var bookPosition: Int = -1
    private lateinit var bookToEdit: Book

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_livro)
        setupBottomNavigation()


        btnSave = findViewById(R.id.btnSalvar)
        btnExcluir = findViewById(R.id.btnExcluir)
        title = findViewById(R.id.etTitulo)
        author = findViewById(R.id.etAutor)
        genre = findViewById(R.id.etGenero)
        synopsys = findViewById(R.id.etDescricao)
        stock = findViewById(R.id.etQuantidade)
        cover = findViewById(R.id.imgCapaLivro)


        loadBookData()


        btnSave.setOnClickListener {
            saveBookChanges()
        }


        btnExcluir.setOnClickListener {
            showConfirmDeleteDialog()
        }
    }

    private fun loadBookData() {
        // 4. Receber a POSIÇÃO enviada pelo Adapter
        bookPosition = intent.getIntExtra("BOOK_POSITION", -1)

        // Se a posição for inválida, feche a tela.
        if (bookPosition == -1) {
            Toast.makeText(this, "Erro ao carregar livro.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }


        bookToEdit = BookRepository.bookList[bookPosition]


        title.setText(bookToEdit.title)
        author.setText(bookToEdit.author)
        genre.setText(bookToEdit.genre)
        synopsys.setText(bookToEdit.synopsis)
        stock.setText(bookToEdit.stock.toString())

        Glide.with(this)
            .load(bookToEdit.imageURL)
            .placeholder(R.drawable.ic_book_placeholder)
            .error(R.drawable.ic_book_error)
            .into(cover)
    }

    private fun saveBookChanges() {

        val newTitle = title.text.toString()
        val newAuthor = author.text.toString()
        val newGenre = genre.text.toString()
        val newSynopsis = synopsys.text.toString()
        val newStockStr = stock.text.toString()


        if (newTitle.isBlank() || newAuthor.isBlank() || newStockStr.isBlank()) {
            Toast.makeText(this, "Título, Autor e Estoque são obrigatórios", Toast.LENGTH_SHORT).show()
            return
        }
        val newStock = newStockStr.toIntOrNull()
        if (newStock == null) {
            Toast.makeText(this, "Estoque inválido", Toast.LENGTH_SHORT).show()
            return
        }


        val updatedBook = bookToEdit.copy(
            title = newTitle,
            author = newAuthor,
            genre = newGenre,
            synopsis = newSynopsis,
            stock = newStock
        )


        BookRepository.bookList[bookPosition] = updatedBook

        Toast.makeText(this, "Livro atualizado!", Toast.LENGTH_SHORT).show()


        navigateToManageBooks()
    }


    private fun showConfirmDeleteDialog() {
        AlertDialog.Builder(this)
            .setTitle("Excluir Livro")
            .setMessage("Tem certeza que deseja excluir '${bookToEdit.title}'? Esta ação não pode ser desfeita.")
            .setPositiveButton("Excluir") { dialog, which ->
                deleteBook()
            }
            .setNegativeButton("Cancelar", null)

            .show()
    }


    private fun deleteBook() {
        if (bookPosition == -1) {
            Toast.makeText(this, "Erro: Livro não encontrado", Toast.LENGTH_SHORT).show()
            return
        }


        BookRepository.bookList.removeAt(bookPosition)

        Toast.makeText(this, "Livro excluído!", Toast.LENGTH_SHORT).show()


        navigateToManageBooks()
    }


    private fun navigateToManageBooks() {
        val intent = Intent(this, ManageBooksActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
        finish()
    }

    override fun onStart() {
        super.onStart()

    }
}