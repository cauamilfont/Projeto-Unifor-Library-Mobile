package com.example.telaslivros

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputEditText
import java.time.LocalDate
import java.time.format.DateTimeFormatter
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
            val urlImage : String? = intent.getStringExtra("URL_IMAGE")
            val intent = Intent(this, RentRequestActivity::class.java)
            intent.putExtra("TITLE", title.text)
            intent.putExtra("AUTHOR", author.text)
            intent.putExtra("URL_IMAGE", urlImage )
            startActivity(intent)
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
}