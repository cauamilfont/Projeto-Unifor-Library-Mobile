package com.example.telaslivros

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class NotificacaoActivity : BaseActivity() {
    override fun getBottomNavItemId() = 0;

    lateinit var switchAluguel: SwitchCompat
    lateinit var switchDevolucao: SwitchCompat
    lateinit var switchRetirada: SwitchCompat
    lateinit var switchRecomendacoes: SwitchCompat
    lateinit var saveButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_notificacao_user)
        switchAluguel = findViewById(R.id.switchAluguel)
        switchDevolucao = findViewById(R.id.switchDevolucao)
        switchRetirada = findViewById(R.id.switchRetirada)
        switchRecomendacoes = findViewById(R.id.switchRecomendacoes)
        saveButton = findViewById(R.id.buttonSave)

        setupBottomNavigation()
    }

    override fun onStart() {
        super.onStart()

        val prefs = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val userId = prefs.getInt("USER_ID_INT", 0)

        if (userId == 0) return

        lifecycleScope.launch(Dispatchers.IO) {
            val config = DatabaseHelper.getNotificationConfig(userId)
            withContext(Dispatchers.Main) {
                switchAluguel.isChecked = config.notificaAluguel
                switchDevolucao.isChecked = config.lembreteDevolucao
                switchRetirada.isChecked = config.lembreteRetirada
                switchRecomendacoes.isChecked = config.novosLivros
            }

            saveButton.setOnClickListener {
                lifecycleScope.launch(Dispatchers.IO) {

                    val currentConfig = DatabaseHelper.getNotificationConfig(userId)


                    currentConfig.notificaAluguel = switchAluguel.isChecked
                    currentConfig.lembreteDevolucao = switchDevolucao.isChecked
                    currentConfig.lembreteRetirada = switchRetirada.isChecked
                    currentConfig.novosLivros = switchRecomendacoes.isChecked


                    val success = DatabaseHelper.saveNotificationConfig(currentConfig)

                    withContext(Dispatchers.Main) {
                        if (success) {
                            Toast.makeText(applicationContext, "Salvo com sucesso!", Toast.LENGTH_SHORT).show()

                        } else {
                            Toast.makeText(applicationContext, "Erro ao salvar.", Toast.LENGTH_SHORT).show()
                        }
                        val intent = Intent(this@NotificacaoActivity, PerfilActivityUser::class.java)
                        startActivity(intent)
                    }
                }
            }
        }
            }
        }

