package com.example.telaslivros

import android.content.Intent
import android.os.Bundle
import android.widget.Button

class AddBooksActivity : BaseActivity() {
    override fun getBottomNavItemId() = 0;

    lateinit var btnSave : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_adicionar_livro)
        btnSave = findViewById(R.id.btnSalvar)


        setupBottomNavigation()
    }

    override fun onStart() {
        super.onStart()
        btnSave.setOnClickListener {
            val intent = Intent(this, ManageBooksActivity::class.java)
            startActivity(intent)
        }
    }
}