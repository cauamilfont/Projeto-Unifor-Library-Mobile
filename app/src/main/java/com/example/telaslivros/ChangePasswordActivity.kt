package com.example.telaslivros

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.core.content.edit
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ChangePasswordActivity : BaseActivity() {
    override fun getBottomNavItemId() = 0;

    lateinit var currentPassword : TextInputEditText
    lateinit var newPassword : TextInputEditText
    lateinit var confirmPassword : TextInputEditText
    lateinit var saveButton : Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_alterar_senha)
        currentPassword = findViewById(R.id.currentPassword)
        newPassword = findViewById(R.id.newPassword)
        confirmPassword = findViewById(R.id.confirmPassword)
        saveButton = findViewById(R.id.buttonSave)


        setupBottomNavigation()
    }

    override fun onStart() {
        super.onStart()

        saveButton.setOnClickListener {
            if(confirmPassword.text.toString() != newPassword.text.toString()){
                Toast.makeText(applicationContext, "As senhas não conferem!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val sessionPrefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
            val userId = sessionPrefs.getInt("USER_ID_INT", 0)
            if(userId == 0){
                Toast.makeText(applicationContext, "Erro, faça login novamente!!", Toast.LENGTH_SHORT).show()
                sessionPrefs.edit {
                    putBoolean("IS_LOGGED_IN", false)
                    putString("USER_ID", null)
                    putString("USER_ROLE", null)
                }
                val intent = Intent(this, LoginActivity::class.java )
                startActivity(intent)
            }

            lifecycleScope.launch(Dispatchers.IO) {
                val verifyPassword = DatabaseHelper.verifyPassword(currentPassword.text.toString(), userId)
                withContext(Dispatchers.Main) {
                    if (!verifyPassword) {
                        Toast.makeText(
                            applicationContext,
                            "Senha Incorreta!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    val changePassword = DatabaseHelper.changePassword(newPassword.text.toString(), userId)
                    if(changePassword){
                        Toast.makeText(applicationContext, "Senha alterada com sucesso!", Toast.LENGTH_SHORT).show()
                        finish()
                    }


                }
            }
            Toast.makeText(applicationContext, "Senha alterada com sucesso!", Toast.LENGTH_SHORT)
                .show()
            val intent = Intent(this, PerfilActivityUser::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
        }
    }



}