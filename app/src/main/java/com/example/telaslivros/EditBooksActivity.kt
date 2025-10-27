package com.example.telaslivros

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.widget.Button
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputEditText

class EditBooksActivity : BaseActivity() {
    override fun getBottomNavItemId() = 0;

    lateinit var btnSave : Button
    lateinit var title : TextInputEditText
    lateinit var author : TextInputEditText
    lateinit var genre : TextInputEditText
    lateinit var synopsys : TextInputEditText
    lateinit var stock : TextInputEditText
    lateinit var cover : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_editar_livro)
        btnSave = findViewById(R.id.btnSalvar)
        title = findViewById(R.id.etTitulo)
        author = findViewById(R.id.etAutor)
        genre = findViewById(R.id.etGenero)
        synopsys = findViewById(R.id.etDescricao)
        stock = findViewById(R.id.etQuantidade)
        cover = findViewById(R.id.imgCapaLivro)


        setupBottomNavigation()
    }

    override fun onStart() {
        super.onStart()
        title.setText(intent.getStringExtra("TITLE"))
        author.setText(intent.getStringExtra("AUTHOR"))
        genre.setText(intent.getStringExtra("GENRE"))
        synopsys.setText(intent.getStringExtra("SYNOPSYS"))
        stock.setText(intent.getStringExtra("STOCK"))

        Glide.with(this)
            .load(intent.getStringExtra("IMAGE_URL"))
            .placeholder(R.drawable.ic_book_placeholder)
            .error(R.drawable.ic_book_error)
            .into(cover)

        btnSave.setOnClickListener {
            val intent = Intent(this, ManageBooksActivity::class.java)
            startActivity(intent)
        }
    }

}