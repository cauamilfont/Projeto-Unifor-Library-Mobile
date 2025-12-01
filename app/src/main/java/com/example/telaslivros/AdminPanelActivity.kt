package com.example.telaslivros

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import android.widget.Toast
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AdminPanelActivity : BaseActivity() {
    override fun getBottomNavItemId() = R.id.navigation_admin_dashboard;

    lateinit var manageBooksButton: Button
    lateinit var manageRequestButton : Button
    lateinit var tvPending: TextView
    lateinit var tvRented: TextView
    lateinit var tvTotal: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.admin_panel)
        manageBooksButton = findViewById(R.id.btnGerenciarLivros)
        manageRequestButton = findViewById(R.id.btnGerenciarSolicitacoes)

        tvPending = findViewById(R.id.tvPendingRequests)
        tvRented = findViewById(R.id.tvBooksRented)
        tvTotal = findViewById(R.id.tvTotalBooks)


        setupBottomNavigation()
    }

    override fun onResume() {
        super.onResume()
        loadDashboardData()
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

    private fun loadDashboardData() {
        lifecycleScope.launch(Dispatchers.IO) {

            val stats = DatabaseHelper.getDashboardStats()

            withContext(Dispatchers.Main) {
                tvPending.text = stats.pendingRequests.toString()
                tvRented.text = stats.rentedBooks.toString()
                tvTotal.text = stats.totalBooks.toString()
            }
        }
    }
    

}