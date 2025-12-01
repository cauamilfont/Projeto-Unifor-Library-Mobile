package com.example.telaslivros

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
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
            val existentAccount = DatabaseHelper.verifyEmail(email.text.toString())
            if(existentAccount == 0){
                Toast.makeText(this, "Email não existente", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            else{
                val code = GenerateCode()

                Toast.makeText(this, code, Toast.LENGTH_LONG).show()
                val intent = Intent(this, RecoveryPasswordCodeActivity::class.java)
                intent.putExtra("CODE", code)
                intent.putExtra("EMAIL", email.text.toString())
                startActivity(intent)
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