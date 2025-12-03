package com.example.telaslivros

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.log

class RecoveryPasswordCodeActivity : AppCompatActivity() {
    lateinit var btnContinue : Button
    lateinit var reSend : TextView
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
        reSend = findViewById(R.id.tvReenviarCodigo)
        btnBack = findViewById(R.id.backButton)

    }

    override fun onStart() {
        super.onStart()
        val userId = intent.getIntExtra("USER_ID", 0)
        Log.e("USER_ID", userId.toString())
        val mockCode = intent.getStringExtra("CODE")
        val emailRecuperacao = intent.getStringExtra("EMAIL_RECUPERACAO")
        btnContinue.setOnClickListener {
            val codigoDigitado = "${code1.text}${code2.text}${code3.text}${code4.text}${code5.text}${code6.text}"


            lifecycleScope.launch(Dispatchers.IO) {

                val isValid = DatabaseHelper.validateRecoveryCode(emailRecuperacao!!, codigoDigitado)

                withContext(Dispatchers.Main) {
                    if (isValid) {
                        val intent = Intent(this@RecoveryPasswordCodeActivity, NewPasswordActivity::class.java)
                        intent.putExtra("EMAIL_RECUPERACAO", emailRecuperacao)
                        intent.putExtra("CODIGO_VALIDADO", codigoDigitado)
                        intent.putExtra("USER_ID", userId)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this@RecoveryPasswordCodeActivity, "Código inválido ou expirado.", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }

        reSend.setOnClickListener {
            if (emailRecuperacao.isNullOrBlank()) {
                Toast.makeText(this, "Erro: E-mail não encontrado.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            reSend.isEnabled = false
            reSend.alpha = 0.5f

            lifecycleScope.launch(Dispatchers.IO) {

                val novoCodigo = DatabaseHelper.requestPasswordReset(emailRecuperacao)

                withContext(Dispatchers.Main) {

                    reSend.isEnabled = true
                    reSend.alpha = 1.0f

                    if (novoCodigo != null) {
                        // 2. SUCESSO: Mostra o novo código (Simulação de E-mail)
                        Toast.makeText(
                            applicationContext,
                            "Novo código enviado: $novoCodigo",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        // 3. ERRO
                        Toast.makeText(
                            applicationContext,
                            "Erro ao reenviar. Tente novamente.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

        }
        btnBack.setOnClickListener {
            finish()
        }



    }
}