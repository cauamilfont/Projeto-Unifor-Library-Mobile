package com.example.telaslivros

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class RecoveryPasswordActivity : AppCompatActivity() {

    lateinit var btnSend : Button
    lateinit var btnBack : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recuperar_senha)

        btnSend = findViewById(R.id.btnEnviarCodigo)
        btnBack = findViewById(R.id.tvVoltarLogin)
    }

    override fun onStart() {
        super.onStart()
        btnSend.setOnClickListener {
            val intent = Intent(this, RecoveryPasswordCodeActivity::class.java)
            startActivity(intent)
        }

        btnBack.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}