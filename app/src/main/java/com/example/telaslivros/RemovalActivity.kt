package com.example.telaslivros

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.compose.ui.platform.LocalGraphicsContext
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.log

class RemovalActivity : BaseActivity() {
    override fun getBottomNavItemId() = 0;

    lateinit var btnBack : ImageView
    lateinit var bookTitle : TextView
    lateinit var userName : TextView
    private lateinit var barcodeImage: ImageView
    private lateinit var barcodeNumber: TextView
    private lateinit var expiryDate: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_removal)
        btnBack = findViewById(R.id.backButton)
        bookTitle = findViewById(R.id.tvBookInfo)
        userName = findViewById(R.id.tvUserInfo)
        barcodeImage = findViewById(R.id.imageViewBarcode)
        barcodeNumber = findViewById(R.id.textViewBarcodeNumber)
        expiryDate = findViewById(R.id.textViewExpiryDate)


        setupBottomNavigation()
        loadData()
    }

    override fun onStart() {
        super.onStart()


        btnBack.setOnClickListener {
            finish()
        }

    }

    private fun loadData() {

        val title = intent.getStringExtra("BOOK_TITLE") ?: "Livro Desconhecido"
        val user = intent.getStringExtra("USER_NAME") ?: "Usuário"


        Log.e("title", title)
        Log.e("user", user)
        bookTitle.text = "Livro: $title"
        userName.text = "Usuário: $user"


        val rentIdString = intent.getStringExtra("EXTRA_TRANSACTION_ID")
        val rentId = rentIdString?.toIntOrNull() ?: 0

        if (rentId != 0) {
            fetchBarcodeFromDatabase(rentId)
        } else {
            Toast.makeText(this, "Erro ao carregar código de retirada.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchBarcodeFromDatabase(rentId: Int) {
        lifecycleScope.launch(Dispatchers.IO) {

            val codigoRetirada = DatabaseHelper.getWithdrawalCode(rentId)

            if (codigoRetirada != null) {

                val bitmap = BarcodeGenerator.generateBarcode(codigoRetirada)

                withContext(Dispatchers.Main) {
                    if (bitmap != null) {
                        barcodeImage.setImageBitmap(bitmap)
                        barcodeNumber.text = codigoRetirada

                    } else {
                        barcodeNumber.text = "Erro ao gerar imagem."
                    }
                }
            } else {
                withContext(Dispatchers.Main) {
                    barcodeNumber.text = "Código não encontrado ou pendente."
                }
            }
        }
    }
}