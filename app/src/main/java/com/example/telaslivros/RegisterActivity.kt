package com.example.telaslivros

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {

    lateinit var btnRegister : Button
    lateinit var btnLogin : TextView
    lateinit var btnBack : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_cadastro)
        btnRegister = findViewById(R.id.btnCadastrar)
        btnLogin = findViewById(R.id.tvJaTemConta)
        btnBack = findViewById(R.id.backButton)

    }

    override fun onStart() {
        super.onStart()

        val intent = Intent(this, LoginActivity::class.java)

        btnRegister.setOnClickListener {
            Toast.makeText(applicationContext, "Usuário criado com sucesso!", Toast.LENGTH_SHORT).show()
            startActivity(intent)
        }

        btnLogin.setOnClickListener {
            startActivity(intent)
        }
        btnBack.setOnClickListener {
            finish()
        }
    }
}