
package com.example.telaslivros

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

class AddBooksActivity : BaseActivity() {
    override fun getBottomNavItemId() = 0;

    lateinit var btnSave : Button
    lateinit var etTitulo: TextInputEditText
    lateinit var etAutor: TextInputEditText
    lateinit var etGenero: TextInputEditText
    lateinit var etSinopse: TextInputEditText
    lateinit var etEstoque: TextInputEditText

    lateinit var frameUpload: FrameLayout
    lateinit var imgCoverPreview: ImageView

    // Variável para guardar os bytes da imagem selecionada
    private var selectedImageBytes: ByteArray? = null


    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            // 1. Mostra o preview na tela
            imgCoverPreview.setImageURI(uri)
            imgCoverPreview.visibility = View.VISIBLE

            // 2. Converte para ByteArray em background para não travar a tela
            lifecycleScope.launch(Dispatchers.IO) {
                selectedImageBytes = uriToByteArray(uri)
            }
        }
    }

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
        frameUpload = findViewById(R.id.frameUpload)
        imgCoverPreview = findViewById(R.id.imgCoverPreview)

        frameUpload.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

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


        val urlImagemPlaceholder =
            "https://m.media-amazon.com/images/I/910-Az3-p6L._AC_UF1000,1000_QL80_.jpg"


        lifecycleScope.launch(Dispatchers.IO) {
            val novoLivro = Book(
                id = 0, // ID será gerado pelo banco
                title = titulo,
                author = autor,
                coverImage = selectedImageBytes,
                genre = genero,
                stock = estoque,
                synopsis = sinopse,
                bookQuality = 0.0F,
                physicalQuality = 0.0F
            )


            val sucesso = DatabaseHelper.createBook(novoLivro)

            withContext(Dispatchers.Main) {
                if (sucesso) {
                    Toast.makeText(
                        this@AddBooksActivity,
                        "Livro salvo no banco!",
                        Toast.LENGTH_SHORT
                    ).show()
                    val intent = Intent(this@AddBooksActivity, ManageBooksActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this@AddBooksActivity, "Erro ao salvar.", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()

    }

    private fun uriToByteArray(uri: Uri): ByteArray? {
        return try {
            val inputStream = contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)

            // Redimensionar se for muito grande (opcional, mas recomendado para o banco não explodir)
            val scaledBitmap = Bitmap.createScaledBitmap(bitmap, 600, 800, true)

            val stream = ByteArrayOutputStream()
            // Comprime para JPEG com 70% de qualidade
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream)
            stream.toByteArray()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}