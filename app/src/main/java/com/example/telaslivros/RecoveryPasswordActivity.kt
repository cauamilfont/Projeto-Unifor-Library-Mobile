package com.example.telaslivros

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class RecoveryPasswordActivity : AppCompatActivity() {

    lateinit var btnSend : Button
    lateinit var btnBack : ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recuperar_senha)

        btnSend = findViewById(R.id.btnEnviarCodigo)
        btnBack = findViewById(R.id.backButton)
    }

    override fun onStart() {
        super.onStart()
        btnSend.setOnClickListener {
            val intent = Intent(this, RecoveryPasswordCodeActivity::class.java)
            startActivity(intent)
        }

        btnBack.setOnClickListener {
            finish()
        }
    }
}