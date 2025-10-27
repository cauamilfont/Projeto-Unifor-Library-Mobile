package com.example.telaslivros

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView

class RemovalActivity : BaseActivity() {
    override fun getBottomNavItemId() = 0;

    lateinit var btnBack : ImageView
    lateinit var bookTitle : TextView
    lateinit var userName : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_removal)
        btnBack = findViewById(R.id.backButton)
        bookTitle = findViewById(R.id.tvBookInfo)
        userName = findViewById(R.id.tvUserInfo)


        setupBottomNavigation()
    }

    override fun onStart() {
        super.onStart()

        bookTitle.text = "Livro: ${intent.getStringExtra("TITLE")}"
        userName.text = "Usuário: ${intent.getStringExtra("USER")}"

        btnBack.setOnClickListener {
            val intent = Intent(this, MyRentsActivity::class.java)
            startActivity(intent)
        }

    }
}