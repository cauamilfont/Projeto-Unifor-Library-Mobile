package com.example.telaslivros

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Calendar


class BookDetailsActivity : BaseActivity() {
    override fun getBottomNavItemId() = 0;

    lateinit var requestButton : Button
    lateinit var title : TextView
    lateinit var author : TextView
    lateinit var synopsys : TextView
    lateinit var bookQuality : TextView
    lateinit var physicalQuality : TextView
    lateinit var bookRB : RatingBar
    lateinit var physicalRB : RatingBar
    lateinit var cover : ImageView
    lateinit var btnBack : ImageView
    lateinit var etInitialDate : TextInputEditText
    lateinit var etFinalDate : TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_detalhes_livro)
        requestButton = findViewById(R.id.btnRequest)
        title = findViewById(R.id.tvTituloLivro)
        author = findViewById(R.id.tvAutorLivro)
        synopsys = findViewById(R.id.tvSinopse)
        bookQuality = findViewById(R.id.tvBookQuality)
        physicalQuality = findViewById(R.id.tvPhysicalQuality)
        bookRB = findViewById(R.id.rbQualidade)
        physicalRB = findViewById(R.id.rbFisico)
        cover = findViewById(R.id.ivCapaLivro)
        btnBack = findViewById(R.id.backButton)
        etInitialDate = findViewById(R.id.etDataInicial)
        etFinalDate = findViewById(R.id.etDataFinal)

        setupBottomNavigation()
    }

    override fun onStart() {
        super.onStart()
        title.text = "Título: ${intent.getStringExtra("TITLE")}"
        author.text = "Autor: ${intent.getStringExtra("AUTHOR")}"
        synopsys.text = intent.getStringExtra("SYNOPSYS")
        bookQuality.text = "${intent.getFloatExtra("BOOK_QUALITY", 0.0F)}/5"
        physicalQuality.text = "${intent.getFloatExtra("PHYSICAL_QUALITY", 0.0F)}/5"
        bookRB.rating = intent.getFloatExtra("BOOK_QUALITY", 0.0F)
        physicalRB.rating = intent.getFloatExtra("PHYSICAL_QUALITY", 0.0F)
        Glide.with(this)
            .load(intent.getStringExtra("URL_IMAGE"))
            .placeholder(R.drawable.ic_book_placeholder)
            .error(R.drawable.ic_book_error)
            .into(cover)

        requestButton.setOnClickListener {
            val inputInitialDate = convertToLocalDate(etInitialDate.text.toString())
            val inputFinalDate = convertToLocalDate(etFinalDate.text.toString())


                if (inputInitialDate == null) {
                    etInitialDate.error = "Data inválida! Use DD/MM/AAAA"
                    return@setOnClickListener
                }
                if (inputFinalDate == null) {
                    etInitialDate.error = "Data inválida! Use DD/MM/AAAA"
                    return@setOnClickListener
                }
            lifecycleScope.launch(Dispatchers.IO) {
                val disponibility =
                    DatabaseHelper.checkDisponibility(1, inputInitialDate, inputFinalDate)
                val sessionPrefs = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                val userId = sessionPrefs.getInt("USER_ID_INT", 0)
                val bookId = intent.getIntExtra("BOOK_ID", 0)


                if (userId == 0) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            applicationContext,
                            "Faça login novamente",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    return@launch
                }

                if (!disponibility) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            applicationContext,
                            "O livro não está disponível nesse período",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    return@launch

            }
                Log.e("BOOKID", bookId.toString())
            val rent = Rent(
                bookId = bookId,
                userId = userId,
                initialDate = inputInitialDate ,
                finalDate =  inputFinalDate,
                book = null,
                userName = ""
                )
            val rentId = DatabaseHelper.createRent(rent)
            if(rentId == 0) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        applicationContext,
                        "Ocorreu um erro ao solicitar a reserva, tente novamente!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                    return@launch
                }
                val urlImage : String? = intent.getStringExtra("URL_IMAGE")
                val intent = Intent(this@BookDetailsActivity, RentRequestActivity::class.java)
                intent.putExtra("TITLE", title.text)
                intent.putExtra("AUTHOR", author.text)
                intent.putExtra("URL_IMAGE", urlImage )
                startActivity(intent)
            }




        }

        btnBack.setOnClickListener {
            finish()
        }
        
        etInitialDate.setOnClickListener {
            showDatePickerDialog(it as TextInputEditText)
        }
        etFinalDate.setOnClickListener {
            showDatePickerDialog(it as TextInputEditText)
        }
    }

    private fun showDatePickerDialog(editText: TextInputEditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)


        val datePickerDialog = DatePickerDialog(
            this,
            { view, selectedYear, selectedMonth, selectedDayOfMonth ->
                val selectedDate = LocalDate.of(selectedYear, selectedMonth + 1, selectedDayOfMonth)

                val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                editText.setText(selectedDate.format(formatter))
            },
            year,
            month,
            day
        )


        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000


        datePickerDialog.show()
    }

    fun convertToLocalDate(dateString: String): LocalDate? {

        if (dateString.isBlank()) {
            return null
        }

        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")


        return try {
            LocalDate.parse(dateString, formatter)
        } catch (e: DateTimeParseException) {

            Log.e("DATA_CONVERSION", "Formato de data inválido ou data inexistente: $dateString", e)
            null
        }
    }
}