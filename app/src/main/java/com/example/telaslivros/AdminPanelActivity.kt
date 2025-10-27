package com.example.telaslivros

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

class AdminPanelActivity : BaseActivity() {
    override fun getBottomNavItemId() = R.id.navigation_admin_dashboard;

    lateinit var manageBooksButton: Button
    lateinit var manageRequestButton : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.admin_panel)
        manageBooksButton = findViewById(R.id.btnGerenciarLivros)
        manageRequestButton = findViewById(R.id.btnGerenciarSolicitacoes)


        setupBottomNavigation()
    }

    override fun onStart() {
        super.onStart()

        manageBooksButton.setOnClickListener {
            val intent = Intent(this, ManageBooksActivity::class.java)
            startActivity(intent)
        }

        manageRequestButton.setOnClickListener {
            val intent = Intent(this, ManageRequestsActivity::class.java)
            startActivity(intent)
        }

    }
    

}