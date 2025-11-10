package com.example.telaslivros

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    lateinit var login: Button
    lateinit var register: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        login = findViewById(R.id.btnEntrar)
        register = findViewById(R.id.btnCriarConta)


    }

    override fun onStart() {
        super.onStart()

        val sessionPrefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val logged = sessionPrefs.getBoolean("IS_LOGGED_IN", false)
        val role = sessionPrefs.getString("USER_ROLE", null)
        if(logged){
            if(role.equals("user", ignoreCase = true)) {
                val intent = Intent(this, ExploreBooksActivity::class.java)
                startActivity(intent)
            }
            if(role.equals("admin", ignoreCase = true)) {
                val intent = Intent(this, AdminPanelActivity::class.java)
                startActivity(intent)
            }
        }

        login.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        register.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

    }
}