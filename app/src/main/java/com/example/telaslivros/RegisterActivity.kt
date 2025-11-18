package com.example.telaslivros

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterActivity : AppCompatActivity() {

    lateinit var name: TextInputEditText
    lateinit var email: TextInputEditText
    lateinit var password: TextInputEditText
    lateinit var confirmPassword: TextInputEditText
    lateinit var acceptTerms: MaterialCheckBox
    lateinit var btnRegister: Button
    lateinit var btnLogin: TextView
    lateinit var btnBack: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_cadastro)
        name = findViewById(R.id.etNome)
        email = findViewById(R.id.etEmail)
        password = findViewById(R.id.etSenha)
        confirmPassword = findViewById(R.id.etConfirmarSenha)
        acceptTerms = findViewById(R.id.cbTermos)
        btnRegister = findViewById(R.id.btnCadastrar)
        btnLogin = findViewById(R.id.tvJaTemConta)
        btnBack = findViewById(R.id.backButton)

    }

    override fun onStart() {
        super.onStart()

        val intent = Intent(this, LoginActivity::class.java)

        val sharedPrefs = getSharedPreferences("user_database", Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()
        if (sharedPrefs.getString("user_admin@teste.com", null) == null) {
            val role = "admin"
            val passwordAdmin = "admin123"
            val nameAdmin = "Administrador"
            editor.putString("user_admin@teste.com", "$passwordAdmin,$role")
            editor.putString("name_admin@teste.com", nameAdmin)
            editor.apply()
        }

        btnRegister.setOnClickListener {
            if (!acceptTerms.isChecked) {
                Toast.makeText(
                    applicationContext,
                    "Aceite os termos de Serviço antes de continuar!",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            if (name.text.toString().isBlank() || email.text.toString()
                    .isBlank() || password.text.toString()
                    .isBlank() || confirmPassword.text.toString().isBlank()
            ) {
                Toast.makeText(this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            if (confirmPassword.text.toString() != password.text.toString()) {
                Toast.makeText(
                    this,
                    "Senha e confirmar senha estão diferentes!",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            lifecycleScope.launch(Dispatchers.IO) {
                val verifyEmail = DatabaseHelper.verifyEmail(email.text.toString())
                if (verifyEmail) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@RegisterActivity,
                            "Email já cadastrado!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }
                val success = DatabaseHelper.registerUser(
                    name.text.toString(),
                    email.text.toString(),
                    password.text.toString()
                )
                withContext(Dispatchers.Main) {
                    if (success) {
                        Toast.makeText(
                            this@RegisterActivity,
                            "Cadastrado com sucesso!",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    } else {
                        Toast.makeText(
                            this@RegisterActivity,
                            "Erro ao cadastrar",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            }

            btnLogin.setOnClickListener {
                startActivity(intent)
            }
            btnBack.setOnClickListener {
                finish()
            }
        }
    }
}