package com.example.telaslivros

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit

class LoginActivity : AppCompatActivity() {


    lateinit var enter : Button
    lateinit var recoveryPassword : TextView
    lateinit var register : TextView
    lateinit var backButton : ImageButton
    lateinit var email : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)
        enter = findViewById(R.id.btnEntrar)
        recoveryPassword = findViewById(R.id.tvEsqueceuSenha)
        register = findViewById(R.id.tvCadastrar)
        backButton = findViewById(R.id.backButton)
        email = findViewById(R.id.etEmailLogin)


    }

    override fun onStart() {
        super.onStart()

        enter.setOnClickListener {
            val sharedPrefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
            sharedPrefs.edit {
                putString("EMAIL", email.text.toString())
            }

            val intent = if(email.text.toString().equals("admin", ignoreCase = true))
                Intent(this, AdminPanelActivity::class.java )
            else
                Intent(this, ExploreBooksActivity::class.java)

            startActivity(intent)
        }

        recoveryPassword.setOnClickListener {
            val intent = Intent(this, RecoveryPasswordActivity::class.java)
            startActivity(intent)
        }

        register.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}