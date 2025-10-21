package com.example.telaslivros

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.core.view.size


abstract class BaseActivity : AppCompatActivity() {
    abstract fun getBottomNavItemId(): Int

    override fun onResume() {
        super.onResume()
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val itemId = getBottomNavItemId()
        if (itemId != 0) {
            val menuItem = bottomNav?.menu?.findItem(itemId)
            menuItem?.isChecked = true
        } else {
            bottomNav?.menu?.setGroupCheckable(0, true, false)
            for (i in 0 until (bottomNav?.menu?.size ?: 0)) {
                bottomNav?.menu?.get(i)?.isChecked = false
            }
            bottomNav?.menu?.setGroupCheckable(0, true, true)
        }
    }

    protected fun setupBottomNavigation() {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        bottomNav.setOnItemSelectedListener { item ->
            if (item.itemId != getBottomNavItemId()) {
                when (item.itemId) {
                    R.id.navigation_inicio -> {
                        startActivity(Intent(this, ExploreBooksActivity::class.java))
                        @Suppress("DEPRECATION")
                        overridePendingTransition(0, 0)
                    }
                    R.id.navigation_alugueis -> {
                        startActivity(Intent(this, MyRentsActivity::class.java))
                        @Suppress("DEPRECATION")
                        overridePendingTransition(0, 0)
                    }
                    R.id.navigation_perfil -> {
                        startActivity(Intent(this, PerfilActivityUser::class.java))
                        @Suppress("DEPRECATION")
                        overridePendingTransition(0, 0)
                    }
                }
            }
            true
        }
        }
    }