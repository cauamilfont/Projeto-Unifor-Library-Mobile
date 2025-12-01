package com.example.telaslivros

import android.os.Bundle
import android.widget.ImageButton
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class MyRentsActivity : BaseActivity() {


    override fun getBottomNavItemId() = R.id.navigation_alugueis


    private lateinit var backBtn: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_meus_alugueis)

        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        val viewPager = findViewById<ViewPager2>(R.id.viewPager)
        backBtn = findViewById(R.id.backBtn)

        val adapter = RentsPagerAdapter(this)
        viewPager.adapter = adapter


        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Em Análise"
                1 -> "Aprovados"
                2 -> "Finalizados"
                else -> null
            }
        }.attach()


        backBtn.setOnClickListener {
            finish()
        }


        setupBottomNavigation()
    }
}