package com.example.telaslivros

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.core.content.edit


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

        logout.setOnClickListener {
            val sessionPrefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
            sessionPrefs.edit {
                putBoolean("IS_LOGGED_IN", false)
            }
            val intent = Intent(this, LoginActivity::class.java )
            startActivity(intent)
        }

    }
}