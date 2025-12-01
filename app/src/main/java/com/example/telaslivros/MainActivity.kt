package com.example.telaslivros

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
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

        NotificationHelper.createChannel(this)


    }

    override fun onStart() {
        super.onStart()

        val prefs = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val token = prefs.getString("AUTH_TOKEN", null)

        Log.e("TOKEN_3", token.toString())

        if (token != null && JwtHelper.validateToken(token)) {


            val role = prefs.getString("USER_ROLE", "user")

            if (role.equals("admin", ignoreCase = true)) {
                startActivity(Intent(this, AdminPanelActivity::class.java))
            } else {
                startActivity(Intent(this, ExploreBooksActivity::class.java))
            }
            finish()
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