package com.example.telaslivros

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class ManageRequestsActivity : BaseActivity() {
    override fun getBottomNavItemId() = 0;


    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2
    private lateinit var pagerAdapter: RequestsPagerAdapter
    private lateinit var backBtn : ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_gerenciar_solicitacoes)
        tabLayout = findViewById(R.id.tabLayoutSolicitacoes)
        viewPager = findViewById(R.id.viewPagerSolicitacoes)
        backBtn = findViewById(R.id.ivBackBtn)

        // Crie o adapter do ViewPager
        pagerAdapter = RequestsPagerAdapter(this)
        viewPager.adapter = pagerAdapter

        // Conecte o TabLayout ao ViewPager
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Pendentes"
                1 -> "Aprovadas"
                2 -> "Finalizadas"
                else -> null
            }
        }.attach()

        setupBottomNavigation()
    }

    override fun onStart() {
        super.onStart()

        backBtn.setOnClickListener {
            val intent = Intent(this, AdminPanelActivity::class.java)
            startActivity(intent)
        }

    }

}