package com.example.telaslivros

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DetailsRequestActivity : BaseActivity() {
    override fun getBottomNavItemId() = 0;

    lateinit var btnAccept: FloatingActionButton
    lateinit var btnDecline : FloatingActionButton
    lateinit var title : TextView
    lateinit var author : TextView
    lateinit var user : TextView
    lateinit var requestDate : TextView
    lateinit var initialDate : TextView
    lateinit var finalDate : TextView
    lateinit var cover : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_detalhes_solicitacao)
        btnAccept = findViewById(R.id.btnAprovar)
        btnDecline = findViewById(R.id.btnRecusar)
        title = findViewById(R.id.tvTituloLivro)
        author = findViewById(R.id.tvAutorLivro)
        user = findViewById(R.id.tvNomeSolicitante)
        requestDate = findViewById(R.id.tvDataSolicitacao)
        initialDate = findViewById(R.id.tvDataInicial)
        finalDate = findViewById(R.id.tvDataFinal)
        cover = findViewById(R.id.imgLivro)


        setupBottomNavigation()
    }

    override fun onStart() {
        super.onStart()


        val imageURL = intent.getStringExtra("IMAGE_URL")


        title.text = "Título: ${intent.getStringExtra("TITLE")}"
        author.text = "Autor: ${intent.getStringExtra("AUTHOR")}"
        user.text = "Nome: ${intent.getStringExtra("USER")}"
        requestDate.text = intent.getStringExtra("R_DATE")
        initialDate.text = intent.getStringExtra("I_DATE")
        finalDate.text = intent.getStringExtra("F_DATE")


        Glide.with(this)
            .load(imageURL)
            .placeholder(R.drawable.ic_book_placeholder)
            .error(R.drawable.ic_book_error)
            .into(cover)

        btnAccept.setOnClickListener {
            val intent = Intent(this, ManageRequestsActivity::class.java)
            startActivity(intent)
        }

        btnDecline.setOnClickListener {
            val intent = Intent(this, ManageRequestsActivity::class.java)
            startActivity(intent)
        }
    }
}