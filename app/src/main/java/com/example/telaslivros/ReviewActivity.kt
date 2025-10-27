package com.example.telaslivros

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.compose.ui.unit.IntRect

class ReviewActivity : BaseActivity() {
    override fun getBottomNavItemId() = 0;

    lateinit var backBtn : ImageButton
    lateinit var sendBtn : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_review)

        backBtn = findViewById(R.id.backButton)
        sendBtn = findViewById(R.id.btnEnviarAvaliacao)

        setupBottomNavigation()
    }

    override fun onStart() {
        super.onStart()

        backBtn.setOnClickListener {
            val intent = Intent(this, MyRentsActivity::class.java)
            startActivity(intent)
        }

        sendBtn.setOnClickListener {
            val intent = Intent(this, MyRentsActivity::class.java)
            startActivity(intent)
        }
    }
}