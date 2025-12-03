package com.example.telaslivros

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.collection.emptyIntSet
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random

class RecoveryPasswordActivity : AppCompatActivity() {

    lateinit var btnSend : Button
    lateinit var btnBack : ImageButton
    lateinit var email : TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recuperar_senha)
        email = findViewById(R.id.etEmailRecuperacao)
        btnSend = findViewById(R.id.btnEnviarCodigo)
        btnBack = findViewById(R.id.backButton)
    }

    override fun onStart() {
        super.onStart()
        val sharedPrefs = getSharedPreferences("user_database", Context.MODE_PRIVATE)


        btnSend.setOnClickListener {
            val email = email.text.toString()

            lifecycleScope.launch(Dispatchers.IO) {

                val codigoGerado = DatabaseHelper.requestPasswordReset(email)

                withContext(Dispatchers.Main) {
                    if (codigoGerado != null) {

                        Toast.makeText(this@RecoveryPasswordActivity, "Email enviado! Código: $codigoGerado", Toast.LENGTH_LONG).show()

                        val intent = Intent(this@RecoveryPasswordActivity, RecoveryPasswordCodeActivity::class.java)
                        intent.putExtra("EMAIL_RECUPERACAO", email)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this@RecoveryPasswordActivity, "E-mail não encontrado.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        btnBack.setOnClickListener {
            finish()
        }
    }

    fun GenerateCode(): String {
        val code = Random.nextInt(0, 1000000) //
        return code.toString().padStart(6, '0')
    }
}