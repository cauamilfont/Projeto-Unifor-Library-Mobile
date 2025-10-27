package com.example.telaslivros

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class RecoveryPasswordCodeActivity : AppCompatActivity() {
    lateinit var btnContinue : Button
    lateinit var btnBack : ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recuperar_senha_codigo)

        btnContinue = findViewById(R.id.btnContinuar)
        btnBack = findViewById(R.id.backButton)

    }

    override fun onStart() {
        super.onStart()
        btnContinue.setOnClickListener {
            val intent = Intent(this, NewPasswordActivity::class.java)
            startActivity(intent)
        }
        btnBack.setOnClickListener {
            finish()
        }

    }
}