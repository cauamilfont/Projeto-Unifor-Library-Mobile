package com.example.telaslivros

import android.content.Intent
import android.os.Bundle
import android.widget.Button


class BookDetailsActivity : BaseActivity() {
    override fun getBottomNavItemId() = 0;

    lateinit var requestButton : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_detalhes_livro)
        requestButton = findViewById(R.id.btnRequest)

        setupBottomNavigation()
    }

    override fun onStart() {
        super.onStart()
        requestButton.setOnClickListener {
            val intent = Intent(this, RentRequestActivity::class.java)
            startActivity(intent)
        }
    }
}