package com.example.telaslivros

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NewPasswordActivity : AppCompatActivity() {
    lateinit var btnConfirm: Button
    lateinit var newPassword: EditText
    lateinit var confirmPassword: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nova_senha)



        newPassword = findViewById(R.id.etNovaSenha)
        confirmPassword = findViewById(R.id.etConfirmarSenha)
        btnConfirm = findViewById(R.id.btnConfirmar)

    }

    override fun onStart() {
        super.onStart()
        btnConfirm.setOnClickListener {
            val userId = intent.getIntExtra("USER_ID", 0)
            if (userId == 0) {
                Toast.makeText(this, "Erro fatal. E-mail não encontrado.", Toast.LENGTH_SHORT)
                    .show()
                finish()
            }
            val newPasswordText = newPassword.text.toString()
            val confirmPasswordText = confirmPassword.text.toString()
            if (newPasswordText != confirmPasswordText) {
                Toast.makeText(applicationContext, "As senhas não conferem!", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            lifecycleScope.launch(Dispatchers.IO) {
                val userDatabase =
                    DatabaseHelper.changePassword(newPasswordText, userId)

                withContext(Dispatchers.Main) {
                    if (userDatabase) {
                        Toast.makeText(
                            applicationContext,
                            "Senha alterada com sucesso!",
                            Toast.LENGTH_SHORT
                        ).show()
                        val intent = Intent(this@NewPasswordActivity, LoginActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                    }
                    Toast.makeText(
                        applicationContext,
                        "Erro ao Alterar Senha!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        }

    }
}