package com.example.telaslivros

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.RatingBar
import android.widget.Toast
import androidx.compose.ui.unit.IntRect
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ReviewActivity : BaseActivity() {
    override fun getBottomNavItemId() = 0;

    lateinit var backBtn : ImageButton
    lateinit var sendBtn : Button
    lateinit var rbContent: RatingBar
    lateinit var rbPhysic: RatingBar
    lateinit var etComment: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_review)

        backBtn = findViewById(R.id.backButton)
        sendBtn = findViewById(R.id.btnEnviarAvaliacao)
        rbContent = findViewById(R.id.ratingBarConteudo)
        rbPhysic = findViewById(R.id.ratingBarFisico)
        etComment = findViewById(R.id.etComentario)


        setupBottomNavigation()
    }

    override fun onStart() {
        super.onStart()

        val bookId = intent.getIntExtra("BOOK_ID", 0)
        val prefs = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val userId = prefs.getInt("USER_ID_INT", 0)
        Log.e("Userid", userId.toString())

        if (bookId != 0 && userId != 0) {
            checkExistingReview(userId, bookId)
        }

        backBtn.setOnClickListener {
            finish()
        }

        sendBtn.setOnClickListener {
            if (bookId == 0 || userId == 0) {
                Toast.makeText(this, "Erro ao identificar livro.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            saveReview(userId, bookId)
            finish()
        }
    }
    private fun checkExistingReview(userId: Int, bookId: Int) {
        lifecycleScope.launch(Dispatchers.IO) {

            val existingReview = DatabaseHelper.getAvaliacao(userId, bookId)

            withContext(Dispatchers.Main) {
                if (existingReview != null) {

                    rbContent.rating = existingReview.ratingContent.toFloat()
                    rbPhysic.rating = existingReview.ratingPhysical.toFloat()
                    etComment.setText(existingReview.commentContent)

                    // Bloqueia edições
                    disableEditing()

                    Toast.makeText(this@ReviewActivity, "Você já avaliou este livro.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    private fun disableEditing() {

        rbContent.setIsIndicator(true)
        rbPhysic.setIsIndicator(true)

        etComment.isEnabled = false
        etComment.setTextColor(getColor(android.R.color.darker_gray))

        sendBtn.isEnabled = false
        sendBtn.alpha = 0.5f
        sendBtn.text = "Avaliação Enviada"
    }

    private fun saveReview(userId: Int, bookId: Int) {
        val notaConteudo = rbContent.rating.toInt()
        val notaFisica = rbPhysic.rating.toInt()
        val comentario = etComment.text.toString()

        if (notaConteudo == 0 || notaFisica == 0) {
            Toast.makeText(this, "Por favor, dê as notas.", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch(Dispatchers.IO) {
            val novaAvaliacao = Comment(
                userId = userId,
                bookId = bookId,
                bookTitle = " ",
                authorName = " ",
                ratingContent = notaConteudo,
                ratingPhysical = notaFisica,
                commentContent = comentario,
                id = 0
            )
            val sucesso = DatabaseHelper.adicionarAvaliacao(novaAvaliacao)

            withContext(Dispatchers.Main) {
                if (sucesso) {
                    Toast.makeText(this@ReviewActivity, "Avaliação enviada!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@ReviewActivity, "Erro ao enviar.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}