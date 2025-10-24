package com.example.telaslivros

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.core.view.size


abstract class BaseActivity : AppCompatActivity() {
    abstract fun getBottomNavItemId(): Int

    private lateinit var bottomNav: BottomNavigationView

    override fun onResume() {
        super.onResume()

        // Verifica se a bottomNav foi inicializada antes de usá-la
        if (::bottomNav.isInitialized) {
            val itemId = getBottomNavItemId()
            if (itemId != 0) {
                val menuItem = bottomNav.menu.findItem(itemId)
                menuItem?.isChecked = true
            } else {
                // Remove a seleção se for uma tela secundária
                bottomNav.menu.setGroupCheckable(0, true, false)
                for (i in 0 until bottomNav.menu.size) {
                    bottomNav.menu[i].isChecked = false
                }
                bottomNav.menu.setGroupCheckable(0, true, true)
            }
        }
    }

    protected fun setupBottomNavigation() {
        bottomNav = findViewById(R.id.bottom_navigation)

        // 2. Lê o papel (role) do SharedPreferences
        val sharedPrefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val userRole = sharedPrefs.getString("EMAIL", "user") ?: "user"

        // 3. Limpa qualquer menu antigo e infla o menu correto
        bottomNav.menu.clear()

        if (userRole.equals("admin", ignoreCase = true)) {
            // Se for ADMIN
            bottomNav.inflateMenu(R.menu.bottom_nav_menu_admin)
            setupAdminNavigation()
        } else {
            // Se for USER (padrão)
            bottomNav.inflateMenu(R.menu.bottom_nav_menu)
            setupUserNavigation()
        }
    }

    private fun setupUserNavigation() {
        bottomNav.setOnItemSelectedListener { item ->
            if (item.itemId != getBottomNavItemId()) {
                when (item.itemId) {
                    R.id.navigation_inicio -> {
                        startActivity(Intent(this, ExploreBooksActivity::class.java))
                        suppressTransition()
                    }
                    R.id.navigation_alugueis -> {
                        startActivity(Intent(this, MyRentsActivity::class.java))
                        suppressTransition()
                    }
                    R.id.navigation_perfil -> {
                        startActivity(Intent(this, PerfilActivityUser::class.java))
                        suppressTransition()
                    }
                }
            }
            true
        }
    }

    private fun setupAdminNavigation() {
        bottomNav.setOnItemSelectedListener { item ->
            if (item.itemId != getBottomNavItemId()) {
                when (item.itemId) {
                    R.id.navigation_admin_moderacao -> {
                         startActivity(Intent(this, ModerationActivity::class.java))
                        suppressTransition()
                    }
                    R.id.navigation_admin_dashboard -> {
                        // (Crie esta Activity quando precisar)
                        // startActivity(Intent(this, DashboardActivity::class.java))
                        suppressTransition()
                    }
                    R.id.navigation_perfil -> {
                        // A tela de perfil é compartilhada!
                        startActivity(Intent(this, PerfilActivityAdmin::class.java))
                        suppressTransition()
                    }
                }
            }
            true
        }
    }

    private fun suppressTransition() {
        @Suppress("DEPRECATION")
        overridePendingTransition(0, 0)
    }
}
