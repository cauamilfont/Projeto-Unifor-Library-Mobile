package com.example.telaslivros

import android.content.Intent
import android.os.Bundle
import android.widget.Button

class RentRequestActivity : BaseActivity() {
    override fun getBottomNavItemId() = 0;

    lateinit var oktButton : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_requisicao_aluguel)
        oktButton = findViewById(R.id.btnOk)

        setupBottomNavigation()
    }

    override fun onStart() {
        super.onStart()
        oktButton.setOnClickListener {
            val intent = Intent(this, MyRentsActivity::class.java)
            startActivity(intent)
        }
    }
}