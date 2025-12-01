
package com.example.telaslivros

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.net.Uri
import java.io.ByteArrayOutputStream

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
    private var bookId: Int = 0



    private lateinit var bookToEdit: Book
    private var selectedImageBytes: ByteArray? = null
    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {

            cover.setImageURI(uri)


            lifecycleScope.launch(Dispatchers.IO) {
                selectedImageBytes = uriToByteArray(uri)
            }
        }
    }

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

        cover.setOnClickListener {
            pickImageLauncher.launch("image/*")
            Toast.makeText(this, "Selecione uma nova capa", Toast.LENGTH_SHORT).show()
        }

        btnSave.setOnClickListener {
            saveBookChanges()
        }


        btnExcluir.setOnClickListener {
            showConfirmDeleteDialog()
        }
    }

    private fun loadBookData() {

        bookId = intent.getIntExtra("BOOK_ID", 0)


        if (bookId == 0) {
            Toast.makeText(this, "Erro ao carregar livro.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }


        lifecycleScope.launch(Dispatchers.IO) {
            val bookFromDb = DatabaseHelper.getBookById(bookId)

            withContext(Dispatchers.Main) {
                if (bookFromDb != null) {
                    bookToEdit = bookFromDb


                    title.setText(bookToEdit.title)
                    author.setText(bookToEdit.author)
                    genre.setText(bookToEdit.genre)
                    synopsys.setText(bookToEdit.synopsis)
                    stock.setText(bookToEdit.stock.toString())


                    Glide.with(this@EditBooksActivity)
                        .load(bookToEdit.coverImage)
                        .placeholder(R.drawable.ic_book_placeholder)
                        .error(R.drawable.ic_book_error)
                        .into(cover)
                } else {
                    Toast.makeText(
                        this@EditBooksActivity,
                        "Livro não encontrado no banco.",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                }
            }
        }
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
        val finalImage = selectedImageBytes ?: bookToEdit.coverImage


        val updatedBook = bookToEdit.copy(
            title = newTitle,
            author = newAuthor,
            genre = newGenre,
            synopsis = newSynopsis,
            stock = newStock,
            coverImage = finalImage
        )


        lifecycleScope.launch(Dispatchers.IO) {
            val success = DatabaseHelper.updateBook(updatedBook)

            withContext(Dispatchers.Main) {
                if (success) {
                    Toast.makeText(this@EditBooksActivity, "Livro atualizado com sucesso!", Toast.LENGTH_SHORT).show()
                    navigateToManageBooks()
                } else {
                    Toast.makeText(this@EditBooksActivity, "Erro ao atualizar livro.", Toast.LENGTH_SHORT).show()
                }
            }
        }
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
        lifecycleScope.launch(Dispatchers.IO) {
            val success = DatabaseHelper.deleteBook(bookId)
            withContext(Dispatchers.Main) {
                if (success) {
                    Toast.makeText(this@EditBooksActivity, "Livro excluído!", Toast.LENGTH_SHORT).show()
                    navigateToManageBooks()
                } else {
                    Toast.makeText(this@EditBooksActivity, "Erro ao excluir.", Toast.LENGTH_SHORT).show()
                }
            }
        }
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

    private fun uriToByteArray(uri: Uri): ByteArray? {
        return try {
            val inputStream = contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)

            // Redimensiona para evitar imagens gigantes no banco (opcional, mas recomendado)
            val scaledBitmap = Bitmap.createScaledBitmap(bitmap, 600, 800, true)

            val stream = ByteArrayOutputStream()
            // Comprime para JPEG (reduz tamanho do arquivo significativamente)
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream)
            stream.toByteArray()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}