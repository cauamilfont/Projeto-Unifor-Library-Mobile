package com.example.telaslivros

import android.content.Intent
import android.os.Bundle
import android.util.Log
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

            val email = intent.getStringExtra("EMAIL_RECUPERACAO")
            val code = intent.getStringExtra("CODIGO_VALIDADO")


            Log.e("USER_EMAIL", email.toString())

            val newPasswordText = newPassword.text.toString()
            val confirmPasswordText = confirmPassword.text.toString()
            if (newPasswordText != confirmPasswordText) {
                Toast.makeText(applicationContext, "As senhas não conferem!", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            lifecycleScope.launch(Dispatchers.IO) {

                val sucesso = DatabaseHelper.updatePasswordWithCode(email!!, code!!, newPasswordText)


                withContext(Dispatchers.Main) {
                    if (sucesso) {
                        Toast.makeText(
                            applicationContext,
                            "Senha alterada com sucesso!",
                            Toast.LENGTH_SHORT
                        ).show()
                        startActivity(Intent(this@NewPasswordActivity, LoginActivity::class.java))
                        finishAffinity()
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Erro ao alterar senha.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

    }
}