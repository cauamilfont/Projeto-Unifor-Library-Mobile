package com.example.telaslivros

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import com.bumptech.glide.Glide


class BookDetailsActivity : BaseActivity() {
    override fun getBottomNavItemId() = 0;

    lateinit var requestButton : Button
    lateinit var title : TextView
    lateinit var author : TextView
    lateinit var synopsys : TextView
    lateinit var bookQuality : TextView
    lateinit var physicalQuality : TextView
    lateinit var bookRB : RatingBar
    lateinit var physicalRB : RatingBar
    lateinit var cover : ImageView
    lateinit var btnBack : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_detalhes_livro)
        requestButton = findViewById(R.id.btnRequest)
        title = findViewById(R.id.tvTituloLivro)
        author = findViewById(R.id.tvAutorLivro)
        synopsys = findViewById(R.id.tvSinopse)
        bookQuality = findViewById(R.id.tvBookQuality)
        physicalQuality = findViewById(R.id.tvPhysicalQuality)
        bookRB = findViewById(R.id.rbQualidade)
        physicalRB = findViewById(R.id.rbFisico)
        cover = findViewById(R.id.ivCapaLivro)
        btnBack = findViewById(R.id.backButton)

        setupBottomNavigation()
    }

    override fun onStart() {
        super.onStart()
        title.text = "Título: ${intent.getStringExtra("TITLE")}"
        author.text = "Autor: ${intent.getStringExtra("AUTHOR")}"
        synopsys.text = intent.getStringExtra("SYNOPSYS")
        bookQuality.text = "${intent.getFloatExtra("BOOK_QUALITY", 0.0F)}/5"
        physicalQuality.text = "${intent.getFloatExtra("PHYSICAL_QUALITY", 0.0F)}/5"
        bookRB.rating = intent.getFloatExtra("BOOK_QUALITY", 0.0F)
        physicalRB.rating = intent.getFloatExtra("PHYSICAL_QUALITY", 0.0F)
        Glide.with(this)
            .load(intent.getStringExtra("URL_IMAGE"))
            .placeholder(R.drawable.ic_book_placeholder)
            .error(R.drawable.ic_book_error)
            .into(cover)

        requestButton.setOnClickListener {
            val urlImage : String? = intent.getStringExtra("URL_IMAGE")
            val intent = Intent(this, RentRequestActivity::class.java)
            intent.putExtra("TITLE", title.text)
            intent.putExtra("AUTHOR", author.text)
            intent.putExtra("URL_IMAGE", urlImage )
            startActivity(intent)
        }

        btnBack.setOnClickListener {
            finish()
        }
    }
}