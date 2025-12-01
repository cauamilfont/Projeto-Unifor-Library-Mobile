package com.example.telaslivros

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NotificacaoAdminActivity : BaseActivity() {
    override fun getBottomNavItemId() = 0;

    lateinit var switchNovosPedidos: SwitchCompat
    lateinit var swicthDevolucoesAtrasadas: SwitchCompat
    lateinit var switchEdicaoLivro: SwitchCompat
    lateinit var switchAdicaoLivro: SwitchCompat
    lateinit var switchRelatosBug: SwitchCompat
    lateinit var saveButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_notificacao_admin)
        switchNovosPedidos = findViewById(R.id.switchNovoPedido)
        swicthDevolucoesAtrasadas = findViewById(R.id.switchDevolucoes)
        switchEdicaoLivro = findViewById(R.id.switchEdicaoLivro)
        switchAdicaoLivro = findViewById(R.id.switchAdicaoLivro)
        switchRelatosBug = findViewById(R.id.switchBugs)
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
                switchNovosPedidos.isChecked = config.novosPedidosAdmin
                swicthDevolucoesAtrasadas.isChecked = config.devolucoesAtrasadasAdmin
                switchEdicaoLivro.isChecked = config.edicaoLivroAdmin
                switchAdicaoLivro.isChecked = config.adicaoLivroAdmin
                switchRelatosBug.isChecked = config.relatosBugAdmin
            }

            saveButton.setOnClickListener {
                lifecycleScope.launch(Dispatchers.IO) {

                    val currentConfig = DatabaseHelper.getNotificationConfig(userId)


                    currentConfig.novosPedidosAdmin = switchNovosPedidos.isChecked
                    currentConfig.devolucoesAtrasadasAdmin = swicthDevolucoesAtrasadas.isChecked
                    currentConfig.edicaoLivroAdmin = switchEdicaoLivro.isChecked
                    currentConfig.adicaoLivroAdmin = switchAdicaoLivro.isChecked
                    currentConfig.relatosBugAdmin = switchRelatosBug.isChecked
                    Log.e("novosPedidosAdmin", switchNovosPedidos.isChecked.toString())
                    Log.e("adicaoLivroAdmin", switchAdicaoLivro.isChecked.toString())


                    val success = DatabaseHelper.saveNotificationConfig(currentConfig)

                    withContext(Dispatchers.Main) {
                        if (success) {
                            Toast.makeText(
                                applicationContext,
                                "Configurações de Admin salvas!",
                                Toast.LENGTH_SHORT
                            ).show()
                            val intent = Intent(
                                this@NotificacaoAdminActivity,
                                PerfilActivityAdmin::class.java
                            )
                            startActivity(intent)

                        }
                    }
                }
            }
        }
    }
}