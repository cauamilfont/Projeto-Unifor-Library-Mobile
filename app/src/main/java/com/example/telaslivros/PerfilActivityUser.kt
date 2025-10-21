package com.example.telaslivros

import android.content.Intent
import android.os.Bundle
import android.widget.Button


class PerfilActivityUser : BaseActivity() {

    lateinit var editProfile: Button
    lateinit var changePassword: Button
    lateinit var notificationConfig: Button
    lateinit var logout: Button

    override fun getBottomNavItemId() = R.id.navigation_perfil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil_usuario)
        editProfile = findViewById(R.id.buttonEditProfile)
        changePassword = findViewById(R.id.buttonChangePassword)
        notificationConfig = findViewById(R.id.buttonNotifications)
        logout = findViewById(R.id.buttonLogout)

        setupBottomNavigation()
    }

    override fun onStart(){
        super.onStart()

        editProfile.setOnClickListener {
            val intent = Intent(this, EditProfileActivity::class.java )
            startActivity(intent)
        }

        changePassword.setOnClickListener {
            val intent = Intent(this, ChangePasswordActivity::class.java )
            startActivity(intent)
        }

        notificationConfig.setOnClickListener {
            val intent = Intent(this, NotificacaoActivity::class.java )
            startActivity(intent)
        }

    }
}