package com.example.telaslivros

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {


    lateinit var enter : Button
    lateinit var recoveryPassword : TextView
    lateinit var register : TextView
    lateinit var backButton : ImageButton
    lateinit var email : EditText
    lateinit var password : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)
        enter = findViewById(R.id.btnEntrar)
        recoveryPassword = findViewById(R.id.tvEsqueceuSenha)
        register = findViewById(R.id.tvCadastrar)
        backButton = findViewById(R.id.backButton)
        email = findViewById(R.id.etEmailLogin)
        password= findViewById(R.id.etSenhaLogin)


        lifecycleScope.launch(Dispatchers.IO) {
                val admin = DatabaseHelper.verifyEmail("admin@teste.com")
                if (admin == 0)
                    DatabaseHelper.setAdmin()
            }
    }

    override fun onStart() {
        super.onStart()


        enter.setOnClickListener {

            if (email.text.toString().isBlank() || password.text.toString().isBlank()) {
                Toast.makeText(this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            lifecycleScope.launch(Dispatchers.IO) {
                val user = DatabaseHelper.loginUser(email.text.toString(), password.text.toString())

                withContext(Dispatchers.Main) {


                    if (user != null) {
                        // 1. GERA O TOKEN JWT
                        val token = JwtHelper.generateToken(user.id, user.role.toString())

                        Log.e("TOKEN", token)
                        val sessionPrefs = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                        sessionPrefs.edit {
                            // Salva o Token em vez de apenas flags soltas
                            putString("AUTH_TOKEN", token)

                            // Ainda guardamos esses para acesso rápido na UI, mas a segurança vem do token
                            putInt("USER_ID_INT", user.id)
                            putString("USER_NAME", user.name)
                            putString("USER_ROLE", user.role.toString())

                            putBoolean("IS_LOGGED_IN", true)
                        }

                        if (user.role.toString().equals("admin", ignoreCase = true)) {
                            val intent = Intent(this@LoginActivity, AdminPanelActivity::class.java)
                            startActivity(intent)
                        } else {
                            val intent =
                                Intent(this@LoginActivity, ExploreBooksActivity::class.java)
                            startActivity(intent)
                        }

                        finish()

                    } else {
                        Toast.makeText(
                            this@LoginActivity,
                            "E-mail ou senha inválidos",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
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