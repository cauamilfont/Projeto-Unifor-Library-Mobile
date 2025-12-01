package com.example.telaslivros

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DetailsRequestActivity : BaseActivity() {
    override fun getBottomNavItemId() = 0;

    lateinit var btnAccept: FloatingActionButton
    lateinit var btnDecline : FloatingActionButton
    lateinit var title : TextView
    lateinit var author : TextView
    lateinit var user : TextView
    lateinit var requestDate : TextView
    lateinit var initialDate : TextView
    lateinit var finalDate : TextView
    lateinit var cover : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_detalhes_solicitacao)
        btnAccept = findViewById(R.id.btnAprovar)
        btnDecline = findViewById(R.id.btnRecusar)
        title = findViewById(R.id.tvTituloLivro)
        author = findViewById(R.id.tvAutorLivro)
        user = findViewById(R.id.tvNomeSolicitante)
        requestDate = findViewById(R.id.tvDataSolicitacao)
        initialDate = findViewById(R.id.tvDataInicial)
        finalDate = findViewById(R.id.tvDataFinal)
        cover = findViewById(R.id.imgLivro)


        setupBottomNavigation()
    }

    override fun onStart() {
        super.onStart()


        val imageURL = intent.getStringExtra("IMAGE_URL")


        title.text = "Título: ${intent.getStringExtra("TITLE")}"
        author.text = "Autor: ${intent.getStringExtra("AUTHOR")}"
        user.text = "Nome: ${intent.getStringExtra("USER")}"
        requestDate.text = intent.getStringExtra("R_DATE")
        initialDate.text = intent.getStringExtra("I_DATE")
        finalDate.text = intent.getStringExtra("F_DATE")


        Glide.with(this)
            .load(imageURL)
            .placeholder(R.drawable.ic_book_placeholder)
            .error(R.drawable.ic_book_error)
            .into(cover)

        btnAccept.setOnClickListener {

            // 1. Recuperar o ID da solicitação que veio da tela anterior (Adapter)
            // Ajuste a chave "EXTRA_TRANSACTION_ID" se você usou outro nome no Adapter
            val rentIdString = intent.getStringExtra("EXTRA_TRANSACTION_ID")
            val rentId = rentIdString?.toIntOrNull() ?: 0

            // Validação de segurança
            if (rentId == 0) {
                Toast.makeText(this, "Erro: ID da solicitação inválido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 2. Inicia a operação em SEGUNDO PLANO (IO)
            lifecycleScope.launch(Dispatchers.IO) {

                // Chama a sua função no DatabaseHelper
                // Ela vai atualizar o status para 'Aprovado' e gerar o código
                val codigoGerado = DatabaseHelper.approveRent(rentId)

                // 3. Volta para a Thread PRINCIPAL (Main) para mexer na tela
                withContext(Dispatchers.Main) {

                    if (codigoGerado != null) {
                        // --- SUCESSO ---
                        Toast.makeText(
                            applicationContext,
                            "Solicitação Aprovada! Código: $codigoGerado",
                            Toast.LENGTH_LONG
                        ).show()

                        // Navega de volta para a lista de solicitações
                        val intent = Intent(this@DetailsRequestActivity, ManageRequestsActivity::class.java)

                        // (Opcional) Essas flags limpam a tela de detalhes da memória,
                        // para que o botão voltar não traga o usuário aqui de novo.
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK

                        startActivity(intent)
                        finish() // Fecha a tela atual

                    } else {
                        // --- ERRO ---
                        Toast.makeText(
                            applicationContext,
                            "Erro ao aprovar. Tente novamente.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        btnDecline.setOnClickListener {
            val rentIdString = intent.getStringExtra("EXTRA_TRANSACTION_ID")
            val rentId = rentIdString?.toIntOrNull() ?: 0

            if (rentId == 0) {
                Toast.makeText(this, "Erro: ID inválido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }



            lifecycleScope.launch(Dispatchers.IO) {

                val sucesso = DatabaseHelper.declineRent(rentId)

                withContext(Dispatchers.Main) {
                    if (sucesso) {
                        Toast.makeText(this@DetailsRequestActivity, "Solicitação recusada.", Toast.LENGTH_SHORT).show()


                        val intent = Intent(this@DetailsRequestActivity, ManageRequestsActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@DetailsRequestActivity, "Erro ao recusar.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}