package com.example.telaslivros

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast


class EditProfileActivity : BaseActivity() {
    override fun getBottomNavItemId() = 0;

    lateinit var saveButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_edit_perfil)
        saveButton = findViewById(R.id.ButtonSave)

        setupBottomNavigation()
    }

    override fun onStart() {
        super.onStart()

        saveButton.setOnClickListener {
            Toast.makeText(applicationContext, "Alteração feita com succeso!", Toast.LENGTH_SHORT)
                .show()
            val intent = Intent(this, PerfilActivityUser::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
        }
    }
}