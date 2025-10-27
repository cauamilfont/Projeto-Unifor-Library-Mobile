package com.example.telaslivros

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast


class ChangePasswordActivity : BaseActivity() {
    override fun getBottomNavItemId() = 0;

    lateinit var saveButton : Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_alterar_senha)
        saveButton = findViewById(R.id.buttonSave)


        setupBottomNavigation()
    }

    override fun onStart() {
        super.onStart()

        saveButton.setOnClickListener {
            Toast.makeText(applicationContext, "Senha alterada com sucesso!", Toast.LENGTH_SHORT)
                .show()
            val intent = Intent(this, PerfilActivityUser::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
        }
    }



}