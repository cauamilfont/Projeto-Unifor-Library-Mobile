package com.example.telaslivros

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
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
    lateinit var btnFinalizar: Button
    lateinit var layoutBotoesAprovacao: LinearLayout

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
        btnFinalizar = findViewById(R.id.btnFinalizar)
        layoutBotoesAprovacao = findViewById(R.id.layoutBotoesAprovacao)


        setupBottomNavigation()
    }

    override fun onStart() {
        super.onStart()

        val bookId = intent.getIntExtra("BOOK_ID", 0)
        val userIdString = intent.getStringExtra("USER_INFO")
        val userId = userIdString?.toIntOrNull()

        lifecycleScope.launch(Dispatchers.IO) {
            val book = DatabaseHelper.getBookById(bookId)

            val userRequest = DatabaseHelper.getUser(userId!!)

            if(book != null && userRequest != null) {
                withContext(Dispatchers.Main) {
                    title.text = "Título: ${book.title}"
                    author.text = "Autor: ${book.author}"
                    user.text = "Nome: ${userRequest.nomeCompleto}"
                    requestDate.text = intent.getStringExtra("R_DATE")
                    initialDate.text = intent.getStringExtra("I_DATE")
                    finalDate.text = intent.getStringExtra("F_DATE")


                    Glide.with(this@DetailsRequestActivity)
                        .load(book.coverImage)
                        .placeholder(R.drawable.ic_book_placeholder)
                        .error(R.drawable.ic_book_error)
                        .into(cover)
                }
            }


        }





        val statusString = intent.getStringExtra("RENT_STATUS")
        val statusEnum = try { Status.valueOf(statusString ?: "PENDENTE") } catch (e: Exception) { Status.PENDENTE }
        val rentId = intent.getStringExtra("EXTRA_TRANSACTION_ID")?.toIntOrNull() ?: 0


        if (statusEnum == Status.APROVADO) {

            layoutBotoesAprovacao.visibility = View.GONE
            btnFinalizar.visibility = View.VISIBLE
        } else if (statusEnum == Status.PENDENTE) {

            layoutBotoesAprovacao.visibility = View.VISIBLE
            btnFinalizar.visibility = View.GONE
        } else {

            layoutBotoesAprovacao.visibility = View.GONE
            btnFinalizar.visibility = View.GONE
        }

        btnAccept.setOnClickListener {


            val requesterIdString = intent.getStringExtra("USER_INFO")

            val requesterId = requesterIdString?.toIntOrNull() ?: 0


            if (rentId == 0) {
                Toast.makeText(this, "Erro: ID da solicitação inválido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            lifecycleScope.launch(Dispatchers.IO) {


                val codigoGerado = DatabaseHelper.approveRent(rentId)




                    if (codigoGerado != null) {

                        val userConfig = DatabaseHelper.getNotificationConfig(requesterId)
                        withContext(Dispatchers.Main) {

                            Toast.makeText(
                                applicationContext,
                                "Solicitação Aprovada! Código: $codigoGerado",
                                Toast.LENGTH_LONG
                            ).show()

                            val deveNotificar = userConfig.notificaAluguel


                            if (deveNotificar) {

                                NotificationHelper.showNotification(
                                    this@DetailsRequestActivity,
                                    "Solicitação Aprovada",
                                    "O usuário foi notificado que o livro está pronto para retirada."
                                )

                            } else {

                                Log.d(
                                    "NOTIFICACAO",
                                    "Usuário optou por não receber notificações de aluguel."
                                )
                            }

                            finish()
                        }

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

        btnFinalizar.setOnClickListener {
            if (rentId == 0) return@setOnClickListener

            lifecycleScope.launch(Dispatchers.IO) {
                val sucesso = DatabaseHelper.finalizeRent(rentId)

                withContext(Dispatchers.Main) {
                    if (sucesso) {
                        Toast.makeText(applicationContext, "Livro devolvido com sucesso!", Toast.LENGTH_SHORT).show()

                        // Volta para a lista
                        val intent = Intent(this@DetailsRequestActivity, ManageRequestsActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(applicationContext, "Erro ao finalizar.", Toast.LENGTH_SHORT).show()
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