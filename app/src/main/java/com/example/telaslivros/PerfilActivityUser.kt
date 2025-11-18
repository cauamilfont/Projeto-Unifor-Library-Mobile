package com.example.telaslivros

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.edit
import com.bumptech.glide.Glide


class PerfilActivityUser : BaseActivity() {

    lateinit var userName : TextView
    lateinit var userEmail : TextView
    lateinit var editProfile: Button
    lateinit var changePassword: Button
    lateinit var notificationConfig: Button
    lateinit var userImage : ImageView
    lateinit var logout: Button

    override fun getBottomNavItemId() = R.id.navigation_perfil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil_usuario)
        userName = findViewById(R.id.textViewUserName)
        userEmail = findViewById(R.id.textViewUserEmail)
        editProfile = findViewById(R.id.buttonEditProfile)
        changePassword = findViewById(R.id.buttonChangePassword)
        notificationConfig = findViewById(R.id.buttonNotifications)
        userImage = findViewById(R.id.imageViewProfile)
        logout = findViewById(R.id.buttonLogout)

        setupBottomNavigation()
    }

    override fun onStart(){
        super.onStart()
        val sessionPrefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val userId = sessionPrefs.getInt("USER_ID", 0)

        val user = DatabaseHelper.getUser(userId)
        if(user != null) {
            userName.text = user.nomeCompleto
            userEmail.text = user.email

            Glide.with(this)
                .load(user.fotoPerfil)
                .placeholder(R.drawable.ic_person_edit)
                .circleCrop()
                .into(userImage)
        }

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

            sessionPrefs.edit {
                putBoolean("IS_LOGGED_IN", false)
            }
            val intent = Intent(this, LoginActivity::class.java )
            startActivity(intent)
        }

    }
}