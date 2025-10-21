package com.example.telaslivros

import android.os.Bundle


class MyRentsActivity : BaseActivity() {
    override fun getBottomNavItemId() = R.id.navigation_alugueis;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_meus_alugueis)

        setupBottomNavigation()
    }
}