package com.example.telaslivros

import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class MyRentsActivity : BaseActivity() { // <--- Mude aqui se necessário

    // Se estiver usando BaseActivity, adicione esta linha
    override fun getBottomNavItemId() = R.id.navigation_alugueis

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 1. Diz ao Kotlin para usar seu arquivo XML
        setContentView(R.layout.activity_meus_alugueis)



        // 2. Encontra os componentes vazios no XML
        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        val viewPager = findViewById<ViewPager2>(R.id.viewPager)

        // 3. Cria o "cérebro" das abas (RentsPagerAdapter)
        val adapter = RentsPagerAdapter(this)
        viewPager.adapter = adapter

        // 4. Conecta as abas ao ViewPager e define os títulos
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Em Análise"
                1 -> "Aprovados"
                2 -> "Histórico"
                else -> null
            }
        }.attach()

        // 5. Liga sua barra de navegação inferior (se estiver usando BaseActivity)
        setupBottomNavigation()
    }
}