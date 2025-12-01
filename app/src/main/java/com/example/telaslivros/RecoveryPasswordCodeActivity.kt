package com.example.telaslivros

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import kotlin.math.log

class RecoveryPasswordCodeActivity : AppCompatActivity() {
    lateinit var btnContinue : Button
    lateinit var btnBack : ImageView
    lateinit var code1 : TextInputEditText
    lateinit var code2 : TextInputEditText
    lateinit var code3 : TextInputEditText
    lateinit var code4 : TextInputEditText
    lateinit var code5 : TextInputEditText
    lateinit var code6 : TextInputEditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recuperar_senha_codigo)



        code1 = findViewById(R.id.codigo1)
        code2 = findViewById(R.id.codigo2)
        code3 = findViewById(R.id.codigo3)
        code4 = findViewById(R.id.codigo4)
        code5 = findViewById(R.id.codigo5)
        code6 = findViewById(R.id.codigo6)
        btnContinue = findViewById(R.id.btnContinuar)
        btnBack = findViewById(R.id.backButton)

    }

    override fun onStart() {
        super.onStart()
        val userId = intent.getIntExtra("USER_ID", 0)
        val mockCode = intent.getStringExtra("CODE")
        btnContinue.setOnClickListener {
            val insertCode = "${code1.text.toString()}${code2.text.toString()}${code3.text.toString()}" +
                    "${code4.text.toString()}${code5.text.toString()}${code6.text.toString()}"
            Toast.makeText(this, "Código recebido : $insertCode --- $mockCode.", Toast.LENGTH_SHORT).show()

            if(insertCode == mockCode){
                Toast.makeText(this, "Código correto!", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, NewPasswordActivity::class.java)
                intent.putExtra("USER_EMAIL", userId)
                startActivity(intent)
            }
            else{
                Toast.makeText(this, "Código inválido.", Toast.LENGTH_SHORT).show()
            }

        }
        btnBack.setOnClickListener {
            finish()
        }

    }
}