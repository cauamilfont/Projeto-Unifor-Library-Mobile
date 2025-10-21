package com.example.telaslivros

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView


class ChatBotActivity : BaseActivity() {

    override fun getBottomNavItemId() = 0;

    lateinit var btnBack : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_chatbot)
        btnBack = findViewById(R.id.backButton)


        setupBottomNavigation()
    }

    override fun onStart() {
        super.onStart()
        btnBack.setOnClickListener {
            val intent = Intent(this, ExploreBooksActivity::class.java)
            startActivity(intent)
        }
    }
}